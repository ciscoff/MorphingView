package dev.barabu.morph.impl

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import dev.barabu.morph.button.*
import dev.barabu.morph.generator.IndeterminateProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator
import dev.barabu.morph.generator.ProgressGenerator.Companion.MAX_PROGRESS
import dev.barabu.morph.generator.ProgressGenerator.Companion.MIN_PROGRESS

class LinearProgressButton : ProgressMorphingButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override var generator: ProgressGenerator = IndeterminateProgressGenerator(
        object : ProgressGenerator.OnCompleteListener {
            override fun onComplete() {
                progress = MIN_PROGRESS
                unBlockTouch()
            }
        })

    private val rectProgress = RectF()

    /**
     * Этот метод работает только для рисования "линии прогресса"
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isMorphingInProgress && progress > MIN_PROGRESS && progress <= MAX_PROGRESS) {
            paint.color = primaryColor

            rectProgress.apply {
                bottom = height.toFloat()
                right = (width.toFloat() / MAX_PROGRESS) * progress
            }

            canvas?.drawRoundRect(rectProgress, cornerRadius, cornerRadius, paint)
        }
    }

    override fun morphToFinish(
        colorNormal: Int,
        colorPressed: Int,
        cornerRadius: Float,
        width: Int,
        height: Int,
        duration: Int,
        iconId: Int
    ) {
        TODO("Not yet implemented")
    }
}

