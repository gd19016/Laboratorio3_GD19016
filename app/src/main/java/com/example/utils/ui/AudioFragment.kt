package com.example.utils.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import com.example.utils.R
class AudioFragment : Fragment() {
    private var audio: MediaPlayer? = null
    private lateinit var playButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var audioProgress: SeekBar
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = inflater.inflate(R.layout.fragment_audio, container, false)
        playButton = fragment.findViewById(R.id.play_button)
        playButton.setOnClickListener {
            playOrPauseSound()
        }
        stopButton = fragment.findViewById(R.id.stop_button)
        stopButton.setOnClickListener {
            stopSound()
        }
        audioProgress = fragment.findViewById(R.id.progressBar)
        audioProgress.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    if (audio != null) {
                        audio!!.seekTo(i * 1000)
                    } else {
                        seekBar.progress = 0
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        return fragment
    }
    override fun onStop() {
        super.onStop()
        if (audio != null) {
            audio!!.release()
            resetAudioBar()
            audio = null
        }
    }
    private fun playOrPauseSound() {
        when {
            audio == null -> {
                audio = MediaPlayer.create(activity, R.raw.music)
                audio!!.isLooping = true
                audio!!.start()
                playButton.setImageResource(R.drawable.ic_button_pause)
                initializeAudioBar()
            }
            audio!!.isPlaying -> {
                audio!!.pause()
                playButton.setImageResource(R.drawable.ic_button_play)
            }
            else -> {
                audio!!.start()
                playButton.setImageResource(R.drawable.ic_button_pause)
            }
        }
    }
    private fun stopSound() {
        if (audio != null) {
            audio!!.stop()
            audio!!.release()
            resetAudioBar()
            playButton.setImageResource(R.drawable.ic_button_play)
            audio = null
        }
    }
    private fun initializeAudioBar() {
        audioProgress.max = audio!!.duration/1000;
        audioProgress.progress = 0
        runnable = Runnable {
            if (audio!!.isPlaying) {
                audioProgress.progress = audio!!.currentPosition/1000
                handler.postDelayed(runnable, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }
    private fun resetAudioBar() {
        audioProgress.progress = 0
        handler.removeCallbacks(runnable)
    }
}