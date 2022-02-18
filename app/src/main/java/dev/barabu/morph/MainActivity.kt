package dev.barabu.morph

import android.os.Bundle
import dev.barabu.morph.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var isRectButtonText = true

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.buttonText.apply {
            setOnClickListener {

                if (isRectButtonText) {
                    morphToSuccess(this)
                } else {
                    morphToRect(this, this@MainActivity.integer(R.integer.animation_duration))
                }
                isRectButtonText = !isRectButtonText
            }
            morphToRect(this, 0)
        }
    }

    private fun morphToRect(button: MorphingButton, duration: Int) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_2dp),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = color(android.R.color.holo_blue_light),
            colorPressed = color(android.R.color.holo_blue_dark),
            duration = duration,
            text = getString(R.string.text_button)
        )
        button.morph(params)
    }

    private fun morphToSuccess(button: MorphingButton) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_56dp),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.holo_green_light),
            colorPressed = color(android.R.color.holo_green_dark),
            duration = integer(R.integer.animation_duration),
            icon = R.drawable.ic_done
        )
        button.morph(params)
    }
}