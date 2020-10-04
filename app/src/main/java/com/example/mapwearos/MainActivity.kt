package com.example.mapwearos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.BuildConfig
import com.example.mapwearos.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var googleMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpMap()

        //setUpButton()

        //requestLocationViaFuseLocationClient()

    }

    private fun setUpButton() {
//        binding.button.setOnClickListener {
//            Log.i("Main", "button click")
////            requestLocationViaLocationManager()
//            requestLocationViaFuseLocationClient()
//        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationViaFuseLocationClient() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            //interval = 5 * 1000
            //fastestInterval = 2 * 1000
        }

//        fusedLocationProviderClient.lastLocation
//            .addOnSuccessListener {
//                Log.i("Main", "addOnSuccessListener: ${it?.latitude}")
//            }.addOnFailureListener {
//                Log.e("Main", "addOnFailureListener: ${it.message}")
//            }


        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    Toast.makeText(
                        this@MainActivity,
                        "${p0?.lastLocation?.latitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("Main", "onLocationResult: ${p0?.locations?.size}")

                    p0?.lastLocation?.let {
                        googleMap.clear()
                        addMarker(it)
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun addMarker(lastLocation: Location) {
        val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions =
            MarkerOptions().position(latLng)
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(
                    latLng,
                    15F
                )
            )
        )
    }

    private fun setUpMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it

            googleMap.setOnMapClickListener {
                requestLocationViaFuseLocationClient()
            }
        }
    }
}