# GPG

Stands for "GNU Privacy Guard" and also referred to as "GnuPG." It is a free and open source implementation of the OpenPGP standard, which is used by proprietary software such as [PGP](https://en.wikipedia.org/wiki/Pretty_Good_Privacy).

Some background (feel free to skip to [Usage](#usage)):

Why would someone need to use GPG when there is already software like OpenSSL? OpenSSL, as demonstrated above, is great for encrypting local files, but a major problem arises when trying to share encrypted files with other people on the Internet:

- For someone else to decrypt the encrypted file, they would need to know the password (salt, key, iv)
- But how can that password be shared with them over the Internet without anyone else being able to use it?
    - One would have to encrypt the password itself, but then how does one share the password for the password?
    - This becomes a never-ending loop when only using private-key algorithms, such as AES-256 in the above example

The solution is public-key encryption. Each person has two keys:

- A public key, which can be shared with the whole world
- A private key, which must be kept secret

A sender encrypts their file with the recipient's public key, which the recipient decrypts with their private key. A cryptosystem like [RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) (which GPG uses by default) is used for this purpose.

- This may seem impossible (or magical) using intuition
- It's worth noting that it took over 2000 years to get from the [Caesar cipher](https://en.wikipedia.org/wiki/Caesar_cipher) (Julius Caesar died in 44 BC), one of the earliest forms of private-key encryption, to RSA, which was published in 1977

In practice, protocols like [HTTPS](https://en.wikipedia.org/wiki/HTTPS), which are used to encrypt Internet traffic, employ a cryptographic protocol like [TLS](https://en.wikipedia.org/wiki/Transport_Layer_Security), which utilizes both public-key and private-key encryption.

- TLS starts by using the [Diffie-Hellman key exchange](https://en.wikipedia.org/wiki/Diffie%E2%80%93Hellman_key_exchange) to securely generate a shared secret key between the client (you) and the server (the website you are visiting)
- After that, TLS encrypts all subsequent communications between the client and the server with AES-256 using the shared secret key as the password
- The reason why TLS doesn't use something like RSA for all communications is because public-key encryption is generally slower than private-key encryption

Cryptosystems like RSA also allow you to [digitally sign](https://en.wikipedia.org/wiki/Digital_signature) a message. The signer signs their message with their private key, and the recipient (who can be anyone if the message isn't encrypted) can verify that signature with the signer's public key.

- In practice, a message is first hashed using SHA-256, and then that hash is signed
- This is perfectly safe, since SHA-256 is a one-to-one function for inputs up to 2^64 - 1 bits long, or roughly 2.3 million terabytes!

#### Usage:

To create a GPG key, follow the instructions [here](../git#optional-sign-your-commits-with-a-gpg-key), but ignore all the Git/GitLab stuff.

- Technically, you are generating a keypair (a GPG public key and a GPG private key)

#### Key management:

- `gpg -k` outputs all public keys saved in your GPG keyring
    - The GPG keyring is where your GPG public and private keys are stored (kind of like the key ring for your keys)
    - This is where you can see the email address associated with a public key
- `gpg -a --export <key-id>` exports your own GPG public key in text format
    - Find your key ID by running `gpg -K --keyid-format=long`
- `gpg --import <public-key>` imports someone else's public key into your keyring
    - `<public-key>` should be a text file (sometimes ends with `.key` or `.asc`) whose contents begin with
        ```
        -----BEGIN PGP PUBLIC KEY BLOCK-----
        ```
        and end with
        ```
        -----END PGP PUBLIC KEY BLOCK-----
        ```
    - [Here](https://github.com/chrislattman/chrislattman/blob/master/public.asc) is an example of a GPG public key
- Optional: `gpg --sign-key <email-address>` signs a public key corresponding to a specified email address with your private key
    - It will get rid of any warning messages about this public key not being trusted by anyone
- `gpg --delete-key <email-address>` deletes a public key from your keyring

#### Sign without encrypting:

- Do this if you want anyone to see your message/file, but be able to verify it was you who signed that message/file
    - E.g. [signed Git commits](../git#optional-sign-your-commits-with-a-gpg-key)
- To verify someone else's signature, you need to import their public key; see [Key management](#key-management)
- Option 1: sign a text file
    - `gpg --clearsign <text-file>`
        - This stores both the contents of the text file and your signature in a file of the form `<text-file>.asc`, whose contents will begin with
            ```
            -----BEGIN PGP SIGNED MESSAGE-----
            ```
            and will end with
            ```
            -----END PGP SIGNATURE-----
            ```
    - `gpg --verify <text-file>.asc`
        - Verifies the signature using the signer's public key
        - Note: this will not work if `<text-file>.asc` is [encrypted](#sign-and-encrypt)
- Option 2: sign any type of file
    - `gpg -b <file>`
        - This stores only your signature in a file of the form `<file>.sig`
    - `gpg --verify <file>.sig <file>`
        - Verifies the signature using the signer's public key
- For either option, when verifying a signature, look for output of the form
    ```
    gpg: Good signature from "name <email-address>" [...]
    ```

#### Sign and encrypt:

- Do this if you want to send an encrypted message/file to someone, who can verify that you signed it
- As a sender, you need to import the recipient's public key; see [Key management](#key-management)
- As a recipient, you need to import the sender's public key to verify their signature
- Option 1: sign and encrypt a text file
    - `gpg -esa -r <email-address> <text-file>`
        - The email address is the recipient's email address associated with their public key
        - This stores both the contents of the text file and your signature in an encrypted file of the form `<text-file>.asc`, whose contents will begin with
            ```
            -----BEGIN PGP MESSAGE-----
            ```
            and will end with
            ```
            -----END PGP MESSAGE-----
            ```
    - `gpg <text-file>.asc`
        - Produces the decrypted file `<text-file>` and verifies the signature using the sender's public key
- Option 2: sign and encrypt any type of file
    - `gpg -es -r <email-address> <file>`
        - The email address is the recipient's email address associated with their public key
        - This stores the file and your signature in an encrypted file of the form `<file>.gpg`
    - `gpg <file>.gpg`
        - Produces the decrypted file `<file>` and verifies the signature using the sender's public key
- For either option:
    - When you encrypt anything with the recipient's public key, you won't be able to recover the original contents from the `.asc` or `.gpg` file alone
        - Don't delete the original message/file until the recipient confirms that they've received your `.asc` or `.gpg` file
    - When verifying a signature (during decryption), look for output of the form
        ```
        gpg: Good signature from "name <email-address>" [...]
        ```

#### Encrypt without signing:

- Do this if you want to send an encrypted message/file to someone without them needing to verify that you signed it
- The only difference between signing and not signing an encrypted message/file is the presence of the `-s` flag in the encryption step
    - As a sender, you still need to import the recipient's public key; see [Key management](#key-management)
    - As a recipient, however, since the message is not signed (and thus no signature is verified), you don't need to import the sender's public key
