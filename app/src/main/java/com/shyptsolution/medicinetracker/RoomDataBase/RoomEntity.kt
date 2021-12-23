package com.shyptsolution.medicinetracker.RoomDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_table")
data class RoomEntity(
    val medicineName:String,
    val time:String,
    var hour:Int,
    var minute:Int,
    var dose:String,
    var stock:String,

    var monday:Boolean,
    var tuesday:Boolean,
    var wednesday:Boolean,
    var thursday:Boolean,
    var friday:Boolean,
    var saturday:Boolean,
    var sunday:Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
