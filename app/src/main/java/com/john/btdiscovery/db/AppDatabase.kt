package jbl.stc.com.database.myproducts

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyJBLDevice::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myJBLDeviceDao(): MyJBLDeviceDao
}