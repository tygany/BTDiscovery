package com.john.btdiscovery.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.john.btdiscovery.MyApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object MyJblDevicesMgr {
    private const val DB_NAME="myJblDevices_db2"

    private fun getDb(): AppDatabase {
        return Room.databaseBuilder(
            MyApplication.getContext(), AppDatabase::class.java,DB_NAME
        ).build()
    }

//    private val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("DROP TABLE MyJBLDevice")
//            database.execSQL("""
//                CREATE TABLE MyJBLDevice (
//                    id INTEGER PRIMARY KEY NOT NULL,
//                    btMac TEXT,
//                    connectStatus INTEGER,
//                    colorId INTEGER,
//                    deviceName TEXT
//                )
//                """.trimIndent())
//        }
//    }

    fun getAll(): List<MyJBLDevice> {
        return getDb().myJBLDeviceDao().getAll()
    }


    fun getJblDeviceByBtMac(btMacs: String): MyJBLDevice? {
        return getDb().myJBLDeviceDao().getJblDeviceByBtMac(btMacs)
    }

    fun insertAll(myJBLDevices: MyJBLDevice) {
        GlobalScope.launch {
            getDb().myJBLDeviceDao().insertAll(myJBLDevices)
        }
    }

    fun update(myJBLDevice: MyJBLDevice) {
        GlobalScope.launch {
            getDb().myJBLDeviceDao().update(myJBLDevice)
        }
    }

    fun delete(myJBLDevice: MyJBLDevice) {

        GlobalScope.launch {
            getDb().myJBLDeviceDao().delete(myJBLDevice)
        }
    }

    fun closeDb() {
        GlobalScope.launch {
            getDb().close()
        }
    }
}