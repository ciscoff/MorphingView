package dev.barabu.runner.circular

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import dev.barabu.morph.R
import dev.barabu.morph.extentions.color
import dev.barabu.morph.extentions.dimen

class AdvancedCircularRunnerView : View {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private var circularElasticDrawable: AdvancedCircularElasticDrawable? = null

    private var isProgress: Boolean = false

    private var params: AdvancedCircularElasticDrawable.Params =
        AdvancedCircularElasticDrawable.Params(
            headInterpolator = DecelerateInterpolator(0.9f),
            strokeWidth = context.dimen(R.dimen.cycle_progress_stroke_width_8dp),
            duration = 1000L,
            color = context.color(R.color.cycle_progress_foreground_kion)
        )

    private fun initView() {
    }

    fun startAnimation() {
        isProgress = true
        circularElasticDrawable?.start()
    }

    fun stopAnimation() {
        isProgress = false
        circularElasticDrawable?.stop()
    }

    fun swapAnimation() {
        if (isProgress) stopAnimation() else startAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isProgress) {
            drawProgress(canvas)
        }
    }

    private fun drawProgress(canvas: Canvas) {

        if (circularElasticDrawable == null || circularElasticDrawable?.isRunning == false) {
            val l = 0
            val t = 0
            val r = width
            val b = height

            circularElasticDrawable = AdvancedCircularElasticDrawable(this, params).apply {
                setBounds(l, t, r, b)
                callback = this@AdvancedCircularRunnerView
                start()
            }
        } else {
            circularElasticDrawable?.draw(canvas)
        }
    }
}