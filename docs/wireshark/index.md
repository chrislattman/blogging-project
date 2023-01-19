# Wireshark/TShark

Wireshark is a packet analyzer that allows you to capture packets on some network interface (normally Wi-Fi or Ethernet) and save them for further analysis. A packet is a small chunk of data sent via a network connection, e.g. the Internet.

While Wireshark is a GUI application, there is also TShark (terminal Wireshark), which is the command line version of Wireshark. Both Wireshark and TShark are maintained by the Wireshark Foundation and use the same underlying libraries.

This page will go over how to use TShark. There are numerous videos available on YouTube on how to use Wireshark.

- `tcpdump` is a similar command line tool, but TShark is more robust

## Installation

To install TShark on macOS or Linux, you need to install `tshark` with a [package manager](../terminal-commands#second-honorable-mention-package-managers).

If you are not comfortable with the terminal, Wireshark can be downloaded for Windows or macOS [here](https://www.wireshark.org/download.html). For Linux, `wireshark` can be installed with a package manager.

- On Linux, you may have to run `sudo chmod +x /usr/bin/dumpcap` right after installation

## Usage

To see which network interfaces are available to TShark, run:

```
tshark -D
```

You should see an indexed list of network interfaces. Pick the interface that corresponds to the connection you want to monitor. Reference [`ifconfig`](../terminal-commands#ifconfig) to verify the name of the Wi-Fi/Ethernet device.

### Capturing Packets

To start capturing packets:

```
tshark -i <interface> [options]
```

where `<interface>` can be either the name of the interface, or the numerical index of the interface from `tshark -D`.

`[options]` can be any combination of:

- `[-P] -w capture.pcapng`, which saves the captured packets into a file named `capture.pcapng`
    - `-P` optionally outputs packet data to the terminal
    - The `capture` in `capture.pcapng` can be anything, but make sure to keep the `.pcapng` file extension
        - You may see `.pcap` elsewhere, which indicates an older format
- `-c <num-packets>`, which limits how many packets are captured
- `-f "capture-filter"`, which filters the packets to capture
    - Replace `capture-filter` with the actual filter you want to use; examples can be found [here](https://gitlab.com/wireshark/wireshark/-/wikis/CaptureFilters#examples)

To stop capturing packets, enter `Ctrl + C` (if TShark did not exit already).

### Analyzing Packets

To analyze a packet capture:

```
tshark -r capture.pcapng
```

You can filter packets by running

```
tshark -r capture.pcapng -Y "read-filter"
```

where `read-filter` is replaced with a display filter. Examples include:

- `ip.addr == 192.168.1.1`
- `tcp.port 80`
- `dns`
- `tcp.port 80 or tcp.port == 443`

More information can be found [here](https://www.wireshark.org/docs/man-pages/wireshark-filter.html).
