package dev.barabu.morph.generator

import dev.barabu.morph.button.ProgressButton

interface ProgressGenerator {

    interface OnCompleteListener {
        fun onComplete()
    }

    fun start(consumer: ProgressButton, delay: Long) {
    }

    fun start(consumer: ProgressButton) {
    }

    companion object {
        const val MAX_PROGRESS = 100
        const val MIN_PROGRESS = 0
    }
}