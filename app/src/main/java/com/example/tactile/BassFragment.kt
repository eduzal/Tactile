package com.example.tactile

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
import android.view.animation.Animation
import android.widget.ImageView
import androidx.fragment.app.Fragment


class BassFragment : Fragment() {

    private lateinit var vibrator: Vibrator
    private lateinit var blinkingView: View
    private lateinit var iconT: ImageView
    private var animationDuration: Long = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bass,container,false)
        animationDuration = requireArguments().getLong(ARG_DURATION)

        blinkingView  = view.findViewById(R.id.blinkingView)
        applyBlinkAnimation(animationDuration)

        iconT = view.findViewById(R.id.viewIcon)
        animateIcon(animationDuration)

        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        startVibration(animationDuration)

        // Inflate the layout for this fragment
        return view
    }

    private fun animateIcon(duration: Long){
        val scaleAnimation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.scale_animation)
        scaleAnimation.duration = duration
        iconT.startAnimation(scaleAnimation)
    }

    private fun startVibration(duration: Long) {
        val pattern = longArrayOf(duration/2, duration, duration/2)
        val amplitudes = intArrayOf(0, 255,0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createWaveform(pattern, amplitudes, 0)
            vibrator.vibrate(vibrationEffect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }
    }

    private fun applyBlinkAnimation(duration: Long) {
        val blinkAnimation = android.view.animation.AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        blinkAnimation.duration = duration
        blinkAnimation.repeatCount = Animation.INFINITE
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

    companion object {
        private const val ARG_DURATION = "ARG_DURATION"

        fun newInstance(duration: Long): BassFragment {
            val fragment = BassFragment()
            val args = Bundle()
            args.putLong(ARG_DURATION, duration)
            fragment.arguments = args
            return fragment
        }
    }
}

    /*
    override fun onStop() {
        super.onStop()
        vibrator.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        vibrator.cancel()
    }
    */
