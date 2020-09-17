package com.john.btdiscovery.db

import androidx.room.*
import com.john.btdiscovery.db.MyJBLDevice

@Dao
interface MyJBLDeviceDao {
    @Query("SELECT * FROM MyJBLDevice")
    fun getAll(): List<MyJBLDevice>

    @Query("SELECT * FROM MyJBLDevice WHERE btMac = :btMacs")
    fun getJblDeviceByBtMac(btMacs: String): MyJBLDevice

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg myJBLDevices: MyJBLDevice)

    @Update
    fun update(vararg myJBLDevices: MyJBLDevice)

    @Delete
    fun delete(myJBLDevice: MyJBLDevice)
}