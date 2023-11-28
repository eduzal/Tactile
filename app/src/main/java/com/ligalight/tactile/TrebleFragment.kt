package com.ligalight.tactile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class TrebleFragment : Fragment() {

    private lateinit var vibrator: Vibrator
    private lateinit var blinkingView: View
    private var animationDuration: Long = 50
    private val handler = Handler(Looper.getMainLooper())
    val fragmentTag = "TrebleFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_treble, container, false)

        blinkingView = view.findViewById(R.id.blinkingView)

        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Inflate the layout for this fragment
        return view
    }

    fun updateAnimation(bang: Boolean) {
        if (bang) {
            applyBlinkAnimation(animationDuration)
                //animateIcon(animationDuration)
            startVibration(animationDuration)
        } else {
            // Handle if animation is false
        }
    }

    private fun startVibration(duration: Long) {
        // Cancel previous vibration
        vibrator.cancel()

        val pattern = longArrayOf(duration/2, duration, duration/2)
        val amplitudes = intArrayOf(0, 255, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(pattern, amplitudes, -1)
            vibrator.vibrate(vibrationEffect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, -1)
        }
    }

    private fun applyBlinkAnimation(duration: Long) {
        val blinkAnimation =
            android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        blinkAnimation.duration = duration
            //blinkAnimation.repeatCount = Animation.INFINITE
        blinkingView.startAnimation(blinkAnimation)
    }

    override fun onStop() {
        super.onStop()
        vibrator.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        vibrator.cancel()
    }

    override fun onResume() {
        super.onResume()
        startVibration(animationDuration)
    }
}
