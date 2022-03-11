package dev.barabu.runner.linear

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import dev.barabu.morph.R
import dev.barabu.morph.extentions.color

class LinearRunnerView : View {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    private var linearElasticDrawable: LinearElasticDrawable? = null

    private var isProgress: Boolean = false

    private var params: LinearElasticDrawable.Params = LinearElasticDrawable.Params(
        headInterpolator = DecelerateInterpolator(0.9f),
        tailInterpolator = AccelerateInterpolator(),
        0,
        0,
        1000L,
        context.color(R.color.linear_elastic_foreground)
    )

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        params = params.copy(moveTo = w)
    }

    private fun initView() {
    }

    fun startAnimation() {
        isProgress = true
        linearElasticDrawable?.start()
    }

    fun stopAnimation() {
        isProgress = false
        linearElasticDrawable?.stop()
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

        if (linearElasticDrawable == null || linearElasticDrawable?.isRunning == false) {

            val l = 0
            val t = 0
            val r = width
            val b = height

            linearElasticDrawable = LinearElasticDrawable(this, params).apply {
                setBounds(l, t, r, b)
                callback = this@LinearRunnerView
                start()
            }
        } else {
            linearElasticDrawable?.draw(canvas)
        }
    }
}