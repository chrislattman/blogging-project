# QEMU

QEMU (Quick EMUlator) is a command line [hypervisor](https://en.wikipedia.org/wiki/Hypervisor). Like [VirtualBox](../virtualbox), you can use it to create virtual machines (VMs).

There are graphical interfaces available for QEMU, which can help you if you are not experienced/comfortable with the various QEMU flags:

- For Linux host OSes (hosts), there is [Virtual Machine Manager](https://virt-manager.org/) (also called virt-manager)
- For macOS hosts, there is [UTM](https://mac.getutm.app/)
- For Windows hosts, there is [QtEmu](https://carlavilla.es/qtemu/qtemu_setup_x86_64.exe) (direct download link)

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
    - [Installing a VM](#installing-a-vm)
    - [Running a VM](#running-a-vm)
    - [Port forwarding](#port-forwarding)
    - [Creating a snapshot](#creating-a-snapshot)
    - [Shared clipboard](#shared-clipboard)
    - [Shared folders](#shared-folders)
    - [Headless mode](#headless-mode)

## Installation

Linux hosts: install `qemu-utils` and `qemu-system-x86` with a [package manager](../terminal-commands#package-managers)

macOS hosts: install `qemu` with `brew`

Windows hosts: install the .exe file from [this](https://qemu.weilnetz.de/w64/) page

- Note: QEMU for Windows is experimental software and may not work

## Usage

### Installing a VM

1. If you haven't already done so, make a folder where all your QEMU VMs will reside, and navigate to it:

    ```
    mkdir ~/qemu-vms
    cd ~/qemu-vms
    ```

1. Create the file that will store your VM (I am giving it 25 GB of storage):

    ```
    qemu-img create -f qcow2 myvm.qcow2 25G
    ```

1. Install the operating system (I am giving it 4 CPUs and 4 GB of memory):

    ```
    qemu-system-x86_64 -enable-kvm \
        -boot d \
        -cdrom /path/to/your/startup-disk.iso \
        -cpu host \
        -smp 4 \
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
        - On macOS hosts, press `Cmd + Option + Ctrl + G`
    - When the installer finishes, close the QEMU window

### Running a VM

```
qemu-system-x86_64 -enable-kvm -cpu host -smp 4 -m 4G -hda ~/qemu-vms/myvm.qcow2
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Installing a VM](#installing-a-vm))
- On macOS, the QEMU window is not resizable

### Port forwarding

```
qemu-system-x86_64 -enable-kvm \
    -cpu host \
    -smp 4 \
    -m 4G \
    -hda ~/qemu-vms/myvm.qcow2 \
    -device e1000e,netdev=net0 \
    -netdev user,id=net0,hostfwd=tcp::3022-:22
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Installing a VM](#installing-a-vm))
- This binds port 3022 of your host OS to port 22 (default SSH port) of the VM over TCP (UDP is available too)
- You can SSH into the VM by running `ssh -p 3022 user@127.0.0.1` in another terminal window, where `user` is the username for your VM and `127.0.0.1` (localhost) is the hostname
- You can save your login credentials by following [these](../ssh#saving-your-login-to-the-server) instructions
- Advanced: forward another port by adding another `hostfwd` key-value pair, e.g.
    ```
    -netdev user,id=net0,hostfwd=tcp::3022-:22,hostfwd=tcp::2159-:2159
    ```

### Creating a snapshot

Snapshots are useful when you want to test something that could potentially break your VM.

Create a snapshot of a VM by running

```
qemu-img create -f qcow2 -b ~/qemu-vms/myvm.qcow2 -F qcow2 ~/qemu-vms/mysnapshot.qcow2
```

- Despite the name, it is really `myvm.qcow2` file that becomes the "snapshot"

You can use the snapshot by running

```
qemu-system-x86_64 -enable-kvm -cpu host -smp 4 -m 4G -hda ~/qemu-vms/mysnapshot.qcow2
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Installing a VM](#installing-a-vm))
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

Now you can safely delete `mysnapshot.qcow2` with your changed persisted to `myvm.qcow2`

### Shared clipboard

For Linux desktop VMs (i.e. not server environments), install `spice-vdagent` in the VM using a [package manager](../terminal-commands#package-managers).

For Windows VMs, install [this](https://www.spice-space.org/download/windows/spice-guest-tools/spice-guest-tools-latest.exe) executable in the VM.

You should now be able to copy and paste between your host OS and your VM.

### Shared folders

Unfortunately, shared folders are not easy to configure with QEMU.

However, you can use [`scp`](../ssh#scp) from your host OS to copy files and directories to and from your VM. Make sure to have [port forwarding](#port-forwarding) set up first. Note: some VMs will require you to manually set up `openssh-server`.

### Headless mode

To run a VM in headless mode:

```
qemu-system-x86_64 -enable-kvm \
    -cpu host \
    -smp 4 \
    -m 4G \
    -hda myvm.qcow2 \
    -device e1000e,netdev=net0 \
    -netdev user,id=net0,hostfwd=tcp::3022-:22 \
    -display none
```

- Replace `-enable-kvm` and `-cpu host` as necessary (refer to step 3 of [Installing a VM](#installing-a-vm))
- Append a [`&`](../terminal-commands#run-in-background) to the command to run it in the background
- You will have to wait until the VM is ready
- You can SSH into the VM by running `ssh -p 3022 user@127.0.0.1`, where `user` is the username for your VM and `127.0.0.1` (localhost) is the hostname
    - From there, you can shut it down (recommended), or you can [kill](../terminal-commands#kill) the `qemu-system-x86_64` process