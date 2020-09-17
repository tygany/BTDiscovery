package jbl.stc.com.database.myproducts

import androidx.room.Room
import jbl.stc.com.activity.JBLApplication

object MyJblDevicesMgr {
    private lateinit var db: AppDatabase

    private fun getDb(): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
                JBLApplication.getJblAppContext(), AppDatabase::class.java).build()
    }

    fun getJblDeviceByBtMac(btMacs: String): MyJBLDevice {
        return getDb().myJBLDeviceDao().getJblDeviceByBtMac(btMacs)
    }

    fun insertAll(myJBLDevices: MyJBLDevice) {
        getDb().myJBLDeviceDao().insertAll(myJBLDevices)
    }

    fun update(myJBLDevice: MyJBLDevice) {
        getDb().myJBLDeviceDao().update(myJBLDevice)
    }

    fun delete(myJBLDevice: MyJBLDevice) {
        getDb().myJBLDeviceDao().delete(myJBLDevice)
    }

    fun closeDb() {
        getDb().close()
    }
}