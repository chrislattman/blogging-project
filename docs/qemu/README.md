# QEMU

QEMU (Quick EMUlator) is a command line [hypervisor](https://en.wikipedia.org/wiki/Hypervisor). Like [VirtualBox](../virtualbox), you can use it to create virtual machines (VMs).

There are graphical interfaces available for QEMU, which can help you if you are not experienced/comfortable with the various QEMU flags:

- For Linux host OSes (hosts), there is [Virtual Machine Manager](https://virt-manager.org/) (also called virt-manager)
- For macOS hosts, there is [UTM](https://mac.getutm.app/)
- For Windows hosts, there is [QtEmu](https://carlavilla.es/qtemu/qtemu_setup_x86_64.exe) (direct download link)

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
    - [Creating a VM](#creating-a-vm)
    - [Running a VM](#running-a-vm)
    - [Port forwarding](#port-forwarding)
    - [Creating a snapshot](#creating-a-snapshot)
    - [Shared clipboard](#shared-clipboard)
    - [Shared folders](#shared-folders)
    - [Headless mode](#headless-mode)
    - [Convert file format](#convert-file-format)
    - [Resizing a VM](#resizing-a-vm)
    - [Importing a VM](#importing-a-vm)
    - [Emulate an executable](#emulate-an-executable)

## Installation

Linux hosts: install `qemu-utils` and `qemu-system-x86` with a [package manager](../terminal-commands#package-managers)

macOS hosts: run `brew install qemu` in Terminal

Windows hosts: install the .exe file from [this](https://qemu.weilnetz.de/w64/) page

- Note: QEMU for Windows is experimental software and may not work

## Usage

### Creating a VM

1. If you haven't already done so, make a folder where all your QEMU VMs will reside, and navigate to it:

    ```
    mkdir ~/qemu-vms
    cd ~/qemu-vms
    ```

1. Create the file that will store your VM (I am giving it 25 GB of storage):

    ```
    qemu-img create -f qcow2 myvm.qcow2 25G
    ```

1. Install the operating system (I am giving it 2 CPUs and 4 GB of memory):

    ```
    qemu-system-x86_64 -enable-kvm \
        -boot d \
        -cdrom /path/to/your/startup-disk.iso \
        -cpu host \
        -smp 2 \
        -m 4G \
        -hda myvm.qcow2
    ```

    - `-enable-kvm` is for Linux hosts only
        - For macOS hosts, replace `-enable-kvm` with `-machine type=q35,accel=hvf`
        - For Windows hosts, remove `-cpu host` and replace `-enable-kvm` with `-machine type=q35,accel=whpx,kernel_irqchip=off`
            - If you're having trouble, go to `Turn Windows features on or off` and make sure Windows Hypervisor Platform is checked (restart your computer if necessary)
            - If you're still having trouble, it just means that QEMU for Windows is buggy (use [VirtualBox](../virtualbox) instead)
    - A good rule of thumb is to give your VM a quarter of your computer's CPU and memory resources
    - On Windows and Linux hosts, release the cursor from the QEMU window by pressing `Ctrl + Alt + G`
        - On macOS hosts, press `Cmd + Option + Ctrl + G` (`Windows key + Alt + Ctrl + G` on a Windows keyboard)
    - When the installer finishes, close the QEMU window

### Running a VM

```
qemu-system-x86_64 -enable-kvm -cpu host -smp 2 -m 4G -hda ~/qemu-vms/myvm.qcow2
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Creating a VM](#creating-a-vm))
- On macOS, the QEMU window is not resizable
- Like VirtualBox VMs, a QEMU VM can access the host OS's `localhost` at `10.0.2.2`
- Advanced: adding `-D <log-file>` will output logging information to the specified file
- Optional: add `-name "My VM Name"` to give your VM a name (this will show up in the window title after "QEMU")

### Port forwarding

```
qemu-system-x86_64 -enable-kvm \
    -cpu host \
    -smp 2 \
    -m 4G \
    -hda ~/qemu-vms/myvm.qcow2 \
    -device e1000,netdev=net0 \
    -netdev user,id=net0,hostfwd=tcp:127.0.0.1:3022-:22
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Creating a VM](#creating-a-vm))
- This binds address `127.0.0.1` (localhost) and port 3022 of your host OS to port 22 (default SSH port) of the VM over TCP (UDP is available too)
- You can SSH into the VM by running `ssh -p 3022 user@127.0.0.1` in another terminal window, where `user` is the username for your VM and `127.0.0.1` is the hostname
- You can save your login credentials by following [these](../ssh#saving-your-login-to-the-server) instructions
- Long commands like this are suitable targets for an [alias](../terminal-commands#aliasing)
- Advanced: forward another port by adding another `hostfwd` key-value pair, e.g.
    ```
    -netdev user,id=net0,hostfwd=tcp:127.0.0.1:3022-:22,hostfwd=tcp:127.0.0.1:2159-:2159
    ```
- QEMU VMs use NAT by default, hence the need for port forwarding
    - It is possible to do more elaborate networking, [these instructions](https://wiki.qemu.org/Documentation/Networking/NAT) show how to set up a NAT network

### Creating a snapshot

Snapshots are useful when you want to test something that could potentially break your VM.

Create a snapshot of a VM by running

```
qemu-img create -f qcow2 -b ~/qemu-vms/myvm.qcow2 -F qcow2 ~/qemu-vms/mysnapshot.qcow2
```

- Despite the name, it is really `myvm.qcow2` file that becomes the "snapshot"

You can use the snapshot by running

```
qemu-system-x86_64 -enable-kvm -cpu host -smp 2 -m 4G -hda ~/qemu-vms/mysnapshot.qcow2
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Creating a VM](#creating-a-vm))
- Any changes to `mysnapshot.qcow2` will not affect `myvm.qcow2`
- However, any changes to `myvm.qcow2` will corrupt `mysnapshot.qcow2`
- Delete a snapshot when:
    - You are done using it
    - You want to use the regular VM again
    - You want to create another snapshot

If you want to save the changes from your snapshot, run

```
qemu-img commit ~/qemu-vms/mysnapshot.qcow2
```

Now you can safely delete `mysnapshot.qcow2` with your changes persisted to `myvm.qcow2`.

### Shared clipboard

For Linux desktop VMs (i.e. not server environments), install `spice-vdagent` in the VM using a [package manager](../terminal-commands#package-managers).

For Windows VMs, install [this](https://www.spice-space.org/download/windows/spice-guest-tools/spice-guest-tools-latest.exe) executable in the VM.

You should now be able to copy and paste between your host OS and your VM.

### Shared folders

Unfortunately, shared folders are not easy to configure with QEMU.

However, you can use [`scp`](../ssh#scp) or [`sftp`](../ssh#sftp) from your host OS to copy files and directories to and from your VM. Note: some VMs will require you to manually set up `openssh-server`.

**Pro tip:** if you just want to share a folder's contents from your host OS to your VM, run [`ifconfig`](../terminal-commands#ifconfig) (or `ipconfig` on Windows) in your host OS to see your computer's local IP address. Take note of this IP address.

Then run the following commands in your host OS:

```
cd <folder-you-want-to-share>
python3 -m http.server
```

- Python 3 comes pre-installed with macOS
- To install Python 3 on Windows, [download](https://www.microsoft.com/store/productId/9PJPW5LDXLZ5) it from the Microsoft Store
- On Windows, make sure to run these commands in Windows PowerShell

Now, in the VM, visit `http://<local-ip-address>:8000` from a web browser, using the IP address from earlier, and now you should be able to see those files from your VM.

- You can change the port by appending an optional port number to the command, e.g. `python3 -m http.server 1234`
- This works not only for VMs but for _any device_ on your local area network (LAN)!
    - This is because `http.server` by default is bound to 0.0.0.0, which means it is bound to ALL network interfaces
        - This includes your Wi-Fi or Ethernet interface, which means all devices connected to your LAN can access the server
    - You can restrict this by binding to localhost only: `python3 -m http.server --bind 127.0.0.1 [1234]`
    - From the VM, you can access this server at `http://10.0.2.2[:1234]`
- This also works the other way around (sharing a folder's contents from your VM to your host OS)
    - Make sure to access the folder by using the VM's local IP address in this scenario
    - The VM's folder will not be accessible to other devices on your LAN (only your host OS knows about your VM)

Note: if for some reason you want to use FTP instead of HTTP, run these commands in your host OS:

```
pip3 install pyftpdlib
cd <folder-you-want-to-share>
python3 -m pyftpdlib -w
```

- Like `http.server`, you can bind `pyftpdlib` to localhost only and/or specify a port number: `python3 -m pyftpdlib -w -i 127.0.0.1 [-p 1234]`
    - Like the HTTP server, you can access the FTP server in the VM at `http://10.0.2.2[:1234]`
- Advanced:
    - By default, this server runs in passive mode
    - If the server requires no password to login, it accepts the username "anonymous" with an empty password

Then you can use [`curl`](../terminal-commands#curl) in a terminal on the VM to view the directory contents, as well as download _and_ upload files:

- `curl ftp://<local-ip-address>:2121` to view files
- `curl ftp://<local-ip-address>:2121/<file> -o <file>` to download a file
    - If you want to download a folder, use [`wget`](../terminal-commands#wget): `wget -r -nH ftp://<local-ip-address>:2121/<folder>`
- `curl ftp://<local-ip-address>:2121 -T <file>` to upload a file

If you don't want to use the terminal, you can use the popular FTP GUI client [FileZilla](https://filezilla-project.org/download.php?type=client), or you can use the file manager that comes with your OS (e.g. Files, File Explorer, Finder).

### Headless mode

To run a VM in headless mode:

```
qemu-system-x86_64 -enable-kvm \
    -cpu host \
    -smp 2 \
    -m 4G \
    -hda myvm.qcow2 \
    -device e1000,netdev=net0 \
    -netdev user,id=net0,hostfwd=tcp:127.0.0.1:3022-:22 \
    -display none
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Creating a VM](#creating-a-vm))
- Append a [`&`](../terminal-commands#run-in-background) to the command to run it in the background
- You will have to wait until the VM is ready
- You can SSH into the VM by running `ssh -p 3022 user@127.0.0.1`, where `user` is the username for your VM and `127.0.0.1` is the hostname
    - From there, you can shut it down (recommended), or you can [`kill`](../terminal-commands#kill) the `qemu-system-x86_64` process

### Convert file format

You may want to export a VM for use with another hypervisor, i.e. VirtualBox or VMware. You can do this by running

```
qemu-img convert -f qcow2 -O vdi myvm.qcow2 myvboxdisk.vdi
```

- The `-O` has an uppercase letter O, not a zero
- In this example, you can use `myvboxdisk.vdi` in a new VirtualBox VM as the virtual hard disk, and without needing to specify an `.iso` file
- VirtualBox uses the `.vdi` format for running VMs, and the `.vmdk` format for exporting VMs
- VMware uses the `.vmdk` format within its `.vmwarevm` directory format

### Resizing a VM

You can increase the size of a VM's storage space by running

```
qemu-img resize -f qcow2 myvm.qcow2 +10G
```

- Here I am adding 10 GB of storage to `myvm.qcow2` (you can add any amount you want)
- You will need to use file system partitioning software within your VM to actually use this extra space
- You can also use this command with the `--shrink` flag and replacing the `+` with a `-` to shrink the storage space, but this may corrupt your VM (not recommended)

### Importing a VM

Note: this assumes the VM you are importing is stored in one `.vmdk` file.

You may run into files with the `.ova` extension, which refers to an [Open Virtualization Format](https://en.wikipedia.org/wiki/Open_Virtualization_Format) archive.

- They are simply [tar archives](../terminal-commands#compression-zip-and-tar) that contain at least two files about an exported VM:
    - An `.ovf` file, which is an [XML](https://en.wikipedia.org/wiki/XML) file containing metadata about the VM
        - Namely, this file will tell you how many CPUs and how much memory this VM should use, as well as any port forwarding rules
        - This is the QEMU equivalent of `.vbox` files for VirtualBox or `.vmx` files for VMware
    - A `.vmdk` file, which is the actual VM itself
    - Optional: a `.mf` manifest file which contains the SHA-256 hashes of the above two files
    - Optional: the `.iso` file used to create the VM
- It is the default format that hypervisors like VirtualBox and VMware use to export VMs
    - Not surprisingly, `.ova` files can be easily imported with VirtualBox and VMware
- For QEMU, you need to do the following steps:
    1. Unarchive the `.ova` file, e.g. `tar -xf myexportedvm.ova`
    1. [Convert](#convert-file-format) the `.vmdk` file to a `.qcow2` file
    1. [Run](#running-a-vm) the newly-created `.qcow2` file with the arguments specified in the `.ovf` file

### Emulate an executable

Say you are working on a x86-64 computer, but have an AArch64 executable that you need to run. Fortunately, you don't have to buy another computer just to run a simple program. Emulate it instead!

- This only works on Linux
- You will need to install `qemu-user` with a [package manager](../terminal-commands#package-managers)

Then run one of the following commands:

- `qemu-aarch64 -L /usr/aarch64-linux-gnu/ <executable>` to emulate an AArch64 executable
- `qemu-x86_64 -L /usr/x86_64-linux-gnu/ <executable>` to emulate a x86-64 executable
