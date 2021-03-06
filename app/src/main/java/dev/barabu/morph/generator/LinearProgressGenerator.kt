package dev.barabu.morph.generator

import android.os.Handler
import android.os.Looper
import dev.barabu.morph.button.ProgressConsumer
import dev.barabu.morph.generator.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.generator.ProgressGenerator.Companion.MIN_PROGRESS

class LinearProgressGenerator(
    private val listener: ProgressGenerator.OnCompleteListener,
    private val isFinite: Boolean = true
) : ProgressGenerator {

    private var progress = MIN_PROGRESS
    private val handler = Handler(Looper.getMainLooper())

    override fun start(consumer: ProgressConsumer, delay: Long) {
        progress = MIN_PROGRESS

        handler.postDelayed(object : Runnable {
            override fun run() {
                progress += PROGRESS_STEP

                if (progress > MAX_PROGRESS) {

                    if (isFinite) {
                        listener.onComplete()
                    } else {
                        progress = MIN_PROGRESS + PROGRESS_STEP
                        consumer.updateProgress(progress)
                        handler.postDelayed(this, DELAY)
                    }
                } else {
                    consumer.updateProgress(progress)
                    handler.postDelayed(this, DELAY)
                }
            }
        }, delay)
    }

    override fun start(consumer: ProgressConsumer) {
        start(consumer, DELAY)
    }

    companion object {
        private const val PROGRESS_STEP = 5
        private const val DELAY = 30L
    }
}