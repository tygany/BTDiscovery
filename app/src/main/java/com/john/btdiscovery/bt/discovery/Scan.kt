package com.john.btdiscovery.bt.discovery

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log

object Scan {

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