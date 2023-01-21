# Ant

Apache Ant is a build automation tool that can be used to compile, test, and package Java projects, among other tasks.

## Structure

Ant uses a file called `build.xml` to know what tasks it can perform. It is the developer's job to tell Ant through the `build.xml` file what it can do. XML is similar to HTML, meaning there are tags, elements, and attributes (see more [here](https://www.geeksforgeeks.org/tags-vs-elements-vs-attributes-in-html/))

In a [`build.xml`](../../../../build.xml) file, there are a few elements to be aware of:

- Since it is a XML file, it should start with an element like this: `<?xml version="1.0"?>`
    - There may be more attributes to this element, such as `encoding="UTF-8"`, but they are optional
- `<project>`: This defines the name of the project
    - It is recommended to add two attributes: 
        - `default`: the default target run when Ant is executed on the command line
        - `basedir`: the starting directory for the project
    - For this project, the project element is [here](../../../../build.xml#L2)
- `<property>`: This defines what properties, or variables, are available for `build.xml`
    - You can define properties in `build.xml` using this format: `<property name="var" location="value_of_var"/>`
    - However, there could end up being many properties for the file, so instead, they can all be placed in a file such as `build.properties`
        - The property var in `build.properties` would look like: `var=value_of_var`
        - `<property file="build.properties"/>` tells Ant to use this file
        - This project uses a [`build.properties`](../../../../build.properties) file to store properties
- `<fileset>`: This is used to aggregate multiple files within a directory
    - It is used in junction with the `<include>` tag to specify which types of files to select
    - Example: select all text files within a directory named folder1
        ```
        <fileset dir="folder1">
            <include name="*.txt"/>
        </fileset>
        ```
- `<target>`: This is the most fundamental tag in a `build.xml` file
    - It defines a task that Ant can run
    - `<target name="cleanup">`: specifies a target named cleanup
    - On the command line, this would be called by running `ant cleanup`
    - It has an optional `description` attribute that allows to you define in words what that target does
    - Targets can also be told to run other comma-separated targets beforehand, using the `depends` attribute
- `<java>`: This runs the Java command
    - For our purposes, it is used to run JAR files
    - `<java jar="archive.jar" fork="true">` tells Ant to run `archive.jar`
    - It can take arguments using the `<arg>` tag:
        ```
        <java jar="archive.jar" fork="true">
            <arg line="arg1 arg2 arg3"/>
        </java>
        ```
- `<javac>`: This is used to compile Java code
    - The `<src>` tag is used to specify which directories to include files from
    - Example: compile Java code from the dir1 and dir2 directories and store the resulting `.class` files in the bin directory
        ```
        <javac destdir="bin" includeantruntime="false">
            <src path="dir1"/>
            <src path="dir2"/>
        </javac>
        ```
- `<jar>`: This is used to package a Java project into a JAR file
    - The optional `<manifest>` tag is used if you want to create a runnable JAR (rather than a library JAR, used by other Java projects)
    - Example: create a runnable JAR from the `.class` files in the bin directory (excluding JUnit test files) and start in the Driver class
        ```
        <jar destfile="archive.jar" basedir="bin" excludes="**/*Test.class">
            <manifest>
                <attribute name="Main-Class" value="${package}.Driver"/>
            </manifest>
        </jar>
        ```
- `<get>`: This is used to download files from the Internet
    - Format: `<get src="URL" dest="filename"/>`
- `<mkdir>`: This is used to create a directory
    - Format: `<mkdir dir="folder-name"/>`
- `<delete>`: This is used to delete a file or directory
    - Format: `<delete dir="folder-name"/>` or `<delete file="filename"/>`

## Ivy

Apache Ivy is a dependency manager for Ant. To use it, the Ivy jar must be installed wherever Ant wants it to be installed.

The Ivy file for this project is located [here](../../../../ivy.xml). It specifies which dependencies get installed, as well as their versions (revisions).

To use Ivy in a `build.xml` file, you must specify an XML namespace (xlmns) for it, e.g.

```
<project name="project" xmlns:ivy="antlib:org.apache.ivy.ant" default="build" basedir=".">
```

Then use `<ivy:retrieve/>` to download the dependencies (jar files) specified in `ivy.xml`.
- They will be downloaded by default to the `lib` folder of your project
- Ivy by default retrieves dependencies from the [Maven Central Repository](https://repo.maven.apache.org/maven2/), which is related to, but distinct from [Maven](https://maven.apache.org/) (a build automation tool)
    - Both the Maven Central Repository and Maven, as well as Ant and Ivy, are all maintained by the nonprofit [Apache Software Foundation](https://www.apache.org/)
