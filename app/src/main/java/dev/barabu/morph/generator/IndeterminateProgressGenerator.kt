package dev.barabu.morph.generator

import android.os.Handler
import android.os.Looper
import dev.barabu.morph.button.ProgressButton
import dev.barabu.morph.generator.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.generator.ProgressGenerator.Companion.MIN_PROGRESS
import kotlin.random.Random

class IndeterminateProgressGenerator(
    private val listener: ProgressGenerator.OnCompleteListener
) : ProgressGenerator {

    private var progress = MIN_PROGRESS
    private val handler = Handler(Looper.getMainLooper())

    override fun start(consumer: ProgressButton, delay: Long) {

        handler.postDelayed(object : Runnable {
            override fun run() {
                progress += PROGRESS_STEP

                if (progress > MAX_PROGRESS) {
                    listener.onComplete()
                } else {
                    consumer.updateProgress(progress)
                    handler.postDelayed(this, Random.nextLong(MIN_DELAY, MAX_DELAY))
                }
            }
        }, delay)
    }

    override fun start(consumer: ProgressButton) {
        start(consumer, MIN_DELAY)
    }

    companion object {
        private const val PROGRESS_STEP = 5
        private const val MIN_DELAY = 10L
        private const val MAX_DELAY = 300L
    }
}