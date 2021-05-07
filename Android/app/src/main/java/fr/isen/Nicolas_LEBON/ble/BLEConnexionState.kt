package fr.isen.Nicolas_LEBON.ble


import android.bluetooth.BluetoothAdapter.STATE_CONNECTED
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.STATE_CONNECTING
import android.bluetooth.BluetoothProfile.STATE_DISCONNECTED
import androidx.annotation.StringRes
import fr.isen.Nicolas_LEBON.R


enum class BLEConnexionState ( val state: Int, @StringRes val text: Int) {/*
    STATE_CONNECTING(BluetoothProfile.STATE_CONNECTING, R.string.ble_device_status_connecting),
    STATE_CONNECTED(BluetoothProfile.STATE_CONNECTED, R.string.ble_device_status_connected),
    STATE_DISCONNECTED(BluetoothProfile.STATE_DISCONNECTED, R.string.ble_device_status_disconnected);


    companion object {
        fun getBLEConnexionStateFromState (state: Int) = values().firstOrNull() {
            it.state == state
    }
    */
}
