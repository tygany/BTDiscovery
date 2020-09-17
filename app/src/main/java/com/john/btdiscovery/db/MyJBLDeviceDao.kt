package jbl.stc.com.database.myproducts

import androidx.room.*

@Dao
interface MyJBLDeviceDao {
    @Query("SELECT * FROM MyJBLDevice")
    fun getAll(): List<MyJBLDevice>

    @Query("SELECT * FROM MyJBLDevice WHERE btMac = 'btMacs'")
    fun getJblDeviceByBtMac(btMacs: String): MyJBLDevice

    @Insert
    fun insertAll(vararg myJBLDevices: MyJBLDevice)

    @Update
    fun update(vararg myJBLDevices: MyJBLDevice)

    @Delete
    fun delete(myJBLDevice: MyJBLDevice)
}