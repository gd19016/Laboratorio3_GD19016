package com.example.utils.ui

import android.annotation.SuppressLint
import android.content.Context.SENSOR_SERVICE
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.utils.R
import kotlin.math.roundToInt

class AcelerometroFragment : Fragment(), SensorEventListener {
    private lateinit var xText: EditText
    private lateinit var yText: EditText
    private lateinit var zText: EditText
    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    private val acelerometroValues = FloatArray(3)
    private val magnometroValues = FloatArray(3)
    private val matrizDeRotacion = FloatArray(9)
    private val angulosDeRotacion = FloatArray(3)
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_acelerometro, container, false)
        xText = fragment.findViewById(R.id.xID)
        yText = fragment.findViewById(R.id.yID)
        zText = fragment.findViewById(R.id.zID)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;
        sensorManager = requireActivity().getSystemService(SENSOR_SERVICE) as
                SensorManager?
        return fragment
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }
    override fun onResume() {
        super.onResume()
        if (sensorManager != null) {
            sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also{ accelerometer ->
                sensorManager!!.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
            }
            sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also{ magneticField ->
                sensorManager!!.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(this, sensor)
        super.onPause()
    }
    override fun onStop() {
        sensorManager!!.unregisterListener(this, sensor)
        super.onStop()
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(event.values, 0, acelerometroValues, 0,
                        acelerometroValues.size)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(event.values, 0, magnometroValues, 0, magnometroValues.size)
                }
            }
            SensorManager.getRotationMatrix(matrizDeRotacion, null, acelerometroValues,
                magnometroValues)
            SensorManager.getOrientation(matrizDeRotacion, angulosDeRotacion)
            xText.setText(radianesAGrados(angulosDeRotacion[1]).toString())
            yText.setText(radianesAGrados(angulosDeRotacion[2]).toString())
            zText.setText(radianesAGrados(angulosDeRotacion[0]).toString())
        };
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    private fun radianesAGrados(radianes: Float): Int {
        val grados = (Math.toDegrees(radianes.toDouble()) + 360) % 360
        return (grados * 100).roundToInt() / 100
    }
}