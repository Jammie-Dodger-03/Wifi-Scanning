package com.example.wifiscanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.wifiscanner.ui.theme.WifiScannerTheme

class MainActivity : ComponentActivity() {

    lateinit var wifiManager: WifiManager
    var wifiNetworks = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialisation()

        setContent {
            WifiScannerTheme {
                ScannerApp(this, wifiNetworks)
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

        RegisterPermissions()
        wifiManagerInitialisation()
    }

    // Establishes all necessary permissions for a wifi scan
    private fun RegisterPermissions() {

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


    ///////// Facto Code ////////////////////

//    @RequiresApi(Build.VERSION_CODES.Q)
//    @SuppressLint("ServiceCast")
//    fun connectToWifi(ssid: String, password: String) {
//        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
//            .setSsid(ssid)
//            //.setWpa2Passphrase(password)
//            .build()
//
//        val networkRequest = NetworkRequest.Builder()
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .setNetworkSpecifier(wifiNetworkSpecifier)
//            .build()
//
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.requestNetwork(networkRequest, object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: android.net.Network) {
//                super.onAvailable(network)
//                connectivityManager.bindProcessToNetwork(network)
//            }
//        })
//    }
//    @SuppressLint("MissingPermission")
//    @Suppress("DEPRECATION")
//    fun connectToWifiLegacy(ssid: String, password: String) {
//        val wifiConfig = WifiConfiguration().apply {
//            SSID = String.format("\"%s\"", ssid)
//            preSharedKey = String.format("\"%s\"", password)
//        }
//
//        wifiManager.addNetwork(wifiConfig)
//        val networkId = wifiManager.configuredNetworks.find { it.SSID == wifiConfig.SSID }?.networkId
//        if (networkId != null) {
//            wifiManager.disconnect()
//            wifiManager.enableNetwork(networkId, true)
//            wifiManager.reconnect()
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun connectToOpenWifi(ssid: String) {
        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)
            .build()

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(wifiNetworkSpecifier)
            .build()

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                connectivityManager.bindProcessToNetwork(network)
                Log.d("WifiConnection", "Connected to $ssid")
            }

            override fun onUnavailable() {
                super.onUnavailable()
                Log.d("WifiConnection", "Connection to $ssid failed")
            }
        })
    }
//    @Suppress("DEPRECATION")
//    fun connectToOpenWifiLegacy(ssid: String) {
//        val wifiConfig = WifiConfiguration().apply {
//            SSID = String.format("\"%s\"", ssid)
//            allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
//        }
//
//        wifiManager.addNetwork(wifiConfig)
//        val networkId = wifiManager.configuredNetworks.find { it.SSID == wifiConfig.SSID }?.networkId
//        if (networkId != null) {
//            wifiManager.disconnect()
//            wifiManager.enableNetwork(networkId, true)
//            wifiManager.reconnect()
//        }
//    }


    /////////////// End of Facto ////////////////////

}


@Composable
fun ScannerApp(activity: MainActivity, wifiNetworks: List<String>) {

    var status by remember { mutableIntStateOf(1) }
    when (status) {
        1 ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(onClick = {
                    activity.wifiList()
                    status = 2
                }) {
                    Text("Search for wifi")


                    }
                }

        2 ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Button(onClick= {
                    activity.wifiList()
                    status = 2
                }) {
                    Text("Refresh Networks")
                }
                wifiNetworks .forEach { ssid ->
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            activity.connectToOpenWifi(ssid)
                        } else {
                            // activity.connectToOpenWifiLegacy(ssid)
                        }
                        Log.d("test", "connect_to_jamie_is_trying: $ssid")
                        status = 3
                    }) {
                        Text(ssid)
                    }
                }


            }


        else -> {}
    }
}







@SuppressLint("MissingPermission")
private fun MainActivity.wifiList() {

    initialisation() // Just checks that permissions are established
    val wifiFullnfo = wifiManager.scanResults // Find local networks

    // Find just the SSIDs
    val wifiNetworksList = wifiFullnfo.map{it.SSID}
    val filteredWifiNetworksList = wifiNetworksList.filter{ it.contains("plug", ignoreCase = true) or it.contains("tasmota", ignoreCase = true)}
    wifiNetworks.clear()
    wifiNetworks.addAll(filteredWifiNetworksList)

    Log.d("test", "wifiManager_jamie_is_trying: $wifiNetworks")

}





//