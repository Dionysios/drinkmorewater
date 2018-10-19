package com.dionpapas.drinkyourwater.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface WaterEntryDAO {
    @Query("SELECT * FROM DailyWaterEntries ORDER BY updated_at")
    LiveData<List<WaterEntry>> getAllWaterEntries();

    @Insert
    void insertWaterEntry(WaterEntry waterEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWaterEntry(WaterEntry waterEntry);

    @Delete
    void deleteWaterEntry(WaterEntry waterEntry);
}
