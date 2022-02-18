package dev.barabu.morph

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton

class SimpleButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    init {
        setBackgroundColor(Color.RED)
        setTextColor(Color.WHITE)
        text = this::class.java.simpleName
        gravity = Gravity.CENTER
        isAllCaps = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        layoutParams = layoutParams.apply {
            width = 400
            height = 120
        }
    }

}