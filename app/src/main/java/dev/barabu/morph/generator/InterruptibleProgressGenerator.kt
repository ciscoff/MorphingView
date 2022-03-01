package dev.barabu.morph.generator

import android.os.Handler
import android.os.Looper
import dev.barabu.morph.button.ProgressConsumer

class InterruptibleProgressGenerator(
    private val listener: ProgressGenerator.OnCompleteListener
) : ProgressGenerator {

    private var progress = ProgressGenerator.MIN_PROGRESS
    private val handler = Handler(Looper.getMainLooper())
    private var isInterrupted: Boolean = false

    override fun start(consumer: ProgressConsumer, delay: Long) {
        progress = ProgressGenerator.MIN_PROGRESS
        isInterrupted = false

        handler.postDelayed(object : Runnable {
            override fun run() {
                progress += PROGRESS_STEP

                if (progress > ProgressGenerator.MAX_PROGRESS) {
                    if (isInterrupted) {
                        listener.onComplete()
                    } else {
                        progress = ProgressGenerator.MIN_PROGRESS + PROGRESS_STEP
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

    fun interrupt() {
        isInterrupted = true
    }

    companion object {
        private const val PROGRESS_STEP = 5
        private const val DELAY = 30L
    }
}