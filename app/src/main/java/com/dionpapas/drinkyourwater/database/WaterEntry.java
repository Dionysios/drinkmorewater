package com.dionpapas.drinkyourwater.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "DailyWaterEntries")
public class WaterEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int counter;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;


    public WaterEntry(int id, int counter, Date updatedAt) {
        this.id = id;
        this.counter = counter;
        this.updatedAt = updatedAt;
    }

    @Ignore
    public WaterEntry(int counter, Date updatedAt) {
        this.counter = counter;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
