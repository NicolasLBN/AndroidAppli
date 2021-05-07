package fr.isen.Nicolas_LEBON.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.provider.Settings.Global.getString
import androidx.appcompat.app.AppCompatActivity
import fr.isen.Nicolas_LEBON.R
import fr.isen.Nicolas_LEBON.databinding.ActivityBleBinding


class BLEDeviceActivity: AppCompatActivity() {
/*
    private lateinit var binding: ActivityBleBinding
    //var bluetoothGatt: BluetoothGatt

    override fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityBleBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val device = intent.getParcelabelExtra<BluetoothDevice>("ble_device")
        binding.deviceName.text = device?.name ?: "Appareil inconnu"
        binding.deviceStatus.text = getString(R.string.ble_device_status, getString(R.string.ble_device_status_connecting))

        connectToDevice(device)
    }

    private fun connectToDevice(device: BluetoothDevice?) {
        bluetoothGatt = device?.connectGatt(this, false, BluetoothGattCallback) {
            override fun onConnectionChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                onConnectionChange(newState, gatt)
            }


        override onServicesDiscovered (gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }

        override onCharacteristicRead (gatt: BluetoothGatt?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }
    })

}

private fun connectionStateChange(newState: Int, gatt: BluetoothGatt?) {
    BLEConnexionState.getBLEConnexionStateFromState(newState)?.let {
        binding.deviceStatus.text = getString(R.string.ble_device_status, getString(it.text))
    }

 */
}