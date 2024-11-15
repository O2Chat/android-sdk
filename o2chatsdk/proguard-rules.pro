# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep all classes and methods in com.example.o2chatsdk package for consumer applications
#-keep class com.example.o2chatsdk.** { *; }

# Keep all interfaces in com.example.o2chatsdk package
#-keep class com.example.o2chatsdk.** { *; }

#-keep class com.example.o2chatsdk.commons.Common** { *; }

#-keep class com.example.o2chatsdk.commons.Common{  <methods>; <fields>;}

#-keep class com.example.o2chatsdk.retrofit.** { *; }
#-keep class com.example.o2chatsdk.Events.**{*;}
#-keep class com.example.o2chatsdk.activities.MainActivityChat
##-keep class com.example.o2chatsdk.O2ChatConfig
##-keep class com.example.o2chatsdk.localDB.**{*;}
##-keep interface com.example.o2chatsdk.** { *; }
#-keep class com.example.o2chatsdk.model.** { *; }
#-keep class net.sqlcipher.** { *; }
#-dontwarn net.sqlcipher.**

#-keep class com.example.o2chatsdk.** { *; }

-keep class com.example.o2chatsdk.** { *; }
-keep interface com.example.o2chatsdk.** { *; }
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**
