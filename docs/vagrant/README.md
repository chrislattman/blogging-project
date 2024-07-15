# Vagrant

Vagrant is software that allows for portable virtual machine (VM) creation and deployment. It can create and run VMs for various providers (what Vagrant calls hypervisors), including [VirtualBox](../virtualbox) and VMware. It is released by HashiCorp.

Vagrant is comparable to [Docker](../docker), except that while Docker runs containers using images, which can be extended in Dockerfiles, Vagrant builds VMs using boxes, which are used in Vagrantfiles.

Infrastructure as Code (IaC) software aims to automate the process of defining and provisioning VMs.

- Terraform, another HasiCorp product, uses Vagrant to define data center infrastructure (create multiple VMs)
    - Terraform also uses the concept of providers (in this case cloud infrastructure providers) and supports the major cloud computing companies (AWS, Azure, GCP, IBM Cloud, Oracle Cloud, and DigitalOcean), as well as on-premises cloud tools such as OpenStack and VMware vSphere
- From there, other IaC tools like Ansible, Puppet, Chef, or Salt can be used to provision (communicate with and install software on) those VMs
- Kubernetes does the work of both Terraform and Ansible/Puppet/Chef/Salt but for Docker containers instead of VMs

## Table of Contents

- [Installation](#installation)
- [Vagrant box vs VM](#vagrant-box-vs-vm)
- [Vagrantfile](#vagrantfile)
- [Creating and running VMs](#creating-and-running-vms)
- [Managing boxes](#managing-boxes)

## Installation

Installation instructions are available [here](https://developer.hashicorp.com/vagrant/install?ajs_aid=163cef3b-804e-4bc4-bd51-1e5f082b0447&product_intent=vagrant).

- Run `vagrant plugin install vagrant-disksize` to enable custom storage sizes using VirtualBox
- Run `vagrant plugin install vagrant-qemu` to enable using [QEMU](../qemu) as a provider

## Vagrant box vs VM

- A Vagrant box is a Vagrant environment, usually an operating system
- A VM is built using a Vagrantfile, which itself specifies what Vagrant box to use
    - Unlike Dockerfiles, Vagrantfiles are required to deploy a VM

## Vagrantfile

- A script of Ruby code that tells Vagrant how to build a VM
- [Here](https://gist.github.com/chrislattman/d44aa5eed259c5640589061eba764f87) is an example Vagrantfile

## Creating and running VMs

With the exception of `vagrant global-status`, these commands must either be executed in a folder with a Vagrantfile in it or executed with an id argument.

- `vagrant global-status --prune`
    - Returns an updated list of the ids for all Vagrant environments

- `vagrant up [id]`
    - Starts up a VM

- `vagrant ssh [id]`
    - Logs in to a VM via SSH

- `vagrant halt [id]`
    - Shuts down a VM

- `vagrant destroy [id]`
    - Removes a VM

## Managing boxes

- `vagrant box add <box>`
    - Downloads a box from [Vagrant Cloud](https://app.vagrantup.com/)

- `vagrant box list`
    - Shows the list of downloaded boxes

- `vagrant box update --box <box>`
    - Updates a box

- `vagrant box remove <box>`
    - Removes a box
