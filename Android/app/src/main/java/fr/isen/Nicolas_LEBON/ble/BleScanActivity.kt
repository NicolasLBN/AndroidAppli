package fr.isen.Nicolas_LEBON.ble

import BleScanAdapter
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.Nicolas_LEBON.R
import fr.isen.Nicolas_LEBON.databinding.ActivityBleBinding

class BleScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBleBinding
    private var isScanning = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private val handler = Handler()
    private var myDeviceList: MutableList<ScanResult> = mutableListOf()


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisation du Bluetooth adapter.
        bluetoothAdapter = getSystemService(BluetoothManager::class.java)?.adapter

        startBLEIfPossible()

        binding.bleScanPlayPauseAction.setOnClickListener {
            togglePlayPauseAction()
        }
        binding.blescantitle.setOnClickListener() {
            togglePlayPauseAction()
        }

    }


    private fun startBLEIfPossible() {

        when {
            !packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) || bluetoothAdapter == null -> {
                Toast.makeText(this, "Cet appareil n'est pas compatible, sorry", Toast.LENGTH_SHORT)
                        .show()
            }
            !(bluetoothAdapter?.isEnabled ?: false) -> {
                //je dois activer le bluethooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }

            ActivityCompat.checkSelfPermission
            (
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_PERMISSION_LOCATION
                )
            }
            else -> {
                //scanLeDevice()//youpi, on peut faire du BLE des alpes
                bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
                //initRecyclerDevice()

            }
        }
    }


    private fun scanLeDevice() {
        bluetoothLeScanner?.let { scanner ->
            if (!isScanning) {
                handler.postDelayed({
                    isScanning = false
                    scanner.stopScan(leScanCallback)
                }, SCAN_PERIOD)
                isScanning = true
                myDeviceList = mutableListOf()
                scanner.startScan(leScanCallback)
            } else {
                isScanning = false
                scanner.stopScan(leScanCallback)
            }
        }
    }


    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            if (!result.scanRecord?.deviceName.isNullOrEmpty())
            {
                myDeviceList.add(result) // remplacer par add result
                updateRecyclerDevice()
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            startBLEIfPossible()
        }
    }


    private fun togglePlayPauseAction() {
        if (!isScanning) {
            binding.blescantitle.text = getString(R.string.ble_scan_pause_title)
            binding.bleScanPlayPauseAction.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.loadingProgress.isVisible = true
            binding.divider.isVisible = false
            scanLeDevice()
            binding.blescantitle.text = "Arretez le scan Ble"
            binding.bleScanPlayPauseAction.setImageResource(R.drawable.ic_baseline_pause_24)
        } else {
            binding.blescantitle.text = getString(R.string.ble_scan_pause_title)
            binding.bleScanPlayPauseAction.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.loadingProgress.isVisible = false
            binding.divider.isVisible = true
            scanLeDevice()


        }
    }

    private fun updateRecyclerDevice() {
          binding.devicelist.layoutManager = LinearLayoutManager(this)
          binding.devicelist.adapter = BleScanAdapter(myDeviceList) {deviceInfo ->
              val intent = Intent(this, DetailBleActivity::class.java)
              intent.putExtra("goToDetail", deviceInfo.device)
              startActivity(intent)
          }
      }

    companion object {
        const val REQUEST_ENABLE_BT = 22
        const val REQUEST_PERMISSION_LOCATION = 22
        const val SCAN_PERIOD: Long = 10000
    }

    // Stops scanning after 10 seconds.


}




