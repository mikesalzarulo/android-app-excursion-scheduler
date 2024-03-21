package com.example.msalzar_d308_mobile_application_development_android.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.msalzar_d308_mobile_application_development_android.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict= OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationID = :vacationID")
    Vacation getVacation(int vacationID);

}
