package com.masaibar.roomflowablesample

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface TimeDao {

    @Query("SELECT * FROM time")
    fun selectAll(): Flowable<List<Time>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(time: Time): Long

    @Query("DELETE FROM time")
    fun deleteAll()

}