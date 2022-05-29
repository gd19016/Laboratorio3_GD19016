package com.example.utils.ui

import android.os.Bundle
import android.view.*
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.utils.R


class VideoFragment : Fragment(),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {
    private lateinit var gestoTxt: TextView
    private var gDetector: GestureDetector? = null
    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController
    private var pantallaCompleta: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_video, container, false)
        videoView = fragment.findViewById(R.id.videoView1)
        videoView.setVideoPath("https://eisi.fia.ues.edu.sv/materialpublico/pdm115/video2.3gp")
        mediaController = MediaController(context)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()
        gestoTxt = fragment.findViewById(R.id.gestoTxt)
        gDetector = GestureDetector(context, this)
        gDetector?.setOnDoubleTapListener(this)
        fragment.setOnTouchListener { _, event ->
            gDetector!!.onTouchEvent(event)
            true
        }
        return fragment
    }
    override fun onDown(e: MotionEvent?): Boolean {
        gestoTxt.text = "onDown"
        return true
    }
    override fun onShowPress(e: MotionEvent?) {
        gestoTxt.text = "onShowPress"
    }
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        gestoTxt.text = "onSingleTapUp"
        return true
    }
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        gestoTxt.text = "onScroll"
        if (e1 != null && e2 != null) {
            val factor: Float = 0.000001f
            if (e1.y > e2.y) {
                if (videoView.currentPosition + factor <= videoView.duration)
                    videoView.seekTo((videoView.currentPosition + factor).toInt())
                else
                    videoView.seekTo(videoView.duration)
            } else
                if (videoView.currentPosition - factor >= 0)
                    videoView.seekTo((videoView.currentPosition - factor).toInt())
                else
                    videoView.seekTo(0)
        }
        return true
    }
    override fun onLongPress(e: MotionEvent?) {
        gestoTxt.text = "onLongPress"
        val duracion: Int = videoView.duration / 1000
        val horas = duracion / 3600
        val minutos = duracion / 60 - horas * 60
        val segundos = duracion - horas * 3600 - minutos * 60
        val duracionFormateada = String.format("%d:%02d:%02d", horas, minutos, segundos)
        Toast.makeText(
            requireContext(),
            "La duración del vídeo es de: $duracionFormateada", Toast.LENGTH_SHORT
        ).show()
    }
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        gestoTxt.text = "onFling"
        if (e1 != null && e2 != null) {
            if (e1.x > e2.x)
                videoView.seekTo(videoView.duration)
            else
                videoView.seekTo(0)
        }
        return true
    }
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        gestoTxt.text = "onSingleTapConfirmed"
        if (videoView.isPlaying)
            videoView.pause()
        else
            videoView.start()
        return true
    }
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        gestoTxt.text = "onDoubleTap"
        return true
    }
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        gestoTxt.text = "onDoubleTapEvent"
        val momentoActual: Int = videoView.currentPosition / 1000
        val horas = momentoActual / 3600
        val minutos = momentoActual / 60 - horas * 60
        val segundos = momentoActual - horas * 3600 - minutos * 60
        val duracionFormateada = String.format("%d:%02d:%02d", horas, minutos, segundos)
        Toast.makeText(
            requireContext(),
            "El momento de reproducción actual es: $duracionFormateada", Toast.LENGTH_SHORT
        ).show()
        return true
    }
}