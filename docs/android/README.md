# Android

Android phones can be emulated on a PC. This allows you to test Android apps on an Android Virtual Device (AVD), essentially an Android virtual machine, without the need for a physical Android device.

- The software needed to accomplish this can all be installed from [Android Studio](https://developer.android.com/studio), or through separate smaller packages described below
- An AVD is emulated with Android Emulator, which uses [QEMU](../qemu) in the background
    - An AVD is _virtualized_ provided there is hardware acceleration support (KVM, HVF, HAXM, etc.)
    - Details about hardware acceleration are available [here](https://developer.android.com/studio/run/emulator-acceleration#accel-vm)

## Table of Contents

- [Installation](#installation)
- [`sdkmanager` - installs Android SDK developer tools](#sdkmanager)
- [`avdmanager` - manages AVDs](#avdmanager)
- [`emulator` - runs AVDs](#emulator)
- [`adb` - interacts with devices](#adb)

## Installation

You have two choices to install Android emulation software:

- [Android Studio](https://developer.android.com/studio), which includes the Android Emulator, a GUI SDK Manager and Virtual Device Manager, and the option to install the Android SDK command-line tools, which include `sdkmanager` and `avdmanager`, respectively
- The [Android SDK command-line tools](https://developer.android.com/studio#command-line-tools-only) only

## `sdkmanager`

`sdkmanager` is used to install other dependencies needed for Android emulation.

- Install a package: `sdkmanager "<package>"` (quotes are required)
- List available packages: `sdkmanager --list`

## `avdmanager`

`avdmanager` is used to manage AVDs.

- Create an AVD: `avdmanager create avd -n <name> -k "<system-image>"` (quotes are required)
    - The system image can be obtained by running `sdkmanager --list` and will need to be installed first
    - Example system image: `"system-images;android-34;google_apis;x86_64"`
    - Make sure the architecture (x86_64 or arm64-v8a) matches your computer's CPU
- Delete an AVD: `avdmanager delete avd -n <name>`
- List AVDs: `avdmanager list avd [-c]`
    - Add `-c` to list just the AVD names

## `emulator`

`emulator` is used to run AVDs.

- `emulator` can be installed by running `sdkmanager "emulator"` or by downloading it directly from [here](https://developer.android.com/studio/emulator_archive)
- See if hardware acceleration is supported: `emulator -accel-check`
- Start up an AVD: `emulator -avd <name> [-no-audio] [-no-window] [-writable-system]`
    - Before running an AVD, make sure to run `adb start-server` first
    - `-no-audio` disables audio support
    - `-no-window` runs the AVD in headless mode
        - If you have Android Studio installed, you can use the Running Devices pane to mirror the device's screen within Android Studio
        - For remotely accessing an Android device, details are available [here](https://www.reddit.com/r/androiddev/comments/13kdma9/comment/jkjz0ej/) (your computer needs a running SSH server)
    - `-writable-system` allows you to modify everything in the `/system` folder on the AVD (normally it's read-only)
        - In order for this to work, you will need to then run `adb root` and then `adb remount`

## `adb`

Short for "Android Debug Bridge." It's used to interact with AVDs as well as physical Android devices plugged in to your computer.

- `adb` can be installed by running `sdkmanager "platform-tools"` or by downloading it directly from [here](https://developer.android.com/tools/releases/platform-tools)
- It works by running an adb server on your computer and an adb service on the device, which allows commands sent by your computer's `adb` client to be received by the device
- Start the adb server: `adb start-server`
- Stop the adb server: `adb kill-server`
- List connected devices: `adb devices [-l]`
    - Add `-l` to output more details
- Open a shell: `adb shell`

### File management

You can copy files to and from a device under certain conditions:

- For an AVD, the `-writable-system` flag by be passed in order to modify anything under `/system`
- For any device: `/data/local/tmp/` is a writable directory
- Copy files to the device: `adb push <file> <folder-on-device>`
- Copy files from the device to current directory: `adb pull <file-on-device> .`

### Package management

Android stores software packages in the form of [`.apk`](https://en.wikipedia.org/wiki/Apk_(file_format)#Package_contents) (Android Package) files, which are essentially `.zip` files containing the app's contents

- "Package managers" available for Android include Google Play, Amazon Appstore, Galaxy Store (exclusively for Samsung Galaxy devices), and alternatives such as F-Droid and APKPure
- Install APK file: `adb install <app>.apk`
- List installed packages: `adb shell pm list packages [-f]`
    - Results are listed in the form of `package:[<apk-location>=]<package-name>`
    - Add `-f` to show the file location of the `.apk` file associated with each package
- Uninstall package: `adb uninstall <package-name>`

### Port forwarding

You can set up port forwarding rules for Android devices like you can with [QEMU](../qemu#port-forwarding). This allows your computer to access services running on certain ports on the Android device.

- Forward host port 1234 to device port 5678: `adb forward tcp:1234 tcp:5678`
- List all port forwards: `adb forward --list`
