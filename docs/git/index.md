# Git

The stupid content tracker. Don't believe me? Run `man git` in your terminal.

Git is a version control system (VCS) or source control management (SCM) system, depending on who you ask. It organizes the code collaboration process via code repositories (repos).

## Table of Contents

- [First time usage](#first-time-usage)
- [Initializing a Git repository](#initializing-a-git-repository)
- [Committing to a repository](#committing-to-a-repository)
- [Merge conflicts](#merge-conflicts)
- [Pull requests](#pull-requests)
- [Miscellaneous](#miscellaneous)

## First time usage

Run these commands the very first time you use Git:

```
git config --global user.email "your email address"
git config --global user.name "your name"
```

For Windows:

- Run these commands as well:
    ```
    git config --global core.autocrlf false
    git config --global core.eol lf
    ```
- This ensures compatibility with Unix-like OSes, which use Unix (LF) rather than Windows (CRLF) line endings

These Git settings will be cached, so you only have to do this once.

## Initializing a Git repository

### To download an existing repo using SSH (recommended):

```
git clone git@github.com:owner/repository.git
```

### To download an existing repo using HTTPS:

```
git clone https://github.com/owner/repository.git
```

or

```
git clone https://your-username:your-PAT@github.com/owner/repository.git
```
- For a private repo you don’t own, but have access to

### To initialize your current folder as a new git repo:

Run

```
git init
```

Optional:
- Create a `.gitignore` file to specify files/file types to ignore
- Summarize the repo in a `README.md` file
- Copy and paste a license into `LICENSE`

Create a new repo on GitLab and follow the instructions to push an existing local repo from the command line.

These instructions should be similar to the following:

```
git remote add origin <URL>
git add .
git commit -m "Initial commit"
git branch -m main
git push -u origin main
```

## Committing to a repository

A branch is what is sounds like: code which "stems" from the main "trunk" of a repo.

### Add all changes:

```
git add .
```

### Optional: see which files have been changed:

```
git status
```

### Optional: see which branch you're currently on:

```
git branch
```

### **If you are working on a new issue:**

Fetch branches from GitHub:

```
git fetch -p
```

List all of the branches:

```
git branch -a
```

Create a new branch, but make sure to give it a name that does _not_ appear from the output of the last command:

```
git checkout -b <new_branch>
```

Commit your changes to the new branch:

```
git commit -m "your commit message"
```

Push your changes to the new branch:

```
git push -u origin <new_branch>
```

### **Otherwise:**

Commit your changes to the current branch:

```
git commit -m "your commit message"
```

Pull the most recent version of the branch:

```
git pull
```
- If you get a merge message that pops up, just type `:q` and press enter/return

Push your changes to the branch:

```
git push
```

## Merge conflicts

- This is when your branch is behind the main branch
- You will know when this happens
- This can be a very, _very_ annoying process if you don't [squash](#squashing-commits-on-a-branch) your commits from time to time

### Pull the most recent version of the main branch and update it, then begin the rebase process:

```
git checkout main
git pull
git checkout <branch>
git rebase main
```

### For each conflicting file:

1. Open it in your preferred editor and pick which block of code (1) or (2) that you want to keep, **then delete the other block**:

    ```
    <<<<<<< HEAD
    # block of code from main branch (1)
    =======
    # block of code from your branch (2)
    >>>>>>> 1a2b3c4 (Commit message)
    ```

1. When you are done, delete the Git markup as well (the `<<<<<<< HEAD`, `=======`, and `>>>>>>> 1a2b3c4 (Commit message)` parts).

1. Add the file and continue rebasing:

    ```
    git add .
    git rebase --continue
    ```

    One of two things will happen:

    - This will show a commit message, which should then be saved (type `:q` and press enter/return in the terminal)
    - You will see a message that includes the following:

        ```
        No changes - did you forget to use 'git add'?
        If there is nothing left to stage, chances are that something else
        already introduced the same changes; you might want to skip this patch.
        ```

        - In this case, skip the current rebase step:

            ```
            git rebase --skip
            ```

1. Once all conflicts are resolved, you should see a message that says either

    `Successfully rebased and updated refs/heads/<branch>.`

    or

    `No changes -- Patch already applied.`

    Otherwise, go back to step 1 and repeat this procedure until all conflicts are resolved. 

Finally, force push the changes:

```
git push -f
```

## Pull requests

Once you are ready to finish an issue, submit a Pull Request (PR) on GitLab for the corresponding branch. Prefix the name of your PR with the associated Jira issue you are working on, e.g. `BP-12: Name of pull request`. In the PR description, explain the changes that were made, i.e. summarize the commits. Make sure to Squash and Merge your commits, and have GitHub delete the branch once the PR is merged in.

After the PR has been merged in, switch back to the main branch and pull the most recent version of it:

```
git checkout main
git pull
```

Then delete the merged branch:

```
git branch -D <merged_branch>
```

Congratulations! You have now contributed code with Git.

## Miscellaneous

### Forking a repository

In case you see the term "fork" somewhere on GitHub or another code hosting website, [here](https://docs.github.com/en/get-started/quickstart/fork-a-repo) is some info on it.

A fork is more or less a copy of another repository with personal changes (or a complete lack of changes) added to it.

### Renaming a branch

To rename a branch (except the `main` branch, which is protected), run the following code:

```
git checkout <branch-name>
git branch -m <new-branch-name>
git push -u origin <new-branch-name>
git push -d origin <branch-name>
```

If the branch had a PR, you will need to create a new PR.

### Squashing commits on a branch

"Squashing" commits means to combine multiple commits into one.

To squash the last _n_ commits on a branch (except the `main` branch, which is protected), run the following code:

```
git reset --soft HEAD~n
git commit -m "your commit message"
git push -f
```

To squash all commits on a branch _tracking_ `main`, run the following code:

```
git reset --soft main
git commit -m "your commit message"
git push -f
```

### Tagging

Git tagging is commonplace on popular open source projects. It allows you to mark a point in a branch's history as being important. This point in history is commonly known as a version, and the version numbers in tags should follow the versioning standards laid out [here](https://semver.org/).

Example: create a version 1.0.1 tag for the `main` branch:

```
git checkout main
git tag -a v1.0.1 -m "Version 1.0.1"
git push origin v1.0.1
```

To update and list all of the tags, run the following code:

```
git fetch -t
git tag
```

On GitHub, tags can be associated with [Releases](https://github.com/chrislattman/blogging-project/releases), which contain information about specific tags as well as downloadable files (i.e. the source code compiled into a `.exe` file for Windows users).
