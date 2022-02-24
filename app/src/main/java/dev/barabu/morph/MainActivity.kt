package dev.barabu.morph

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import dev.barabu.morph.button.MorphStateController
import dev.barabu.morph.button.MorphingButton
import dev.barabu.morph.databinding.ActivityMainBinding
import dev.barabu.morph.impl.ProgressMorphingButton

class MainActivity : BaseActivity() {

    private var isRectButtonInTextMode = true
    private var isLinearProgressButtonInTextMode = true
    private var isCycleProgressButtonInTextMode = true
    private var isGradientCycleProgressButtonInTextMode = true
    private var isMultiStateButtonInTextMode = true

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.buttonText.apply {
            setOnClickListener {

                if (isRectButtonInTextMode) {
                    morphToSuccess(this)
                } else {
                    morphToRect(this, this@MainActivity.integer(R.integer.animation_duration))
                }
                isRectButtonInTextMode = !isRectButtonInTextMode
            }
            morphToRect(this, 0)
        }

        viewBinding.buttonProgressLinear.apply {
            setOnClickListener {
                if (isLinearProgressButtonInTextMode) {
                    morphToLinearProgress(this)
                } else {
                    morphToRect(this, this@MainActivity.integer(R.integer.animation_duration))
                }
                isLinearProgressButtonInTextMode = !isLinearProgressButtonInTextMode
            }
            morphToRect(this, 0)
        }

        viewBinding.buttonProgressCycle.apply {
            setOnClickListener {

                if (isCycleProgressButtonInTextMode) {
                    morphToCircleProgress(this)
                } else {
                    morphToRect(this, this@MainActivity.integer(R.integer.animation_duration))
                }
                isCycleProgressButtonInTextMode = !isCycleProgressButtonInTextMode
            }
            morphToRect(this, 0)
        }

        viewBinding.buttonProgressCycleGradient.apply {
            setOnClickListener {
                if (isGradientCycleProgressButtonInTextMode) {
                    morphToCircleProgress(this)
                } else {
                    morphToRect(this, this@MainActivity.integer(R.integer.animation_duration))
                }
                isGradientCycleProgressButtonInTextMode = !isGradientCycleProgressButtonInTextMode
            }
            morphToRect(this, 0)
        }

        viewBinding.buttonMultiState.apply {
            setOnClickListener {
                if (isMultiStateButtonInTextMode) {
                    morphToProgressState(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.cycle_progress_background
                        ),
                        ContextCompat.getColor(this@MainActivity, R.color.cycle_progress_foreground)
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToFiniteState(MorphStateController.State.Success)
                    }, 3000)
                } else {
                    morphToRect(this, this@MainActivity.integer(R.integer.animation_duration))
                }
                isMultiStateButtonInTextMode = !isMultiStateButtonInTextMode
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

    private fun morphToLinearProgress(button: MorphStateController) {
        val color =
            ContextCompat.getColor(this, R.color.cycle_progress_clipping)
        val progressBackgroundColor =
            ContextCompat.getColor(this, R.color.cycle_progress_background)
        val progressColor =
            ContextCompat.getColor(this, R.color.cycle_progress_foreground)
        val progressCornerRadius = dimen(R.dimen.corner_radius_4dp)
        val width = dimen(R.dimen.button_rectangle_width).toInt()
        val height = dimen(R.dimen.button_progress_height).toInt()
        val duration = integer(R.integer.animation_duration)

        button.morphToProgress(
            color,
            progressColor,
            progressBackgroundColor,
            progressCornerRadius,
            width,
            height,
            duration
        )
    }

    private fun morphToCircleProgress(button: MorphStateController) {
        val color =
            ContextCompat.getColor(this, R.color.cycle_progress_clipping)
        val progressBackgroundColor =
            ContextCompat.getColor(this, R.color.cycle_progress_background)
        val progressColor =
            ContextCompat.getColor(this, R.color.cycle_progress_foreground)
        val progressCornerRadius = dimen(R.dimen.corner_radius_56dp)
        val width = dimen(R.dimen.button_rectangle_height).toInt()
        val height = dimen(R.dimen.button_rectangle_height).toInt()
        val duration = integer(R.integer.animation_duration)

        button.morphToProgress(
            color,
            progressColor,
            progressBackgroundColor,
            progressCornerRadius,
            width,
            height,
            duration
        )
    }

    private fun ProgressMorphingButton.morphToProgressState(
        startColor: Int,
        endColor: Int
    ) {
        val progressCornerRadius = dimen(R.dimen.corner_radius_56dp)
        val width = dimen(R.dimen.button_rectangle_height).toInt()
        val height = dimen(R.dimen.button_rectangle_height).toInt()
        val duration = integer(R.integer.animation_duration)

        morphToProgress(
            Color.TRANSPARENT,
            startColor,
            endColor,
            progressCornerRadius,
            width,
            height,
            duration
        )
    }

    private fun ProgressMorphingButton.morphToFiniteState(state: MorphStateController.State) {
        when (state) {
            MorphStateController.State.Success -> {
                morphToState(
                    ContextCompat.getColor(this@MainActivity, android.R.color.holo_green_light),
                    ContextCompat.getColor(this@MainActivity, android.R.color.holo_green_dark),
                    dimen(R.dimen.corner_radius_56dp),
                    dimen(R.dimen.button_square).toInt(),
                    dimen(R.dimen.button_square).toInt(),
                    300,
                    R.drawable.ic_done
                )
            }
            MorphStateController.State.Error -> {
                morphToState(
                    ContextCompat.getColor(this@MainActivity, android.R.color.holo_red_light),
                    ContextCompat.getColor(this@MainActivity, android.R.color.holo_red_dark),
                    dimen(R.dimen.corner_radius_56dp),
                    dimen(R.dimen.button_square).toInt(),
                    dimen(R.dimen.button_square).toInt(),
                    300,
                    R.drawable.ic_error
                )
            }
            else -> {

            }
        }
    }
}