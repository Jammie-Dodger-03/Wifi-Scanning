package com.example.wifiscanner

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
import com.example.wifiscanner.ui.theme.WifiScannerTheme
import androidx.activity.compose.setContent


class MainActivity : ComponentActivity() {

    public lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialisation()

        setContent {
            WifiScannerTheme {
                scannerApp(this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    public override fun onStart() {
        super.onStart()
        wifiList()
    }

    private fun wifiManagerInitialisation(){

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun initialisation() {

        registerReceiverForWiFiScan()
        wifiManagerInitialisation()
    }

    // Establishes all necessary permissions for a wifi scan
    private fun registerReceiverForWiFiScan() {

        if (ActivityCompat.checkSelfPermission(       // If location permission isn't granted
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val accessFinePermission =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) {    // Request permission for location
                    if (it) {   // If permission granted

                        Log.d(  // Sends notification of permission granted
                            "myWifiManager",
                            "Location Permission Granted"
                        )

                    } else {
                        // Permission is denied
                        // Show an error message
                    }
                }
            accessFinePermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
    }
}


fun scannerApp(activity: MainActivity){
    while(true){
        activity.wifiList()
    }
}


@SuppressLint("MissingPermission")
private fun MainActivity.wifiList() {

    initialisation()
    val wifiFullnfo = wifiManager.scanResults


    // Find just the SSIDs
    val wifiNetworks = wifiFullnfo.map{it.SSID}

    Log.d("test", "wifiManager_jamie_is_trying: $wifiNetworks")

}





//