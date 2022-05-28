package com.example.utils.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.utils.R
class GPSFragment : Fragment(), LocationListener {
    private lateinit var edtLatitud: EditText
    private lateinit var edtLongitud: EditText
    private lateinit var edtAltitud: EditText
    private lateinit var edtDireccion: EditText
    private lateinit var locationManager: LocationManager
    private lateinit var geoCoder: Geocoder
    private val locationPermissionCode = 2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_g_p_s, container, false)
        edtLatitud = fragment.findViewById(R.id.edtLatitud)
        edtLongitud = fragment.findViewById(R.id.edtlongitud)
        edtAltitud = fragment.findViewById(R.id.edtAltitud)
        edtDireccion = fragment.findViewById(R.id.edtDireccion)
        geoCoder = Geocoder(context)
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as
                LocationManager
        if ((ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode)
        } else {
            initLocation()
        }
        return fragment
    }
    override fun onLocationChanged(location: Location) {
        edtLatitud.setText(location.latitude.toString())
        edtAltitud.setText(location.altitude.toString())
        edtLongitud.setText(location.longitude.toString())
        val dir = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        if (dir != null && dir.count() > 0) {
            val direccion = dir.first()
            edtDireccion.setText("${direccion.locality}, ${direccion.subAdminArea}, ${direccion.countryName}")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                initLocation()
            }
            else {
                Toast.makeText(context, "Permiso para GPS denegado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun initLocation() {
        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            onLocationChanged(it)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0f, this)
    }
}