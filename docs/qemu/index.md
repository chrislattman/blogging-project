# QEMU

QEMU (Quick EMUlator) is a command line hypervisor. Like [VirtualBox](../virtualbox), you can use it to create virtual machines.

Note: on Linux, you can use [Virtual Machine Manager](https://virt-manager.org/) (also called virt-manager) as a graphical interface to QEMU.

## Installation

Install QEMU using the link [here](https://www.qemu.org/download/), and select your operating system.

## Usage

### Installing VM

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
        -cdrom /path/to/your/disk.iso \
        -cpu host \
        -smp 4 \
        -m 4G \
        -hda myvm.qcow2
    ```

    - `-enable-kvm` is a Linux-only feature
        - For macOS, replace `-enable-kvm` with `-accel hvf`
        - For Windows, replace `-enable-kvm` with `-accel whpx`
            - If that doesn't work, try following [these instructions](https://www.qemu.org/2017/11/22/haxm-usage-windows/)
    - A good rule of thumb is to give your VM a quarter of your computer's CPU and memory resources
    - On Windows and Linux, release the cursor from the QEMU window by pressing `Ctrl + Alt + G`
        - On macOS, press `Cmd + Option + Ctrl + G`
    - When the installer finishes, close the QEMU window

### Running VM

```
qemu-system-x86_64 -enable-kvm -cpu host -smp 4 -m 4G -hda ~/qemu-vms/myvm.qcow2
```

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

- This binds port 3022 of your host OS to port 22 (default SSH port) of the VM
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

You can use the snapshot by running

```
qemu-system-x86_64 -enable-kvm -cpu host -smp 4 -m 4G -hda ~/qemu-vms/mysnapshot.qcow2
```

- Any changes to `mysnapshot.qcow2` will not affect `myvm.qcow2`
- However, any changes to `myvm.qcow2` will corrupt `mysnapshot.qcow2`
- Delete a snapshot when:
    - You are done using it
    - You want to use the regular VM again
    - You want to create another snapshot

### Shared clipboard

For Linux desktop VMs (i.e. not server environments), install `spice-vdagent` in the VM using a [package manager](../terminal-commands#second-honorable-mention-package-managers).

For Windows VMs, install [this](https://www.spice-space.org/download/windows/spice-guest-tools/spice-guest-tools-latest.exe) executable in the VM.

You should now be able to copy and paste between your host OS and your VM.

### Shared folders

Unfortunately, shared folders are not easy to configure with QEMU.

However, you can use [`scp`](../ssh#scp) from your host OS to copy files and directories to and from your VM. Make sure to have [port forwarding](#port-forwarding) set up first. Note: some OSes will require you to manually set up `openssh-server`.

### Headless mode

On QEMU, headless mode is only supported for kernel images.
