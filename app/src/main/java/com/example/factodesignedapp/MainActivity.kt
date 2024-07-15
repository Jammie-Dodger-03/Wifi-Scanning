package com.example.factodesignedapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat


class MainActivity : ComponentActivity() {

    private lateinit var wifiManager: WifiManager

    //    private val wifiScanReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
//            Log.d("StepStep", "Step 3")
//            if (success) {
//                scanSuccess()
//            } else {
//                scanFailure()
//            }
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        registerReceiverForWiFiScan()

        // Check for location permission
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {
//            // Request the permission
//            val REQUEST_CODE_COARSE_LOCATION = 0
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
//                REQUEST_CODE_COARSE_LOCATION
//            )
//        } else {
//            // Permission already granted, register receiver
//            registerReceiverForWiFiScan()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        unregisterReceiver(wifiScanReceiver)
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        Log.d("StepStep", "wifiManager.scanResults: ${wifiManager.scanResults}")
    }

    private fun registerReceiverForWiFiScan() {
//        val intentFilter = IntentFilter()
//        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
//        registerReceiver(wifiScanReceiver, intentFilter)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val accessFinePermission =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (it) {
                        // Permission is granted
                        // TODO: After the permission is granted we can make on another thread a scan to
                        // get the values from: wifiManager.scanResults
                        // val job = CoroutineScope(Dispatchers.IO).launch {
                        // a regular loop here to update a list or a variable with some data related to the wifi network
                        // careful to concurrent access to the list or variable
                        // for this use Synchronized Block with atomic variables or MUTEX or copyOnWriteArrayList
                        // }
                        // onDestroy() from the activity -> job.cancel()
                        // References:
                        // 1. https://developer.android.com/develop/connectivity/wifi/wifi-suggest
                        // 2. https://developer.android.com/develop/connectivity/wifi/wifi-direct

                        Log.d("myWifiManager", "wifiManager.scanResults: ${wifiManager.scanResults}")

                    } else {
                        // Permission is denied
                        // Show an error message
                    }
                }
            accessFinePermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

//        val success = wifiManager.startScan()
//        if (!success) {
//            Log.d("StepStep", "Step 2")
//            // scan failure handling
//            scanFailure()
//        }
    }
//    private fun scanSuccess() {
//        Log.d("StepStep", "Step 1")
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        } else {
//            Log.d("StepStep", "wifiManager.scanResults: ${wifiManager.scanResults}")
//            return
//        }
    // Process results
//}

//    private fun scanFailure() {
//        // Handle failure: new scan did NOT succeed
//        // Consider using old scan results: these are the OLD results!
//        val results: Unit = if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        } else {
//            return
//        }
//    }
}


