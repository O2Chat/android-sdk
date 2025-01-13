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

-keep class com.arittek.o2chatsdk.** { *; }
-keep interface com.arittek.o2chatsdk.** { *; }
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

-keep class com.download.library.** { *; }
-keep interface com.download.library.** { *; }
-keepclasseswithmembernames class com.download.library.** { *; }
-keepclasseswithmembernames interface com.download.library.** { *; }




-keep class com.downloader.** {*;}


#download library

# Keep all classes and members in the `com.download.library` package
-keep class com.download.library.** { *; }

# Keep all interfaces in the `com.download.library` package
-keep interface com.download.library.** { *; }

# Prevent ProGuard from removing any classes with specific member names
-keepclasseswithmembers class com.download.library.** {
    *;
}

# Preserve annotations used by the library (if any)
-keep @interface com.download.library.**

# Preserve all generic signatures for the package
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

# Prevent the stripping of enums
-keepclassmembers enum com.download.library.** {
    *;
}

# Preserve any classes that are dynamically loaded via reflection
-keepnames class com.download.library.**

# Keep dependencies (if the library uses third-party code)
-keep class okio.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }