## O2Chat Android SDK

## First time integration 
### [Integration Guide](https://support.o2chat.com/support/solutions/articles/50000000207)

#### Quick Steps
Project gradle file **build.gradle**
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

App Module gradle file **(app/build.gradle)** 
```
dependencies {
	implementation 'com.github.O2Chat:android-sdk:3cc9701424'
}
```

## Updating to newer versions of SDK
### [Changelog](https://github.com/02chat/android-sdk/blob/master/CHANGELOG.md)


## Contact us
For any issues, queries or feature request, please reach out to us through support@02chat.com
