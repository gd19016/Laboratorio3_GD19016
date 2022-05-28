package com.example.utils.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.example.utils.R
class GestosFragment : Fragment(),
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {
    private lateinit var gestoTxt: TextView
    private var gDetector: GestureDetector? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_gestos, container, false)
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
        return true
    }
    override fun onLongPress(e: MotionEvent?) {
        gestoTxt.text = "onLongPress"
    }
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        gestoTxt.text = "onFling"
        return true
    }
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        gestoTxt.text = "onSingleTapConfirmed"
        return true
    }
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        gestoTxt.text = "onDoubleTap"
        return true
    }
    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        gestoTxt.text = "onDoubleTapEvent"
        return true
    }
}