# VirtualBox

VirtualBox is a [hypervisor](https://en.wikipedia.org/wiki/Hypervisor), or a program used to simulate operating systems on computers. It allows you to run multiple guest OSes (known as virtual machines) on a single host computer.

This is different from [dual-booting](https://www.howtogeek.com/187789/dual-booting-explained-how-you-can-have-multiple-operating-systems-on-your-computer/), which allows you to choose between two host OSes at boot time.

It also differs from Docker in that a virtual machine (or VM for short) is completed isolated from the host OS, whereas Docker containers share the same underlying "OS" (container engine).

- This makes Docker containers much more lightweight (they take up less storage) than VMs, which is why Docker is used a lot more in practice
- VMs also tend to run slower if you have a high-resolution screen (i.e. modern Macs)

Note: on Linux desktops, a better alternative to VirtualBox is [Virtual Machine Manager](https://virt-manager.org/), also known as virt-manager. It runs on top of [QEMU](../qemu).

On VirtualBox, the concept of snapshots can be confusing. Navigate to the Snapshots view (click bulleted list on selected VM), click "Take" to take a snapshot, then start your VM if you want to test changes that could corrupt your VM.

When you are finished:

- Click "Restore" if you want to discard the changes from your snapshot
- Click "Delete" if you want to save the changes from your snapshot

## Installing Ubuntu

Since VirtualBox isn't something we'll be using a lot in this project, I won't go into much detail. However, knowing how to install an OS on VirtualBox (or an alternative like VMware) is valuable knowledge to have. There are several tutorials online on how to install Ubuntu on VirtualBox. Here are a few:

- https://www.geeksforgeeks.org/how-to-install-ubuntu-on-virtualbox/
- https://brb.nci.nih.gov/seqtools/installUbuntu.html (older info)
- https://ubuntu.com/tutorials/how-to-run-ubuntu-desktop-on-a-virtual-machine-using-virtualbox#1-overview (the official guide from Ubuntu)

There are also several videos on YouTube describing this installation process.

## Using Ubuntu

Once you've installed the virtual machine, you can use it like a regular operating system:

![Ubuntu screen](../img/ubuntu.png)

VirtualBox allows you to start up a VM in "Headless Start" mode:

![Headless Start](../img/headless-start.png)

This runs your VM in the background without a GUI. It treats your VM much like a detached Docker container, although by no means can Docker access it.

Regardless of whether you start your VM in "Normal Start" mode or "Headless Start" mode, you can SSH into your VM using the steps from [this](../ssh#ssh-configurations) section.

You can shut down your VM from the command line with this command: `sudo shutdown now`
