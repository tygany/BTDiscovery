package com.john.btdiscovery.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MyJBLDevice")
class MyJBLDevice(

    @PrimaryKey
    var btMac: String,

    @ColumnInfo(name = "connectStatus")
    var connectStatus: Int,

    @ColumnInfo(name = "colorId")
    var colorId: Int,

    @ColumnInfo(name = "deviceName")
    var deviceName: String
) {

    override fun toString(): String {
        return "\nMyJBLDevice(btMac='$btMac', connectStatus=$connectStatus, colorId=$colorId, deviceName='$deviceName')"
    }
}