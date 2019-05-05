package com.ieeevit.evento.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ScannedEventDao {

    @Query("SELECT * FROM scanned_events")
    List<ScannedEvent> getAllEvent();

    @Insert
    void insertEvent(ScannedEvent scannedEvent);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEvent(ScannedEvent scannedEvent);

    @Delete
    void deleteEvent(ScannedEvent scannedEvent);

}
