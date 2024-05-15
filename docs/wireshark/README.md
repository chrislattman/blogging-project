# Wireshark/TShark

Wireshark is a packet analyzer that allows you to capture packets on some network interface (normally Wi-Fi or Ethernet) and save them for further analysis. A packet is a small chunk of data sent via a network connection, e.g. the Internet.

While Wireshark is a GUI application, there is also TShark (terminal Wireshark), which is the command line version of Wireshark. Both Wireshark and TShark are maintained by the Wireshark Foundation and use the same underlying libraries.

This page will go over how to use TShark. There are numerous videos available on YouTube on how to use Wireshark.

- `tcpdump` is a similar command line tool, and is mostly compatible with `tshark` flags
- `tshark` uses another command called `dumpcap` under the hood

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
    - [Capturing packets](#capturing-packets)
    - [Analyzing packets](#analyzing-packets)
- [`mergecap` - merge packet captures into one](#mergecap)
- [`editcap` - limit time range of packets](#editcap)

## Installation

To install TShark on Linux, you need to install `tshark` with a [package manager](../terminal-commands#package-managers).

To install TShark on macOS, run `brew install wireshark`

If you are not comfortable with the terminal, Wireshark can be downloaded for Windows or macOS [here](https://www.wireshark.org/download.html). For Linux, `wireshark` can be installed with a package manager.

- On Linux, you may have to run `sudo chmod +x /usr/bin/dumpcap` right after installation

## Usage

To see which network interfaces are available to TShark, run:

```
tshark -D
```

You should see an indexed list of network interfaces. Pick the interface that corresponds to the connection you want to monitor. Reference [`ifconfig`](../terminal-commands#ifconfig) to verify the name of the Wi-Fi/Ethernet device.

### Capturing packets

To start capturing packets:

```
tshark -i <interface> <flags> "capture filter"
```

where `<interface>` can be either the name of the interface or the numerical index of the interface from `tshark -D`.

`<flags>` can be any combination of:

- `-c <num-packets>`, which limits how many packets are captured
- `-s0`, which captures each packet in its entirety
- `[--print] -w capture.pcapng`, which saves the captured packets into a file named `capture.pcapng`
    - `--print` optionally outputs packet data to the terminal
    - The `capture` in `capture.pcapng` can be anything, but make sure to keep the `.pcapng` file extension
        - You may see `.pcap` elsewhere, which indicates an older format (`tcpdump` uses .pcap)
- `-I`, which puts a Wi-Fi interface in monitor/promiscuous mode
    - This allows you to capture Wi-Fi traffic not meant for your computer
    - This may disconnect you from the Internet, so it's best to use this option for a separate Wi-Fi adapter plugged in to your computer
- If printing to console:
    - `-nn` doesn't resolve IP addresses and port numbers to hostnames and services, respectively (this makes TShark run faster)
    - `-tad` displays human-readable timestamps (`-tttt` in `tcpdump`)

Replace `capture filter` with the actual filter you want to use; examples can be found [here](https://gitlab.com/wireshark/wireshark/-/wikis/CaptureFilters#examples). Examples include:

- `host 192.168.1.1`
- `tcp port 80`
- `port 53`
- `tcp port 80 or tcp port 443`
- `dst host 192.168.1.1`
- `tcp dst port 80`
- `tcp[tcpflags] & 0x08 != 0`
    - This filter checks that the TCP PSH flag is set
- `((ip[2:2] - ((ip[0] & 0xf) << 2)) - ((tcp[12] & 0xf0) >> 2)) != 0`
    - This filter captures TCP segments that contain (payload) data
    - It does this by subtracting from the IPv4 total packet length (`ip[2:2]`) the IPv4 header length (`(ip[0] & 0xf) << 2`) and the TCP header length (`(tcp[12] & 0xf0) >> 2`) to get the TCP data length, and checking that it's not equal to 0

To stop capturing packets, enter `Ctrl + C` (if TShark did not exit already).

### Analyzing packets

To analyze a packet capture:

```
tshark -r capture.pcapng [-x] "read filter"
```

- You can use the `-nn` and `-tad` (`-tttt` in `tcpdump`) flags here as well
- `-x` prints out the packet in hex and ASCII (just hex on `tcpdump`)
    - `-A` prints out the packet in ASCII on `tcpdump`

Replace `read filter` with a valid display filter; examples can be found [here](https://www.wireshark.org/docs/man-pages/wireshark-filter.html). Examples include:

- `ip.addr == 192.168.1.1`
- `tcp.port == 80`
- `dns`
- `tcp.port == 80 or tcp.port == 443`
- `ip.dst == 192.168.1.1`
- `tcp.dstport == 80`
- `tcp.flags & 0x08`
    - This filter checks that the TCP PSH flag is set
- `tcp.len != 0`
    - This filter captures TCP segments that contain (payload) data

`tcpdump`'s display filters are the same as its capture filters.

## `mergecap`

To merge multiple packet captures (pcaps) into one file:

```
mergecap -w merged.pcapng cap1.pcapng ... capN.pcapng
```

- `tcpslice` is a similar utility that can be installed on Linux

## `editcap`

To filter packets from a specific time window and write them to a new file:

```
editcap -A "<start-time>" -B "<stop-time>" in.pcapng out.pcapng
```

- `<start-time>` and `<stop-time>` are in the form YYYY-MM-DD HH:MM:SS[.nnnnnn], e.g. 2023-08-28 18:57:51.763066 (24-hour time, milliseconds are optional)
- `<start-time>` is inclusive, `<stop-time>` is exclusive
- This reads from `in.pcapng` and copies the filtered packets into `out.pcapng`
- `tcpslice` can also be used for this purpose, and can be installed on Linux
