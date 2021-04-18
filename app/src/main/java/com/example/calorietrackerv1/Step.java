package com.example.calorietrackerv1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * This class is responsible to create and manage a Step Object, which manages the number
 * of steps introduced by a User, and the time.
 */
@Entity
public class Step {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Time")
    public String time;
    @ColumnInfo(name = "number_steps")
    public int numberSteps;

    public Step(String time, int numberSteps) {
        this.time=time;
        this.numberSteps=numberSteps;
    }

    public int getId() {
        return id;
    }
    public String getTime() {
        return time;
    }
    public int getNumberSteps() {
        return numberSteps;
    }

    public void setTime(String time) {
        this.time=time;
    }
    public void setNumberSteps(int numberSteps) {
         this.numberSteps=numberSteps;
    }


}