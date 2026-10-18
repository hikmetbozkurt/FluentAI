-keepattributes *Annotation*, InnerClasses, Signature, Exceptions

-keepclassmembers @kotlinx.serialization.Serializable class com.seanora.fluentai.** {
    *** Companion;
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.seanora.fluentai.**$$serializer { *; }

-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}

-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
