package com.turtle.qrviewer.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.Flow

class QRViewModelViewmodel(appObj: Application) : AndroidViewModel(appObj) {

    private val db =  Room.databaseBuilder(
        appObj,
        AppDatabase::class.java, "qrgen-database"
    ).build()

    suspend  fun getDefault(): QREntry {
        var entry = db.qrdoa().findByName("default")
        if (entry == null) {
            entry = QREntry("default", "")
            db.qrdoa().insert(entry)
        }
        return entry
    }

    suspend  fun update(entry: QREntry) {
        val existing = db.qrdoa().findByName(entry.name)
        if (existing == null) {
            db.qrdoa().insert(entry)
        }
        else {
            existing.data = entry.data
            db.qrdoa().update(existing)
        }
    }
}