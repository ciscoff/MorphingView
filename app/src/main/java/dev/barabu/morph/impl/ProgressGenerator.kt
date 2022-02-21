package dev.barabu.morph.impl

import android.os.Handler
import android.os.Looper
import android.util.Log
import dev.barabu.morph.button.ProgressButton
import kotlin.random.Random

class ProgressGenerator(private val listener: OnCompleteListener) {

    interface OnCompleteListener {
        fun onComplete()
    }

    private var progress = MIN_PROGRESS
    private val handler = Handler(Looper.getMainLooper())

    fun start(consumer: ProgressButton, delay: Long) {

        handler.postDelayed(object : Runnable {
            override fun run() {
                progress += PROGRESS_STEP
                Log.d("LOG_TAG", "progress=$progress")

                if (progress > MAX_PROGRESS) {
                    listener.onComplete()
                } else {
                    consumer.updateProgress(progress)
                    handler.postDelayed(this, Random.nextLong(MIN_DELAY, MAX_DELAY))
                }
            }
        }, delay)
    }

    fun start(consumer: ProgressButton) {
        start(consumer, MIN_DELAY)
    }

    companion object {
        const val MAX_PROGRESS = 100
        const val MIN_PROGRESS = 0
        const val PROGRESS_STEP = 5
        const val MIN_DELAY = 10L
        const val MAX_DELAY = 300L
    }
}