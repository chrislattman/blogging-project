# Curl

Stands for "client URL" and is often written as cURL. It allows you to both download and upload files to the Internet.

- You could also run `curl -LO https://releases.ubuntu.com/20.04.3/ubuntu-20.04.3-desktop-amd64.iso` to do the same thing as the above `wget` command
- `curl` only works for individual files (`wget` is more [robust](https://daniel.haxx.se/docs/curl-vs-wget.html))
- You might also need to install `curl` with a package manager (it's better to use `wget` anyways for downloads)

Example use cases:

- `curl https://ipv4.ipleak.net/json/` outputs your IP address and rough geographic location
- `curl wttr.in` outputs a 3-day weather forecast for your current IP address location
    - `curl wttr.in/<city>` outputs a 3-day weather forecast for the given city
    - You can also specify a state to narrow down the right city, e.g. `curl wttr.in/manhattan,ks`

`curl` is a very powerful tool for testing web applications. Here are some more `curl` options:

- `-L` follows HTTP 3xx redirects
- `-O` names the downloaded file as it appears in the URL
- `-d arg=value` is used to submit a POST request with `Content-Type: application/x-www-form-urlencoded`
    - This flag can be specified multiple times for multiple arguments
    - This flag cannot be used with `-F`
- `--data-raw <data>` is like `-d` above, but allows you to send raw text (e.g. JSON data) without treating `@` as a special character
- `-F arg=value` is used to submit a POST request with `ContentType: multipart/form-data` (supports file uploads)
    - This flag can be specified multiple times for multiple arguments
    - To submit a file, use `-F arg=@filename`
    - This flag cannot be used with `-d`
- `-H "<header>"` is used to specify an extra HTTP request header when submitting any HTTP request
    - This flag can be specified multiple times for multiple headers
    - A file of HTTP request headers (one for each line) can be specified with `-H @<headerfilename>.txt`
    - `--proxy-header "<header>"` does the same thing, but passes the header to an HTTP proxy only
- `--dns-servers <address>` specifies a custom DNS server to use
    - This flag is not available on all versions of `curl`, but you can use [`host`](#host) to get a website's IP address according to a particular DNS server
- `-D <file>` writes the HTTP response line and headers from a GET request to a file
- `-o <file>` writes the HTTP response body from a GET request to a file
- `-I` is used to submit a HEAD request, so only the HTTP response line and headers are outputted
- `-i` outputs the HTTP response line and headers along with the HTTP response body
- `-v` outputs any TLS handshake data and the HTTP request line and headers, as well as the output from `-i`
- `-U <username:password>` specifies the username and password for a proxy
- `-u <username:password>` specifies the username and password for a server
- `-x https://proxy-address` specifies an HTTPS proxy to use to connect to a website
- `--ssl-reqd` forces TLS to be used for any protocol
    - Alternatively, you can specify a specific (secure) protocol to use with `--proto =<protocol>`
    - Example: `--proto =https` only lets `curl` use HTTPS for the request
    - A list of supported protocols is available [here](https://man7.org/linux/man-pages/man1/curl.1.html#PROTOCOLS)
        - You can also run `curl -V | grep Protocols` to locally see what protocols your version of `curl` supports
- `-c <file>.txt` outputs cookies from a website to a text file
- `-b <file>.txt` uses cookies from a text file when uploading data
- `-T <file>` uploads a file with the request
- `-X <request>` specifies a custom request
    - This is generally unnecessary for HTTP, but other protocols may necessitate it (see below)

Aside from HTTP requests, `curl` can be used with other protocols, such as FTP(S).

- FTPS is different from [SFTP](../ssh#sftp)
    - FTPS is simply [FTP](https://en.wikipedia.org/wiki/File_Transfer_Protocol) over TLS, much like HTTPS is HTTP over TLS
    - SFTP is FTP over SSH, which is its own cryptographic protocol

FTP is largely being phased out, and most web browsers no longer support `ftp://` or `ftps://` URLs. If you do need to use it though, it can be invoked in the following ways with `curl`:

- `curl --ssl-reqd ftp://<ftp-server>` allows you to view files in a FTPS server
- `curl --ssl-reqd ftp://<ftp-server>/<file> -o <file>` downloads a file from a FTPS server
- `curl --ssl-reqd ftp://<ftp-server> -T <file>` uploads a file to a FTPS server
- For all of these commands, you may need to specify a username and password with `-u <username>:<password>`
- The reason why `ftps://` isn't used is explained [here](https://everything.curl.dev/ftp/ftps#common-ftps-problems)
    - It uses port 21 to establish a connection and port 20 for data transfer
    - Yes this is weird
- You can also use the file manager on your OS to access FTP(S) servers, as well as FTP clients such as [FileZilla](https://filezilla-project.org/download.php?type=client)

Windows computers use a similar protocol to FTP called [SMB](https://en.wikipedia.org/wiki/Server_Message_Block). Like FTP, it can be used to share files, but it also supports printing services.

- Replace `ftp://` in the `curl` commands for FTPS with `smbs://` to do the same thing with a SMBS server
- Even though SMB is a Windows protocol, it can be used on Linux and macOS with [Samba](https://en.wikipedia.org/wiki/Samba_(software))

You can even send and receive email from the command line using `curl`!

To send email, use SMTPS, the secure version of [SMTP](https://en.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol):

```
curl --ssl-reqd \
    smtps://smtp.gmail.com \
    -u "sender@gmail.com:<app-password>" \
    --mail-from sender@gmail.com \
    --mail-rcpt recipient@outlook.com \
    -T email.txt
```

where `email.txt` is a file that looks like this:

```
From: Sender Name <sender@gmail.com>
To: Recipient Name <recipient@outlook.com>
Subject: This is a test

Hi Recipient,

I’m sending this email with cURL through my Gmail account.

Bye!
```

- Add more `--mail-rcpt` flags to specify more recipients, then add those recipients to the "To: " field separated by a comma followed by a space

To send an email with an attachment, there are more flags involved:

```
curl --ssl-reqd \
    smtps://smtp.gmail.com \
    -u "sender@gmail.com:<app-password>" \
    --mail-from sender@gmail.com \
    --mail-rcpt recipient@outlook.com \
    -H @headers.txt \
    -F "=(;type=multipart/mixed" \
    -F "=$(cat body.txt);type=text/plain" \
    -F "file=@attachment.pdf;type=application/pdf;encoder=base64" \
    -F "=)"
```

where `headers.txt` is a file that looks like this:

```
From: Sender Name <sender@gmail.com>
To: Recipient Name <recipient@outlook.com>
Subject: This is a test
```

and `body.txt` is a file that looks like this:

```
Hi Recipient,

I’m sending this email with cURL through my Gmail account.

Bye!
```

- You can use HTML (with CSS if desired) instead of plain text for the email body
- `type` refers to the [MIME type](https://en.wikipedia.org/wiki/Media_type#Common_examples) of the file
- You can programatically get the MIME type of an attachment with the [`file`](#file) command:
    ```
    file --mime-type <attachment> | sed "s/.*: //"
    ```

To download emails, there are a few steps involved. They use IMAPS, the secure version of [IMAP](https://en.wikipedia.org/wiki/Internet_Message_Access_Protocol).

First, to see the names of your folders:

```
curl --ssl-reqd \
    imaps://imap.gmail.com \
    -u "username@gmail.com:<app-password>"
```

For a Gmail account, this should print something like this:

```
* LIST (\HasNoChildren) "/" "INBOX"
* LIST (\HasNoChildren) "/" "Notes"
* LIST (\HasChildren \Noselect) "/" "[Gmail]"
* LIST (\All \HasNoChildren) "/" "[Gmail]/All Mail"
* LIST (\HasNoChildren) "/" "[Gmail]/Chats"
* LIST (\Drafts \HasNoChildren) "/" "[Gmail]/Drafts"
* LIST (\HasNoChildren \Important) "/" "[Gmail]/Important"
* LIST (\HasNoChildren \Sent) "/" "[Gmail]/Sent Mail"
* LIST (\HasNoChildren \Junk) "/" "[Gmail]/Spam"
* LIST (\Flagged \HasNoChildren) "/" "[Gmail]/Starred"
* LIST (\HasNoChildren \Trash) "/" "[Gmail]/Trash"
```

To find out which emails are in your inbox, run

```
curl --ssl-reqd \
    imaps://imap.gmail.com/INBOX \
    -u "username@gmail.com:<app-password>" \
    -X "UID SEARCH ALL"
```

This uses the IMAP SEARCH command and prints out `* SEARCH` followed by a potentially gigantic list of numbers.

- These numbers are UIDs, or unique IDs, associated with each of your emails
- They should be sorted (mostly) chronologically
- However, you can refine your search by replacing `UID SEARCH ALL` with one of the following IMAP commands:
    - `UID SEARCH UNSEEN` prints out UIDs for unread emails
    - `UID SEARCH BEFORE 13-Apr-2021` prints out UIDs for emails before April 13, 2021
    - `UID SEARCH ON 13-Apr-2021` prints out UIDs for emails on April 13, 2021
    - `UID SEARCH SINCE 13-Apr-2021` prints out UIDs for emails on or after April 13, 2021
    - `UID SEARCH BODY jackrabbit` prints out UIDs for emails that contain the phrase "jackrabbit" in the email body
    - `UID SEARCH SUBJECT jackrabbit` prints out UIDs for emails that contain the phrase "jackrabbit" in the email subject line
    - `UID SEARCH TEST jackrabbit` prints out UIDs for emails that contain the phrase "jackrabbit" in either the email subject line or email body
    - `UID SEARCH FROM user@gmail.com` prints out UIDs for emails that were sent from `user@gmail.com`
    - `UID SEARCH TO user@gmail.com` prints out UIDs for emails that were sent to `user@gmail.com`

When you find the UID for an email you want to download, run

```
curl --ssl-reqd \
    "imaps://imap.gmail.com/INBOX;UID=<desired-UID>" \
    -u "username@gmail.com:<app-password>" \
    -o email.txt
```

- You can also download it as `email.eml` since email clients recognize that file extension
- Downloading an email causes it to be marked as read (at least for Gmail)
- Emails are really just long text files, with file attachments encoded as [Base64](https://en.wikipedia.org/wiki/Base64)
    - To decode Base64 from a text file:
        ```
        cat base64_encoded.txt | base64 -d > decoded_file
        ```

To replicate this with an email in the "Sent Mail" folder, run

```
curl --ssl-reqd \
    "imaps://imap.gmail.com/%5BGmail%5D/Sent%20Mail;UID=<desired-UID>" \
    -u "username@gmail.com:<app-password>" \
    -o email.txt
```

- This applies URL [percent-encoding](https://en.wikipedia.org/wiki/Percent-encoding#Reserved_characters) that you have probably seen before in other URLs

Some notes about web browsers:

- Web browsers like Google Chrome, Safari, Microsoft Edge, and Mozilla Firefox use an underlying browser engine to render webpages
    - Google Chrome and Microsoft Edge use [Blink](https://en.wikipedia.org/wiki/Blink_(browser_engine))
    - Safari uses [WebKit](https://en.wikipedia.org/wiki/WebKit)
    - Mozilla Firefox uses [Gecko](https://en.wikipedia.org/wiki/Gecko_(software))
- Email clients such as the Gmail and Outlook apps on iOS and Android, Apple Mail, Windows Mail/Outlook for Windows, and Mozilla Thunderbird also use browser engines to render emails (since they can be in HTML format)
- Browser engines include an underlying JavaScript engine, used to execute JavaScript code in webpages
    - Blink uses [V8](https://en.wikipedia.org/wiki/V8_(JavaScript_engine))
        - The popular JavaScript runtime environment [Node.js](https://en.m.wikipedia.org/wiki/Node.js) also uses V8
    - WebKit uses [JavaScriptCore](https://en.wikipedia.org/wiki/WebKit#JavaScriptCore)
    - Gecko uses [SpiderMonkey](https://en.wikipedia.org/wiki/SpiderMonkey)
- Email clients, however, do not support JavaScript in emails, so they do not use the JavaScript engine
