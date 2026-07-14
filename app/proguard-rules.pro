# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the SDK tools proguard-defaults.txt.

# Keep TFLite model classes to prevent UnsatisfiedLinkError during on-device inference
-keep class org.tensorflow.lite.** { *; }
-dontwarn org.tensorflow.lite.**

# Keep SQLCipher bindings for room local database encryption
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.** { *; }
-dontwarn net.sqlcipher.**

# Keep Room database schemas, entities, and compilation elements
-keep class com.seren.app.data.model.** { *; }
-keep class com.seren.app.data.dao.** { *; }
-keep class com.seren.app.data.SerenDatabase { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>(...);
}
-dontwarn androidx.room.**

