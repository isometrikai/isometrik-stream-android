# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

-keep class com.cloudinary.android.*Strategy

-dontwarn  org.conscrypt.ConscryptHostnameVerifier
-dontwarn  com.google.android.gms.gcm.GcmTaskService
-keep,allowoptimization,allowobfuscation public class io.isometrik.gs.** {*;}
-flattenpackagehierarchy io.isometrik.groupstreaming.ui

-keepclassmembernames class io.netty.** { *; }
-keepclassmembers class org.jctools.** { *; }