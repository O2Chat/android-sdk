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

-keep class com.arittek.o2chatsdk.commons.O2ChatConfig.** { *; }
-keep class com.arittek.o2chatsdk.commons.O2ChatConfig{  <methods>;
                                                           <fields>;
                                                           }
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**
-keep class com.arittek.o2chatsdk.Events.**{*;}
-keep class com.arittek.o2chatsdk.activities.MainActivityChat
-keep class com.arittek.o2chatsdk.localDB.**{*;}
-keep class com.arittek.o2chatsdk.retrofit.** { *; }
-keep class com.arittek.o2chatsdk.model.** { *; }

-keep class com.microsoft.signalr.** { *; }
-keep interface com.microsoft.signalr.** { *; }

-keep class com.karumi.dexter.** { *; }
-keep interface com.karumi.dexter.** { *; }
-keepclasseswithmembernames class com.karumi.dexter.** { *; }
-keepclasseswithmembernames interface com.karumi.dexter.** { *; }









#download library

# Keep all classes and members in the `com.download.library` package


-keepclasseswithmembernames class com.download.library.** { *; }
-keepclasseswithmembernames interface com.download.library.** { *; }
-keep class com.downloader.** {*;}
-keep class com.download.library.** { *; }
-keep interface com.download.library.** { *; }
-keepclasseswithmembers class com.download.library.** {
    *;
}
-keep @interface com.download.library.**
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
-keepclassmembers enum com.download.library.** {
    *;
}
-keepnames class com.download.library.**

# Keep dependencies (if the library uses third-party code)
-keep class okio.** { *; }
-keep class okhttp3.** { *; }
-keep class com.google.gson.** { *; }

# Room database rules
-keep class androidx.room.RoomDatabase { *; }
-keep class androidx.room.** { *; }
-keepattributes Signature
-keepattributes *Annotation*