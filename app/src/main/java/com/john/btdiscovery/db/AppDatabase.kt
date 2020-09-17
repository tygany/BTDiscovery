package com.john.btdiscovery.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [MyJBLDevice::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myJBLDeviceDao(): MyJBLDeviceDao
}

