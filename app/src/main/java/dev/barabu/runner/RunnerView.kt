package dev.barabu.runner

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import dev.barabu.morph.button.AnchorIcon
import dev.barabu.morph.button.MorphingAnimation
import dev.barabu.morph.button.MorphingButton

class RunnerView: View, Drawable.Callback {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }


    fun initView() {

    }

    data class Params(
        val cornerRadius: Float,
        val width: Int,
        val height: Int,
        @ColorInt val colorForeground: Int,
        @ColorInt val colorBackground: Int,
        @ColorInt val strokeColor: Int = Color.TRANSPARENT,
        val strokeWidth: Int = STROKE_WIDTH_ZERO,
        val duration: Int = 0,
        val text: String? = null,
        val icon: AnchorIcon = AnchorIcon(),
        var animationListener: MorphingAnimation.Listener? = null
    )

    companion object {
        private const val STROKE_WIDTH_ZERO = 0

    }

}