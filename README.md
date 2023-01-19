# Blogging Project

## Table of Contents

- [Getting Started](#getting-started)
- [Project Overview](#project-overview)
- [Submission Procedures](#submission-procedures)
- [Miscellaneous](#miscellaneous)

## Getting Started

Make sure your GitLab account has a strong password and [2FA](https://docs.gitlab.com/ee/user/profile/account/two_factor_authentication.html#enable-one-time-password) enabled. Also make sure that your Atlassian account has a strong password and [2FA](https://support.atlassian.com/atlassian-account/docs/manage-two-step-verification-for-your-atlassian-account/#Twostepverification-Useaverificationapponyoursmartphone) enabled.

Then read the [docs](docs).

## Project Overview

You will be creating a simple [CLI](https://en.wikipedia.org/wiki/Command-line_interface) blogging application. CLI means "command line interface," so there is no front-end GUI. All interaction with the application will be through a terminal.

This is not how a modern day social network would operate (old platforms like [IRC](https://en.wikipedia.org/wiki/Internet_Relay_Chat) still exist), but it makes our job easier, and allows us to focus on core Java development.

It also doesn't have any messaging capability! In an ideal world, a social network owner wouldn't be able to read private messages anyways. Oh well.

### Description of source files

- [`Driver.java`](src/app/Driver.java) will be the entrypoint of the blogging application
    - It should not be modified
- [`Application.java`](src/app/Application.java) should be the backbone of the application
    - It should take several commands: add user, view blogs, etc. (explained below)
- [`Blog.java`](src/app/Blog.java) should be the template for an individual blog on the application
    - It should have a fields for an author of type `User`, a `String` blogname, as well as an `ArrayList<User>` of members
- [`User.java`](src/app/User.java) should be the template for a blog user
    - It should have `String` fields for username, first name, and last name

### Project Requirements

Your application should support the following commands:

- `ALL` - outputs all users in the format (username, first name, last name) and all blogs in the format (blogname, username of author)
- `ADD USER <username> <first-name> <last-name>` - adds a new user to the application
- `ADD BLOG <blogname> <author-username>` - adds a new blog to the application, and specifies its author by username
- `ADD USERTOBLOG <username> <blogname>` - adds a user as a member to a blog
- `CANCEL` - reverts the last `ADD` command
- `CHANGE USERNAME <old-username> <new-username>` - changes the username of a user
- `CHANGE BLOGNAME <old-blogname> <new-blogname>` - changes the blogname of a blog
- `CLEANUP` - removes any users who are neither a blog author nor a member of a blog
- `RESET` - removes all users and blogs from the application (resets the application)
- `VIEW <username>` - outputs the first name and last name of a user
- `VIEW <blogname>` - outputs the username of the blog's author and the usernames of its members
- `SEARCH <input>` - searches the application for any users or blogs that match the input
    - output any matches in the format (username, first name, last name) for users and/or  (blogname, username of author) for blogs
- `HELP` - outputs a help menu that briefly explains these commands

I've left some starter code in `Application.java` for you to reference.

Your application should adhere to the following guidelines:

- Commands should be case-insensitive
- Store your users and blogs in data structures
- `Blog.java` and `User.java` should have [getter and setter methods](https://www.geeksforgeeks.org/getter-and-setter-in-java/) for each of their fields
- There should be no duplicate users (by username) or blogs (by blogname)
- There should be no blog with a nonexistent author
- There should be no blog with its author as a member
    - A blog's author is distinct from its list of members
- Any command line input (including non-commands) should be handled without crashing the application
- Any new `.java` files in `src/app` should have an associated test file in `test/app`
- No `.java` files should be renamed, and the file structure of the project should be left unchanged
- `Driver.java` should not be modified, and there should be no JUnit test class for `Driver.java` (e.g. `DriverTest.java`)

Notes on Java are [here](docs/java).

## Submission Procedures

You will submit your code using [`git`](docs/git). Make sure to get acquainted with the commands; there aren't too many but they will help you a lot with this project and out in the real world.

You will notice that the Git page mentions "issues." Reason: this project cannot be completed in one night. It is best to split up your work into small, manageable chunks. These chunks may be individual commands, new test cases, bug fixes, or even new features you're working on. Jira calls these chunks "issues" (_not_ the same as [GitLab issues](https://gitlab.com/chrislattman/blogging-project/-/issues)). We will be using Jira for issue tracking.

For each issue:

- Make sure to [create an issue](https://chrislattman.atlassian.net/jira/software/projects/BP/boards/2) on Jira, briefly describing what you plan to submit
    - This should be in the "To Do" bin
    - Follow the Git instructions [here](docs/git#committing-to-a-repository) for new issues
- If you are currently working on an issue, then simply click and drag it into the "In Progress" bin
- When you are ready to submit an issue, create a [Merge Request](docs/git#merge-requests) for your Git branch
- Once the Merge Request has been merged in for an associated Jira issue, click and drag it into the "Done" bin

Using Jira might sound pointless for a project this small, but in larger projects with more people, it is a necessity. Jira also allows you to create as many issues as you want, so you can plan far into the future (this is what businesses do).

## Miscellaneous

Some info about the files in this folder:

- [`.gitattributes`](.gitattributes) is used to format line endings for Git commits

- [`.gitignore`](.gitignore) has the names of files, folders, and file extensions to exclude from this repository
    - It is meant to keep build files and other "junk" on your computer without clogging up this repository

- [`.gitlab-ci.yml`](.gitlab-ci.yml) has the GitLab CI/CD configurations, a.k.a. the "pipeline" used to build and test your code

- [`LICENSE.txt`](LICENSE.txt) has the open source license for this project (GNU GPLv3)
    - It's optional, but good to have nonetheless

- [`README.md`](README.md) is this file

- [`build.properties`](build.properties) stores variables used in `build.xml`

- [`build.xml`](build.xml) is the build configuration file used by [Ant](docs/java/build-automation-tools/ant)

- [`ivy.xml`](ivy.xml) is used by `build.xml` to download dependencies needed for the project
