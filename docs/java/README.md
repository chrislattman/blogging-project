# Java

Java is a cross-platform programming language used for many applications.

## Table of Contents

- [Development](#development)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Deployment](#deployment)
- [Build Automation Tools](#build-automation-tools)
- [Advanced](#advanced-not-recommended)

## Development

You will want to reference the `java.util` package. This documentation is freely available, and can be accessed [here](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/package-summary.html#class-summary).

- Make sure to click on the "Classes" tab
- The most commonly used data structures are:
    - Basic arrays, i.e. `int[] arr = new int[10];`
    - [`ArrayDeque`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayDeque.html)
    - [`ArrayList`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html)
    - [`HashMap`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashMap.html)
    - [`HashSet`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/HashSet.html)
    - [`LinkedList`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/LinkedList.html)
    - [`PriorityQueue`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html)
    - [`Stack`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Stack.html)
    - [`TreeMap`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeMap.html)
    - [`TreeSet`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeSet.html)
- [`Arrays`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html) and [`Collections`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html) can be used for binary search/sorting of arrays and List objects, respectively
- Use the [`Scanner`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Scanner.html) class to read data from the command line (stdin) or for file I/O
    - The classes in [`java.io`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/package-summary.html) are another option

You will also want to reference the [`java.lang`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/package-summary.html) package.

- You might find these classes to be helpful:
    - [`Math`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Math.html)
    - [`String`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/String.html)
    - [`StringBuilder`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StringBuilder.html) (used to concatenate Strings efficiently)
    - [`System`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html)
    - [`Thread`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Thread.html)

Some more helpful info:
- [Java Primitive Data Types](https://www.baeldung.com/java-primitives)
- [Overriding the `toString()` method in Java](https://www.geeksforgeeks.org/overriding-tostring-method-in-java/)
- [java.util.concurrent](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/package-summary.html) (package with useful concurrent data structures)
    - [`java.util.concurrent.locks`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/locks/package-summary.html)
    - [`java.util.concurrent.atomic`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/atomic/package-summary.html)

## Testing

You will be using the [JUnit 5](https://junit.org/junit5/) Testing Framework for Java. This comes pre-installed with Eclipse. While its user guide is very long, you only need to know a few functions.

See the [unit testing](https://en.wikipedia.org/wiki/Unit_testing) entry in Wikipedia for more information about this type of software testing.

### Test class functions

Each of your test classes have 5 example functions:

```
@BeforeAll
static void initAll() {

}

@BeforeEach
void init() {

}

@Test
void test() {
    assertTrue(true);
}

@AfterEach
void tearDown() {

}

@AfterAll
static void tearDownAll() {

}
```

- `initAll()` - this function is run only once, before _all_ of your test cases
- `tearDownAll()` - this function is run only once, after _all_ of your test cases
- `init()` - this function is run before _each_ of your test cases
- `tearDown()` - this function is run after _each_ of your test cases
- `test()` - this is an example of a test case
    - You will likely have many such test cases, each corresponding to a public function in the corresponding source file
    - E.g. `testInsertData()` or `testFile()`
- You might not need to have the `init()/initAll()` or `tearDown()/tearDownAll()` functions in your test code
    - But they are available in case you do need them

Make sure to keep the `@` tags
- These are known as [annotations](https://docs.oracle.com/javase/tutorial/java/annotations/basics.html), which Java uses extensively

### Assert functions

Within each of your test cases, you will have one or more `assert` functions, that test the behavior of your source code.

The 6 `assert` functions you'll ever need to know:

- `assertTrue(condition)`
    - asserts that a boolean condition is true, and fails otherwise
- `assertFalse(condition)`
    - asserts that a boolean condition is false, and fails otherwise
- `assertEquals(obj1, obj2)`
    - asserts that `obj1` and `obj2`, whether they are of type `int`, `long`, `double`, `boolean`, `String`, etc. are equal, and fails otherwise
- `assertNotEquals(obj1, obj2)`
    - asserts that `obj1` and `obj2`, whether they are of type `int`, `long`, `double`, `boolean`, `String`, etc. are not equal, and fails otherwise
- `assertNull(obj)`
    - asserts that `obj` is equal to `null`, and fails otherwise
- `assertNotNull(obj)`
    - asserts that `obj` is not equal to `null`, and fails otherwise

## Troubleshooting

If you are having issues running your Driver class in Eclipse, follow these steps:

- Go to Project -> Clean...
- Uncheck "Clean all projects" and check this project (`blogging-project`)
- Click Clean

Then try re-running your Driver class.

If this still doesn't work, try the following:

- Go to Project -> Properties -> Java Build Path
- Follow steps 15-17 of [Setting up Eclipse](../../docs#setting-up-eclipse)
- Click Apply and Close

The Driver class should now be able to run.

Advanced: to debug Java from the command line using `jdb` (Java Debugger):

```
javac -g -d out -cp /src/pkg/*.java YourMainClass.java
cd out
jdb YourMainClass
```

- Common `jdb` commands are described [here](https://www.geeksforgeeks.org/java-debugger-jdb/)

## Deployment

To generate JavaDoc HTML pages:

```
javadoc -d public src/pkg/*.java
```

- The homepage should be in `public/index.html`

## Build Automation Tools

A build automation tool is a program used to compile, test, and package code for a [compiled language](https://en.wikipedia.org/wiki/Compiled_language). There are three major build automation tools for Java:

- Ant
- Maven
- Gradle

[Ant](build-automation-tools/ant) and [Maven](build-automation-tools/maven) are discussed in this project. We will not be discussing Gradle, since it uses a programming language (Groovy) instead of XML. However, it is worth noting that Google chose Gradle as the build automation tool for Android Studio due to its increased efficiency.

## Advanced (not recommended)

To build and run a Java project from the command line, you have two options:

1. Run the program directly from bytecode:

    ```
    javac -d out src/pkg/*.java
    cd out
    java pkg/YourMainClassName
    ```

    - If using [Kotlin](https://en.wikipedia.org/wiki/Kotlin_(programming_language)), replace `javac` with `kotlinc`

1. Create an executable [`.jar`](https://en.wikipedia.org/wiki/JAR_(file_format)) file:

    ```
    javac -d out src/pkg/*.java
    cd out
    mkdir META-INF
    ```

    - If using Kotlin, replace `javac` with `kotlinc`
        - If you want to run the Kotlin-generated `.jar` file with `java`, add `-include-runtime` to the `kotlinc` command
    - Create a `MANIFEST.MF` file in the `META-INF` directory with the following contents (note the blank third line):

    ```
    Manifest-Version: 1.0
    Main-Class: pkg.YourMainClassName

    ```

    Then run:

    ```
    jar -cmf META-INF/MANIFEST.MF archive.jar pkg/*.class
    java -jar archive.jar
    ```

    - You can run `kotlin archive.jar` for Kotlin projects that weren't compiled with `-include-runtime`

To view the contents of a `.jar` file:

```
jar -tf archive.jar
```

To extract a `.jar` file:

```
jar -xvf archive.jar
```

To convert (decompile) Java bytecode (`.class` files) into Java code (`.java`):

```
jadx File.class
```

- You will need to install `jadx` with a [package manager](../terminal-commands#package-managers)
- You can also pass in a `.jar` file to decompile an entire Java project
- Use `-d <dir>` to specify an output directory
- An alternative decompiler is FernFlower
