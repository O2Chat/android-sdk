package com.arittek.o2chatsdk.localDB;

import androidx.room.TypeConverter;

import java.util.UUID;

public class EmUUIDConverter {
    @TypeConverter
    public static String fromUUID(UUID uuid) {
        return uuid.toString();
    }

    @TypeConverter
    public static UUID uuidFromString(String string) {
        return UUID.fromString(string);
    }
}
