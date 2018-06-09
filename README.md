# Spoof My Device [Xposed]

## Play Store Link
https://play.google.com/store/apps/details?id=nz.pbomb.xposed.spoofmydevice

## Why does this have low ratings?
This application, like any other root/xposed application, requires a certain level of technical expertise to use to avoid risk towards your device. 

A lot of people get their devices bricked or bootlooped by my application because they try to spoof all applications including system applications (which is not recommended!). This will make the device a bit confused when booting and hence not boot. My application tries to block users from spoofing these system applications, but due to the variation of ROMs available, each ROM has different system applications, making it impossible for me to block all system applications that exist in the Android ecosystem. 


## Status 
### Inactive
This project is inactive for a very long time as I no longer have a rooted device to continue development.

Additionally, I don't think there is need for such an application. Earlier in the Android ecosystem, some app developers would block specific brands from running their applications or have 'Samsung-only' apps which this app would help to bypass. Nowadays, its very unlikely to need such as app developers rarely do this.

Secondly, this application has a few bugs. Some are GUI bugs and can easily be fixed, others are a little trickier. I recommend only using this code as a reference if you are thinking about making a similar application. Using this code as a base for future projects is not recommended as the code is not tested well.

## Description
NOTE: You must have the Xposed Framework installed on your device in order for this application to provide any functionality.

This application allows Xposed users to spoof their device to a much more modern device to bypass restrictions in numerous applications that contain in-app blacklists and whitelists. This can also be handy when developing apps in an emulator where real information is needed.

Although this application is released for production is still very new therefore expect more features to come out soon. I have some great ideas in my head that can be used to differentiate from other similar apps. Also I actually try to fix bugs and read reviews, so if you have any issues feel free to contact me.

Features

→ Manual Entry Device Spoofing - Allows users to manually enter their own device properties if they feel comfortable doing so or want to be spoofed as a specific device.

→ Included Device Templates - Allows users to also pick various device templates, including the Samsung Galaxy Note 3, Samsung Galaxy S7 Edge and Huawei P8 and some others to be used as the spoofing device. (Please note, that more devices are being added in future).

→ Specify Applications to Spoof - Unlike other spoofing applications, Spoof My Device only spoofs the applications which users want to be spoofed and leaves all other applications alone. This integration allows Spoof My Device to be safer as it cannot crash system applications that rely on the actual device's properties.
