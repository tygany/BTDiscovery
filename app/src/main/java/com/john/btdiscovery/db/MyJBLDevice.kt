package jbl.stc.com.database.myproducts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyJBLDevice(
        @PrimaryKey
        val index: Int,

        @ColumnInfo(name = "btMac")
        val btMac: String?,

        @ColumnInfo(name = "connectStatus")
        val connectStatus: Int,

        @ColumnInfo(name = "colorId")
        val colorId: Int,

        @ColumnInfo(name = "deviceName")
        val deviceName: String?
)