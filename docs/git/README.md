# Git

The stupid content tracker. Don't believe me? Run `man git` in your terminal.

Git is a version control system (VCS) or source control management (SCM) system, depending on who you ask. It organizes the code collaboration process via code repositories (repos).

On Linux, you might need to install `git` with a [package manager](../terminal-commands#package-managers).

## Table of Contents

- [First time usage](#first-time-usage)
- [Initializing a Git repository](#initializing-a-git-repository)
- [Committing to a repository](#committing-to-a-repository)
- [Merge requests](#merge-requests)
- [Merge conflicts](#merge-conflicts)
- [Miscellaneous](#miscellaneous)

## First time usage

Run these commands the very first time you use Git:

```
git config --global user.email "your email address"
git config --global user.name "your name"
git config --global alias.lg "log --pretty=format:'%C(yellow)%h%Creset %C(blue)%as%Creset %s%n%C(green)%an%Creset %C(red)<%ae>%Creset%n'"
git config --global advice.detachedHead false
```

For Windows:

- Run these commands as well:
    ```
    git config --global core.autocrlf false
    git config --global core.eol lf
    ```
- This ensures compatibility with Unix-like OSes, which use Unix (LF) rather than Windows (CRLF) line endings

These Git settings will be cached, so you only have to do this once.

Sometimes you may want one repository to have separate settings that differ from your global settings. For example:

```
git config --local user.email "a different email address"
git config --local commit.gpgsign false
```

### Optional: sign your commits with a GPG key:

Create the GPG key:

```
gpg --full-gen-key
```

Press enter/return, then enter 4096 for the key size, press enter/return two more times, then press y and hit enter/return to finalize the key.

You will then be asked to enter your name and email address. The email address must match the email address you used to create your GitLab account.

Then, you will be asked to enter and confirm a passphrase for your new GPG key. Once this is done, run the following command to print out the metadata for your GPG key:

```
gpg -K --keyid-format=long
```

Here is sample output:

```
/Users/hubot/.gnupg/secring.gpg
------------------------------------
sec   4096R/3AA5C34371567BD2 2016-03-10 [expires: 2017-03-10]
uid                          Hubot <hubot@example.com>
ssb   4096R/4BB6D45482678BE3 2016-03-10
```

Then copy your GPG key ID. In the above example, it is `3AA5C34371567BD2`

Run the following command to output your GPG public key (with your own GPG key ID):

```
gpg -a --export 3AA5C34371567BD2
```

Copy the output, which should start with

`-----BEGIN PGP PUBLIC KEY BLOCK-----`

and end with

`-----END PGP PUBLIC KEY BLOCK-----`

and paste it [here](https://gitlab.com/-/profile/gpg_keys) in the Key box, and add the key to your account.

Back in your terminal, run the following commands (with your GPG key ID):

```
git config --global user.signingkey 3AA5C34371567BD2
git config --global commit.gpgsign true
```

This tells Git to use your GPG key to sign all of your commits.

You will have to provide your GPG key passphrase each time you commit. To cache your passphrase, run this command:

```
echo -e "default-cache-ttl 2592000\nmax-cache-ttl 2592000" > ~/.gnupg/gpg-agent.conf
```

This will create a file that specifies the number of seconds that your GPG key passphrase is saved. In this case, 2592000 seconds = 30 days. You can specify a different value if desired.

Then run

```
gpg-connect-agent reloadagent /bye
```

If you see the message `OK`, then your passphrase has been cached. Quit your current terminal and open a new terminal, and your changes will take effect.

## Initializing a Git repository

### To download an existing repo using SSH (recommended):

```
git clone git@gitlab.com:owner/repository.git
```

### To download an existing repo using HTTPS:

```
git clone https://gitlab.com/owner/repository.git
```

or

```
git clone https://your-username:your-PAT@gitlab.com/owner/repository.git
```
- For a private repo that you have access to

### To shallow clone an existing repo using SSH (for very large repos):

```
git clone --depth 1 git@gitlab.com:owner/repository.git
```

### To initialize your current directory as a new Git repo:

Run

```
git init
```

Optional:
- Create a `.gitattributes` file to format line endings for Git
- Create a `.gitignore` file to specify files/file types to ignore
- Summarize the repo in a `README.md` file
- Copy and paste a license into `LICENSE.txt`

Create a new repo on GitLab and follow the instructions to push an existing local repo from the command line.

These instructions should be similar to the following:

```
git remote add origin <url>
git add .
git commit -m "Initial commit"
git branch -m main
git push -u origin main
```

## Committing to a repository

A branch is what is sounds like: code which "branches" from the main "trunk" of a repo.

### Add all changes:

```
git add .
```

### Optional: see which files have been changed:

```
git status
```

### Optional: see which changes have been made:

```
git diff HEAD
```

### Optional: see which branch you're currently on:

```
git branch
```

### **If you are working on a new issue:**

Fetch branches from GitLab:

```
git fetch -p
```

List all of the branches:

```
git branch -a
```

Create a new branch, but make sure to give it a name that does _not_ appear from the output of the last command:

```
git checkout -b <new-branch>
```

Commit your changes to the new branch:

```
git commit -m "your commit message"
```

Optional: revert your commit (only do this if you committed your changes by mistake):

```
git reset HEAD~1
```

Push your changes to the new branch:

```
git push -u origin <new-branch>
```

### **Otherwise:**

Commit your changes to the current branch:

```
git commit -m "your commit message"
```

Optional: revert your commit (only do this if you committed your changes by mistake):

```
git reset HEAD~1
```

Pull the most recent version of the branch:

```
git pull origin <branch>
```
- This is only necessary if someone else contributed to your branch
- If you get a merge message that pops up, just type `:q` and press enter/return

Push your changes to the branch:

```
git push
```

### Optional: see commit history of repo:

```
git lg
```

### Optional: see commit history for a particular file:

```
git lg <file>
```

## Merge requests

Once you are ready to finish an issue, submit a Merge Request (MR) on GitLab for the corresponding branch. Prefix the name of your MR with the associated Jira issue you are working on, e.g. `BP-12: Name of merge request`. In the MR description, explain the changes that were made, i.e. summarize the commits. Make sure to Squash and Merge your commits, and have GitLab delete the branch once the MR is merged in.

After the MR has been merged in, switch back to the main branch and pull the most recent version of it:

```
git checkout main
git pull
```

Then delete the merged branch:

```
git branch -D <merged-branch>
```

Congratulations! You have now contributed code with Git.

PS: on GitHub and Bitbucket, Merge Requests are known as Pull Requests (PRs), and the default branch for Git is currently called `master`.

## Merge conflicts

- This is when your branch is behind the main branch
- You will know when this happens (clicking the blue Rebase button on GitLab won't work)

**Note: [Squash](#squashing-commits-on-a-branch) all of your commits first!**

### Pull the most recent version of the main branch, then begin the rebase process:

```
git checkout main
git pull
git checkout <branch>
git rebase main
```

### To see which files are conflicting:

```
git diff --name-only --diff-filter=U
```

- If there are no conflicting files, then just force push any changes: `git push -f`

### For each conflicting file:

1. Open it in your preferred editor and pick which block of code (1) or (2) that you want to keep, **then delete the other block**:

    ```
    <<<<<<< HEAD
    # block of code from main branch (1)
    =======
    # block of code from your branch (2)
    >>>>>>> 1234abc (Commit message)
    ```

    - You can also replace the conflicting block of code with entirely new code

1. When you are done, delete the Git markup as well (the `<<<<<<< HEAD`, `=======`, and `>>>>>>> 1234abc (Commit message)` parts).

### Afterwards:

Add the files and continue the rebase process:

```
git add .
git rebase --continue
```

- You should see a message that says something like `Applying: squash commits`
- If you see a [vim](../terminal-commands#vim) screen pop up, type `:q!` and press enter/return
    - Then you should see a message that ends like `Successfully rebased and updated refs/heads/<branch>.`

Finally, force push the changes:

```
git push -f
```

## Miscellaneous

### Contents:

- [Forking a repository](#forking-a-repository)
- [Renaming a branch](#renaming-a-branch)
- [Squashing commits on a branch](#squashing-commits-on-a-branch)
- [Tagging](#tagging)
- [Renaming files in Git](#renaming-files-in-git)
- [Changing remote URL](#changing-remote-url)
- [Git settings](#git-settings)
- [Patches](#patches)

### Forking a repository

In case you see the term "fork" somewhere on GitLab or another code hosting website, [here](https://docs.github.com/en/get-started/quickstart/fork-a-repo) is some info on it.

A fork is more or less a copy of another repository with personal changes (or a complete lack of changes) added to it.

### Renaming a branch

To rename a branch (except the `main` branch, which is protected), run the following code:

```
git checkout <branch-name>
git pull origin <branch-name>
git branch -m <new-branch-name>
git push -u origin <new-branch-name>
git push -d origin <branch-name>
```

If the branch had a MR, you will need to create a new MR.

### Squashing commits on a branch

"Squashing" commits means to combine multiple commits into one.

To squash _all_ commits on a branch _tracking_ `main`, run the following code:

```
git pull origin <branch>
git reset --soft main
git commit -m "squash commits"
git push -f
```

To squash _the last n_ commits on a branch (except the `main` branch, which is protected), run the following code:

```
git pull origin <branch>
git reset --soft HEAD~n
git commit -m "squash last n commits"
git push -f
```

### Tagging

Git tagging is commonplace on popular open source projects. It allows you to mark a point in a branch's history as being important. This point in history is commonly known as a version, and the version numbers in tags should follow the versioning standards laid out [here](https://semver.org/).

Example: create a version 1.0.1 tag for the `main` branch:

```
git checkout main
git pull
git tag -a v1.0.1 -m "Version 1.0.1"
git push origin v1.0.1
```

To update and list all of the tags, run the following code:

```
git fetch -t
git tag
```

To checkout a specified version of the repository (e.g. version 1.0.1):

```
git checkout v1.0.1
```

On GitLab, tags can be associated with [Releases](https://gitlab.com/chrislattman/blogging-project/-/releases), which contain information about specific tags as well as downloadable files (i.e. the source code compiled into a `.exe` file for Windows users).

### Renaming files in Git

On systems like Windows and macOS, files are internally case-insensitive. When you rename a file by changing its case (e.g. `File.java` to `file.java`) without modifying its contents, Git does not recognize the name change. To reflect such a name change in Git, run the following code:

```
git mv <old-name> <new-name>
```

### Changing remote URL

To see which URL is used for pushing and pulling commits:

```
git remote -v
```

- If the URL starts with `git@` then the protocol used is SSH (preferred), otherwise it is HTTPS

To change the URL:

```
git remote set-url origin <new-url>
```

### Git settings

To see the Git settings for a repository:

```
git config --list
```

You can apply an option for either all repositories (global) or just one repository (local).

- To see just the global Git settings, run `git config --list --global`
- To see just the local Git settings, run `git config --list --local`

### Patches

Sometimes, it is convenient to create a "patch," or summary of changes for a repository. These are useful because they can quickly be shared with others.

To create a patch before `git add` (recommended):

```
git diff > mypatch.patch
```

To create a patch after `git add` but before `git commit`:

```
git diff --cached > mypatch.patch
```

To create a patch of an entire branch tracking `main`:

```
git diff main mybranch > mypatch.patch
```

- This also works for branches tracking other non-`main` branches

To apply a patch:

```
git apply mypatch.patch
```
