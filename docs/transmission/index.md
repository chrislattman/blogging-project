# Transmission

Transmission is an open source [BitTorrent](https://en.wikipedia.org/wiki/BitTorrent) client. It is used to torrent files.

Torrenting provides a decentralized way of downloading large files, which can be faster and more robust than downloading from a single server.

With that said:

- While downloading a file, you are also uploading content to the Internet
    - This is called seeding, and it can result in high bandwidth usage
    - Seeding cannot be prevented while your file is downloading (this is part of the BitTorrent protocol)
    - However, there is a way to stop seeding once the download is complete
- Torrenting files can be dangerous, as your IP address is exposed
    - This can be (mostly) mitigated by using a VPN, but this can slow your download speed tremendously
- It is illegal to torrent copyrighted material [[reference](https://en.wikipedia.org/wiki/No_Electronic_Theft_Act)]
- While torrenting gets a bad rap for its illegal usage, it can be used legitimately to download large files, e.g. OS (.iso) files
    - Examples include various Linux distributions: [Ubuntu](https://ubuntu.com/download/alternative-downloads#bittorrent), [Fedora](https://torrent.fedoraproject.org/), and [Debian](https://www.debian.org/CD/torrent-cd/)
- These days, legal torrenting isn't really necessary anymore, nor is it [popular](https://trends.google.com/trends/explore?date=all&geo=US&q=torrent)

Note: you should ALWAYS cross-reference the [SHA-256](../terminal-commands#shasum) hash of EACH file you torrent with one or more reputable sources.

## Installation

To install Transmission on macOS or Linux, you need to install `transmission-cli` with a [package manager](../terminal-commands#package-managers).

- For Linux, you also need to install `transmission-daemon` with a package manager

## Usage

To list the settings for Transmission:

```
transmission-daemon -d
```

On macOS, start the Transmission daemon (background process) with this:

```
transmission-daemon
```

For Linux, this one-time change needs to be made to use Transmission seamlessly:

1. Stop the Transmission daemon: `sudo systemctl stop transmission-daemon.service`
1. Edit `/etc/transmission-daemon/settings.json` and
    - change `"rpc-authentication-required"` to be `false`
    - change `"download-dir"` to be your Downloads folder
    - change `"incomplete-dir"` to be your Downloads folder
1. Save your changes and restart the Transmission daemon: `sudo systemctl start transmission-daemon.service`

To torrent a file:

```
transmission-remote -a <url-or-file>
```

- Torrent files end with the `.torrent` file extension, but you can specify a URL, such as https://releases.ubuntu.com/22.04/ubuntu-22.04.1-live-server-amd64.iso.torrent without having to download the actual `.torrent` file
- You may need to put quotes around the URL or file, such as
    ```
    transmission-remote -a "https://releases.ubuntu.com/22.04/ubuntu-22.04.1-live-server-amd64.iso.torrent"
    ```

To check the status of a torrent:

```
transmission-remote -l
```

You can also have your terminal repeatedly update the status of your torrent by running:

```
watch -n 1 "transmission-remote -l"
```

- You might need to install `watch` with a [package manager](../terminal-commands#package-managers)
- This will run `transmission-remote -l` every (1) second and refresh the terminal window with the latest results
- When the download is done, you can exit by entering `Ctrl + C`

When a download is complete, you can run the following command to stop seeding:

```
transmission-remote -t <id> -r
```

- `<id>` is the torrent ID of the download

To stop seeding all torrents:

```
transmission-remote -t all -r
```
