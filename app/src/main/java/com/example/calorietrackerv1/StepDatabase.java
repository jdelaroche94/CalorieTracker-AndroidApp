package com.example.calorietrackerv1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import android.content.Context;

@Database(entities = {Step.class}, version = 4, exportSchema = false)
public abstract class StepDatabase extends RoomDatabase {
    public abstract StepDao stepDao();
    private static volatile StepDatabase INSTANCE;
    static StepDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StepDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    StepDatabase.class, "steps_database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}