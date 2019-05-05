package com.ieeevit.evento.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ScannedEvent.class}, version = 1, exportSchema = false)
public abstract class ScannedEventsDatabase extends RoomDatabase{

    private static final String LOG_TAG = ScannedEventsDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "scanned_event_db";
    private static ScannedEventsDatabase sInstance;

    public static ScannedEventsDatabase getInstance(Context context){
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ScannedEventsDatabase.class, ScannedEventsDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract ScannedEventDao scannedEventDao();

}
