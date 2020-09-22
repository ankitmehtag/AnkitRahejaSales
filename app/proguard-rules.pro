# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\NareshGupta\AndroidSDKADTBundle64Bit\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-libraryjars "C:/Program Files/Java/jre1.8.0_111/lib/rt.jar"
#-libraryjars "C:/Program Files/Java/jre1.8.0_131/lib/rt.jar"
#-libraryjars "C:/Users/Intel/AppData/Local/Android/sdk/extras/android/m2repository/com/android/support/support-v4/23.1.0/support-v4-23.1.0-sources.jar"

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep public class com.squareup.** { public *; }
-dontwarn com.squareup.**
-keep public class javax.jms.** { public *; }
-dontwarn javax.jms.**
-keep public class com.sun.jdmk.comm.** { public *; }
-dontwarn com.sun.jdmk.comm.**
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep public class com.google.android.m4b.* { public *; }
-dontwarn com.google.android.m4b.**
-keep public class android.net.http.** { public *; }
-dontwarn com.koushikdutta.async.**
-dontnote android.net.http.**
-keep public class org.apache.** { public *; }
-dontnote org.apache.**
-keep class * extends org.ietf.jgss.** { *; }
-dontwarn org.ietf.jgss.**
-keep class * extends javax.naming.** { *; }
-dontwarn javax.naming.**
-keep class * extends org.apache.** { *; }
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-dontwarn org.apache.lang.**
-keep public class com.google.gson


-dontwarn com.filippudak.ProgressPieView.**

#-dontwarn com.octo.android.robospice.SpiceService

-dontwarn android.support.v4.**
-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-dontwarn com.mixpanel.**
## --------- Projects files ---------- ##

-keep class com.bookmyhouse.** { *; }
-keepclassmembers class com.bookmyhouse.** { *; }

-keep class com.adapters.** { *; }
-keepclassmembers class com.adapters.** { *; }

-keep class com.AppEnums.** { *; }
-keepclassmembers class com.AppEnums.** { *; }

-keep class com.appnetwork.** { *; }
-keepclassmembers class com.appnetwork.** { *; }

-keep class com.exception.** { *; }
-keepclassmembers class com.exception.** { *; }

-keep class com.filter.** { *; }
-keepclassmembers class com.filter.** { *; }

-keep class com.fragments.** { *; }
-keepclassmembers class com.fragments.** { *; }

-keep class com.galleryview.** { *; }
-keepclassmembers class com.galleryview.** { *; }

-keep class com.helper.** { *; }
-keepclassmembers class com.helper.** { *; }

-keep class com.interfaces.** { *; }
-keepclassmembers class com.interfaces.** { *; }

-keep class com.jsonparser.** { *; }
-keepclassmembers class com.jsonparser.** { *; }

-keep class com.model.** { *; }
-keepclassmembers class com.model.** { *; }

-keep class com.pwn.** { *; }
-keepclassmembers class com.pwn.** { *; }

-keep class com.sitevisit.** { *; }
-keepclassmembers class com.sitevisit.** { *; }

-keep class com.slidingpanel.** { *; }
-keepclassmembers class com.slidingpanel.** { *; }

-keep class com.utils.** { *; }
-keepclassmembers class com.utils.** { *; }

-keep class com.views.** { *; }
-keepclassmembers class com.views.** { *; }

-keep class com.VO.** { *; }
-keepclassmembers class com.VO.** { *; }
 -ignorewarnings
