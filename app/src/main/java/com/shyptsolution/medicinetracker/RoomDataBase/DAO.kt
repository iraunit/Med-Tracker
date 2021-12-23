package com.shyptsolution.medicinetracker.RoomDataBase

import androidx.constraintlayout.helper.widget.Flow
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAO {
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun addNote(note:RoomEntity)

    @Query("SELECT * FROM medicine_table ORDER BY time ASC")
      fun getAllNotes(): LiveData<List<RoomEntity>>

    @Insert
    suspend  fun addMultipleNotes(vararg note:RoomEntity)

    @Update
    suspend fun updateMed(note:RoomEntity)

    @Delete
    suspend fun deleteMed(note:RoomEntity)

    @Query("UPDATE medicine_table SET stock=:stock WHERE id =:id ")
   suspend fun updatestock(stock:String, id:Int)

//    @Query("UPDATE medicine_table SET stock =:stock WHERE id =:id "  )
//    suspend fun updateday(id:Int)

//    @Query("SELECT * FROM medicine_table WHERE medicineName LIKE :searchQuery or dose LIKE :searchQuery or stock LIKE :searchQuery")
//    fun searchData(searchQuery:String): Flow<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE monday LIKE :day || '%' ")
     fun getmonday(day:Boolean=true):LiveData<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE tuesday LIKE :day || '%' ")
    fun gettuesday(day:Boolean=true):LiveData<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE wednesday LIKE :day || '%' ")
    fun getwednesday(day:Boolean=true):LiveData<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE tuesday LIKE :day || '%' ")
    fun getthurs(day:Boolean=true):LiveData<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE friday LIKE :day || '%' ")
    fun getfriday(day:Boolean=true):LiveData<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE saturday LIKE :day || '%' ")
    fun getsatur(day:Boolean=true):LiveData<List<RoomEntity>>

    @Query("SELECT * FROM medicine_table WHERE sunday LIKE :day || '%' ")
    fun getsunday(day:Boolean=true):LiveData<List<RoomEntity>>
}