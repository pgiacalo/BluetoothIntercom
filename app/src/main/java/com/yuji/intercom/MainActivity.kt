package com.yuji.intercom

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Context
import android.bluetooth.BluetoothManager
import androidx.compose.material.ButtonDefaults

class MainActivity : AppCompatActivity() {

    private val context = this
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val REQUEST_ENABLE_BT = 1
    private val bluetoothDevices = mutableStateListOf<BluetoothDevice>()
    private val selectedDevices = mutableStateListOf<BluetoothDevice>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ENABLE_BT
            )
        }

        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        setContent {
            Column {
                Button(onClick = {
                    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
                    pairedDevices?.forEach { device ->
                        if (device !in bluetoothDevices) {
                            bluetoothDevices.add(device)
                        }
                    }
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)) {
                    Text("Refresh Bluetooth Devices", color = Color.White)
                }

                LazyColumn {
                    items(bluetoothDevices) { device ->
                        Button(onClick = {
                            if (selectedDevices.size < 2 && device !in selectedDevices) {
                                selectedDevices.add(device)
                            }
                        }) {
                            Text("${device.name} - ${device.address}")
                        }
                    }
                }

                Text("Selected Devices:")

                LazyColumn {
                    items(selectedDevices) { device ->
                        Text("${device.name} - ${device.address}")
                    }
                }
            }
        }
    }
}
