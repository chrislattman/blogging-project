# QEMU

QEMU (Quick EMUlator) is a command line hypervisor. Like [VirtualBox](../virtualbox), you can use it to create virtual machines.

## Installation

Install QEMU using the link [here](https://www.qemu.org/download/), and select your operating system.

## Usage

Note: remove `sudo` when running these steps on macOS or Windows.

### Installing VM

1. If you haven't already done so, make a folder where all your QEMU VMs will reside, and navigate to it:

    ```
    mkdir ~/qemu-vms
    cd ~/qemu-vms
    ```

1. Create the file that will store your VM (I am giving it 25 GB of storage):

    ```
    sudo qemu-img create myvm.qcow2 25G
    ```

1. Install the operating system (I am giving it 4 CPUs and 4 GB of memory):

    ```
    sudo qemu-system-x86_64 -enable-kvm -boot d -cdrom /path/to/your/disk.iso -cpu host -smp 4 -m 4G -drive file=~/qemu-vms/myvm.qcow2,format=raw,index=0,media=disk
    ```

    - `-enable-kvm` is a Linux-only feature
        - For macOS, replace `-enable-kvm` with `-accel hvf`
        - For Windows, replace `-enable-kvm` with `-accel whpx`
            - If that doesn't work, try following [these instructions](https://www.qemu.org/2017/11/22/haxm-usage-windows/)
    - A good rule of thumb is to give your VM a quarter of your computer's CPU and memory resources
    - On Windows and Linux, release the cursor from the QEMU window by pressing `Ctrl + Alt + G`
        - On macOS, press `Cmd + Option + Ctrl + G`
    - The installer may hang when trying to install security updates
        - If it says "Install complete!" on the top orange banner, you can close the QEMU window

### Running VM

```
sudo qemu-system-x86_64 -enable-kvm -cpu host -m 4G -smp 4 -drive file=~/qemu-vms/ubuntuserver2204.qcow2,format=raw,index=0,media=disk
```
