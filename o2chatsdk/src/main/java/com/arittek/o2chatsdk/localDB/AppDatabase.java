package com.arittek.o2chatsdk.localDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.arittek.o2chatsdk.commons.DateConverter;
import com.arittek.o2chatsdk.localDB.entity.ConversationDetail;
import com.arittek.o2chatsdk.localDB.entity.ConversationDetailFile;
import com.arittek.o2chatsdk.localDB.entity.NewChatEntity;

@Database(entities = {ConversationDetail.class,ConversationDetailFile.class, NewChatEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "02chatbackup_db";

    public abstract ConversationDetailDao conversationDetailDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            // Initialize SQLCipher with your encryption passphrase
          //  SQLiteDatabase.loadLibs(context.getApplicationContext());
         /*   String passphrase = "CMitXwKoZFw6PrK";
            byte[] key = SQLiteDatabase.getBytes(passphrase.toCharArray());
            // Create a SupportFactory with the encryption key
            SupportFactory factory = new SupportFactory(key);*/
            // Initialize Room with the encrypted factory

            System.loadLibrary("sqlcipher");
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                            .build();


            // allow queries on the main thread.
            // Don't do this on a real app! See PersistenceBasicSample for an example.
//                            .allowMainThreadQueries().openHelperFactory(factory)
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}