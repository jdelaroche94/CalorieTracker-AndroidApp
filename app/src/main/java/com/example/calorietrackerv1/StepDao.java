package com.example.calorietrackerv1;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface StepDao {
    @Query("SELECT * FROM step")
    List<Step> getAll();
    @Query("SELECT * FROM step WHERE id = :stepId LIMIT 1")
    Step findByID(int stepId);
    @Insert
    void insertAll(Step... steps);
    @Insert
    long insert(Step step);
    @Update(onConflict = REPLACE)
    void updateStep(Step... step);
    @Delete
    void delete(Step step);
    @Query("DELETE FROM step")
    void deleteAll();
    //@Query("UPDATE step SET  WHERE NAME=Step");
}
