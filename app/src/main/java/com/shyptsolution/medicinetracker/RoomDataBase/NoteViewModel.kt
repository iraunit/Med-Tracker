package com.shyptsolution.medicinetracker.RoomDataBase

import android.app.Application
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.shyptsolution.medicinetracker.Alarm.SaveData
import com.shyptsolution.medicinetracker.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class NoteViewModel(application: Application) :AndroidViewModel(application) {
    lateinit var allNotes: LiveData<List<RoomEntity>>
    lateinit var todayNotes: LiveData<List<RoomEntity>>
    lateinit var repository: NoteRepoditory
    var context = application.applicationContext

    init {
        val dao = DataBase(application.applicationContext).getDao()
        repository = NoteRepoditory(dao)
        allNotes = repository.allNotes
        todayNotes = getDayName()

    }

    fun deleteNote(note: RoomEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun insertNote(note: RoomEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }

    fun updatestock(stock:String, id:Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updatestock(stock,id)
    }
//    fun updateday( id:Int) = viewModelScope.launch(Dispatchers.IO) {
//        repository.updateday(id)
//    }



    fun getDayName(): LiveData<List<RoomEntity>> {
        var day = (Date().day)
//        Toast.makeText(context,"${day}  day",Toast.LENGTH_SHORT).show()
        when (day) {
            7 -> return repository.sunday
            1 -> return repository.monday
            2 -> return repository.tuesday
            3 -> return repository.wednesday
            4 -> return repository.thursday
            5 -> return repository.friday
            6 -> return repository.saturday
        }
        return repository.allNotes
    }
}



