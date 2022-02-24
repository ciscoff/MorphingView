package dev.barabu.morph.generator

import dev.barabu.morph.button.ProgressConsumer

interface ProgressGenerator {

    interface OnCompleteListener {
        fun onComplete()
    }

    fun start(consumer: ProgressConsumer, delay: Long) {
    }

    fun start(consumer: ProgressConsumer) {
    }

    companion object {
        const val MAX_PROGRESS = 100
        const val MIN_PROGRESS = 0
    }
}