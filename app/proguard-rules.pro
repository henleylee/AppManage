#设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
#混淆时不会产生形形色色的类名
-dontusemixedcaseclassnames
#指定不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#不预校验
-dontpreverify
#混淆后生产映射文件 map 类名->转化后类名的映射
-verbose
#混淆采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
# 引入libs下的所有jar包，与android build.xml保持一致
-libraryjars libs(*.jar;)

#所有的系统组件不要混淆，保持其原类名和包名
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
#Anroid官方建议不混淆的类
-keep public class com.android.vending.licensing.ILicensingService
-keep public class android.app.backup.BackupAgentHelper
-keep public class android.preference.Preference

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
    public void *(android.view.View);
}

#aidl文件不进行混淆
-keep public class android.content.pm.IPackageStatsObserver { *; }

#对所有类的native方法名不进行混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#对所有类的指定方法的方法名不进行混淆
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
#对枚举类型enum的所有类的以下指定方法的方法名不进行混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#对实现了Parcelable接口的所有类的类名不进行混淆，对其成员变量为Parcelable$Creator类型的成员变量的变量名不进行混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#对实现了Serializable接口的所有类的类名不进行混淆
-keepnames class * implements java.io.Serializable

-keep class **.R$* { *; }

-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes Deprecated,SourceFile,LineNumberTable,EnclosingMethod

-dontshrink
-dontoptimize
