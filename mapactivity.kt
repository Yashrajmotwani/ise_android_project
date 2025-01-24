package com.example.uitestmap

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // This callback is triggered when the map is ready to be used.
        mMap = googleMap

        // List of IITs, IISc locations (latitude, longitude) with their names and websites
        val locations = listOf(
            LatLng(23.0333, 72.5704) to Pair("IIT Gandhinagar", "http://www.iitgn.ac.in/"),
            LatLng(20.2961, 85.8189) to Pair("IIT Bhubaneswar", "http://www.iitbbs.ac.in/"),
            LatLng(12.9911, 80.2354) to Pair("IIT Madras", "http://www.iitm.ac.in/"),
            LatLng(26.2006, 91.6788) to Pair("IIT Guwahati", "http://www.iitg.ernet.in/"),
            LatLng(22.7196, 75.8577) to Pair("IIT Indore", "http://www.iiti.ac.in/"),
            LatLng(26.4499, 80.3329) to Pair("IIT Kanpur", "http://www.iitk.ac.in/"),
            LatLng(26.2195, 73.0236) to Pair("IIT Jodhpur", "https://www.iitj.ac.in/"),
            LatLng(22.3026, 87.3101) to Pair("IIT Kharagpur", "http://www.iitkgp.ac.in/"),
            LatLng(17.5027, 78.3005) to Pair("IIT Hyderabad", "http://www.iith.ac.in"),
            LatLng(19.0836, 72.9157) to Pair("IIT Mumbai", "http://www.iitb.ac.in/"),
            LatLng(25.6097, 85.1365) to Pair("IIT Patna", "http://www.iitp.ac.in/"),
            LatLng(28.6129, 77.2295) to Pair("IIT Delhi", "http://www.iitd.ac.in/"),
            LatLng(31.3317, 76.6821) to Pair("IIT Ropar", "http://www.iitrpr.ac.in/"),
            LatLng(32.2497, 76.9338) to Pair("IIT Mandi", "http://www.iitmandi.ac.in/"),
            LatLng(29.8500, 78.1636) to Pair("IIT Roorkee", "https://www.iitr.ac.in"),
            LatLng(25.3192, 82.9865) to Pair("IIT BHU", "http://iitbhu.ac.in"),
            LatLng(32.5500, 74.8756) to Pair("IIT Jammu", "http://iitjammu.ac.in"),
            LatLng(10.5600, 76.9574) to Pair("IIT Palakkad", "http://iitpkd.ac.in"),
            LatLng(13.6280, 79.9313) to Pair("IIT Tirupati", "http://iittp.ac.in/"),
            LatLng(15.2894, 73.9948) to Pair("IIT Goa", "http://www.iitgoa.ac.in"),
            LatLng(21.9214, 81.7327) to Pair("IIT Bhilai", "https://www.iitbhilai.ac.in/"),
            LatLng(15.3670, 75.0662) to Pair("IIT Dharwad", "http://www.iitdh.ac.in/"),
            LatLng(23.8160, 86.4343) to Pair("IIT Dhanbad", "https://www.iitism.ac.in/"),
            LatLng(12.9716, 77.5946) to Pair("IISc Bangalore", "http://www.iisc.ac.in/") // IISc
        )

        // Add markers for all IITs and IISc with website links
        for (location in locations) {
            mMap.addMarker(MarkerOptions()
                .position(location.first)
                .title(location.second.first)
                .snippet(location.second.second)) // Adding website URL as snippet
        }

        // Set a default zoom level to view the entire map
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(20.5937, 78.9629), 5f))
    }
}
