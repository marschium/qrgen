package com.turtle.qrviewer.model

import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Entity
data class QREntry (
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "data") var data: String
)

@Dao
interface QREntryDao {

    @Query("SELECT * FROM qrentry WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): QREntry?

    @Insert
    suspend fun insert(vararg entry: QREntry)

    @Update
    suspend fun update(entry: QREntry)

    @Delete
    suspend fun delete(entry: QREntry)
}

@Database(entities = [QREntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun qrdoa(): QREntryDao
}