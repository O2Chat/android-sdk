## O2Chat Android SDK

[![](https://jitpack.io/v/O2Chat/android-sdk.svg)](https://jitpack.io/#O2Chat/android-sdk)

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
	implementation 'com.github.O2Chat:android-sdk:1.0.7'
}
```

## Step 2: Sync Project

After adding the dependency, click on "Sync Now" in the notification bar to sync your project with the updated Gradle files.

## Step 3: Initialize and Use the Library

To use the library in your project, follow these steps:

1. **Create an instance of `O2ChatConfig`:**

   ```java
   O2ChatConfig o2chatConfig = new O2ChatConfig();
   ```

2. **Save Channel ID and User Information:**

   Use the following methods to save user details:

   ```java
   o2chatConfig.saveChannelID(context, "example-5b2e-4227-a456-eab45924a1d3");
   o2chatConfig.saveFirstName(context, "First Name");
   o2chatConfig.saveCustomerEmail(context, "Email Address");
   o2chatConfig.saveCustomerMobileNumber(context, "Phone Number");
   o2chatConfig.saveCnicNumber(context, "Cnic");
   o2chatConfig.setTitleBarColor(context,"#bb1823");
   o2chatConfig.setTitleTextColor(context,"#ffffff");
   ```

## Step 4: Start Chat Activity 

   Use the following method to start chat`.

  ```java
        startActivity(new Intent(this, MainActivityChat.class));
   ```


## Updating to newer versions of SDK
### [Changelog](https://github.com/02chat/android-sdk/blob/master/CHANGELOG.md)


## Contact us
For any issues, queries or feature request, please reach out to us through support@02chat.com
