package com.john.btdiscovery.bt.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.john.btdiscovery.db.MyJBLDevice
import com.john.btdiscovery.db.MyJblDevicesMgr
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object Scan {

    fun discovery(context: Context) {
        Log.i(TAG, "discovery")
        val mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        mBtAdapter.startDiscovery()
        mReceiver = SingBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //开始扫描
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
//        filter.addAction(BluetoothDevice.ACTION_FOUND);//搜索到设备
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //配对状态监听
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        context.registerReceiver(mReceiver, filter)
    }

    fun cancelDiscovery(context: Context) {
        Log.i(TAG, "cancelDiscovery")
        context.unregisterReceiver(mReceiver)
        val mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBtAdapter.isDiscovering) {
            mBtAdapter.cancelDiscovery()
        }
    }

    private var mReceiver: SingBroadcastReceiver? = null

    private class SingBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val action = intent.action
            if (BluetoothDevice.ACTION_ACL_CONNECTED == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // Add the name and address to an array adapter to show in a Toast
                if (device != null) {
                    val deviceStr = device.name + " - " + device.address
                    Log.i(TAG, "SingBroadcastReceiver ACTION_ACL_CONNECTED deviceStr: $deviceStr")
                    GlobalScope.launch {
                        val myJBLDevice = MyJBLDevice(device.address, 1, 0, device.name)
                        val myJBLDeviceDb = MyJblDevicesMgr.getAll()
                        Log.i(TAG, "SingBroadcastReceiver myJBLDeviceDb: $myJBLDeviceDb")
                        MyJblDevicesMgr.insertAll(myJBLDevice)
                    }
                }
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // Add the name and address to an array adapter to show in a Toast
                if (device != null) {
                    val deviceStr = device.name + " - " + device.address
                    Log.i(TAG, "SingBroadcastReceiver ACTION_ACL_DISCONNECTED deviceStr: $deviceStr")
//                    val myJBLDevice = MyJBLDevice(1, device.address, 0, 0, device.name)
//                    MyJblDevicesMgr.update(myJBLDevice)

                }
            }
        }
    }

    public fun start(context: Context) {
        if (isBtEnabled()) {
            val mBtAdapter = BluetoothAdapter.getDefaultAdapter()
            mBtAdapter.getProfileProxy(
                context,
                mListener,
                BluetoothProfile.A2DP
            )
        }
    }

    private fun isBtEnabled(): Boolean {
        try {
            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled
        } catch (e: Exception) {
            Log.e(TAG, "Exception:" + e.localizedMessage)
        }
        return false
    }

    class BtListener : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            Log.i(TAG, "onServiceConnected")
            if (profile == BluetoothProfile.A2DP) {
                val deviceList: MutableList<BluetoothDevice>? = proxy?.connectedDevices
                deviceList?.let {
                    for (bluetoothDevice: BluetoothDevice in deviceList) {
                        Log.d(
                            TAG, "onServiceConnected name: " + bluetoothDevice.name
                                    + ",address: " + bluetoothDevice.address
                        )
                    }
                }
            }
            BluetoothAdapter.getDefaultAdapter().closeProfileProxy(profile, proxy)
        }

        override fun onServiceDisconnected(profile: Int) {
            Log.i(TAG, "onServiceDisconnected")

        }
    }

    private val mListener: BtListener = BtListener()
    const val TAG = "Scan"
}