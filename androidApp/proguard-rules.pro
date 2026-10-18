# Room 3 looks the generated database constructor up by type.
-keep class * implements androidx.room3.RoomDatabaseConstructor { *; }

# Firebase Messaging service is referenced from the manifest only.
-keep class com.eillia.ehya.messaging.CloudMessagingService { *; }
