package dev.barabu.morph

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import dev.barabu.morph.button.AnchorIcon
import dev.barabu.morph.button.MorphingButton
import dev.barabu.morph.button.OpResult
import dev.barabu.morph.button.ProgressMorphingButton
import dev.barabu.morph.databinding.ActivityMainBinding
import dev.barabu.morph.impl.CircularGradientProgressButton

class MainActivity : BaseActivity() {

    private var isRectButtonInTextMode = true
    private var isLinearProgressButtonInTextMode = true
    private var isCycleProgressButtonInTextMode = true
    private var isMultiStateButtonInTextMode = true
    private var isMtsButtonInTextMode = true
    private var isDottedButtonInTextMode = true

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.buttonText.apply {
            setOnClickListener {

                if (isRectButtonInTextMode) {
                    morphToSuccess()
                } else {
                    morphToRect(this@MainActivity.integer(R.integer.animation_duration))
                }
                isRectButtonInTextMode = !isRectButtonInTextMode
            }
            morphToRect(0)
        }

        viewBinding.buttonLinearProgress.apply {
            setOnClickListener {
                if (isLinearProgressButtonInTextMode) {
                    startLinearProgress()
                } else {
                    morphToRect(this@MainActivity.integer(R.integer.animation_duration))
                }
                isLinearProgressButtonInTextMode = !isLinearProgressButtonInTextMode
            }
            morphToRect(0)
        }

        viewBinding.buttonCircularColoredProgress.apply {
            setOnClickListener {

                if (isCycleProgressButtonInTextMode) {
                    startCircularProgress(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.cycle_progress_foreground
                        ),
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.cycle_progress_background
                        ),
                        Color.TRANSPARENT,
                        0f
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Failure,
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_green_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_green_dark
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_dark
                            )
                        )
                    }, 2000)
                } else {
                    morphToRect(this@MainActivity.integer(R.integer.animation_duration))
                }
                isCycleProgressButtonInTextMode = !isCycleProgressButtonInTextMode
            }
            morphToRect(0)
        }

        viewBinding.buttonCircularGradientProgress.apply {
            setOnClickListener {
                if (isMultiStateButtonInTextMode) {
                    startCircularProgress(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.cycle_progress_background
                        ),
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.cycle_progress_foreground
                        ),
                        Color.TRANSPARENT,
                        0f
                    )
                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_green_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_green_dark
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_dark
                            )
                        )
                    }, 2000)
                } else {
                    morphToRect(this@MainActivity.integer(R.integer.animation_duration))
                }
                isMultiStateButtonInTextMode = !isMultiStateButtonInTextMode
            }
            morphToRect(0)
        }

        viewBinding.buttonMts.apply {

            setOnClickListener {
                if (isMtsButtonInTextMode) {
                    startCircularProgress(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.ds_mts_pink
                        ),
                        Color.WHITE,
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.ds_mts_red
                        ),
                        dimen(R.dimen.cycle_progress_padding_2dp)
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_blue_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_blue_dark
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_dark
                            )
                        )
                    }, 2000)

                } else {
                    morphToMtsRect(this@MainActivity.integer(R.integer.animation_duration))
                }
                isMtsButtonInTextMode = !isMtsButtonInTextMode
            }
            morphToMtsRect(0)
        }

        viewBinding.buttonDotted.apply {
            setOnClickListener {
                if(isDottedButtonInTextMode) {
                    startCircularProgress(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.ds_mts_pink
                        ),
                        Color.WHITE,
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.ds_mts_red
                        ),
                        dimen(R.dimen.cycle_progress_padding_4dp)
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        stopCircularProgress(
                            OpResult.Success,
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_blue_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_blue_dark
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_light
                            ),
                            ContextCompat.getColor(
                                this@MainActivity,
                                android.R.color.holo_red_dark
                            )
                        )
                    }, 2000)
                } else {
                    morphToMtsRect(this@MainActivity.integer(R.integer.animation_duration))
                }

                isDottedButtonInTextMode = !isDottedButtonInTextMode
            }
            morphToMtsRect(0)
        }
    }

    private fun MorphingButton.morphToRect(duration: Int) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_2dp),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = color(android.R.color.holo_blue_light),
            colorPressed = color(android.R.color.holo_blue_dark),
            duration = duration,
            text = getString(R.string.text_button)
        )
        morph(params)
    }

    private fun MorphingButton.morphToMtsRect(duration: Int) {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_56dp),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = color(R.color.ds_mts_red),
            colorPressed = color(android.R.color.holo_red_dark),
            duration = duration,
            text = getString(R.string.text_button),
            icon = AnchorIcon(0, 0, R.drawable.ic_send, 0)
        )
        morph(params)
    }

    private fun MorphingButton.morphToSuccess() {
        val params = MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_56dp),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.holo_green_light),
            colorPressed = color(android.R.color.holo_green_dark),
            duration = integer(R.integer.animation_duration),
            icon = AnchorIcon(l = R.drawable.ic_done)
        )
        morph(params)
    }

    private fun ProgressMorphingButton.startLinearProgress() {
        val color =
            ContextCompat.getColor(this@MainActivity, R.color.cycle_progress_clipping)
        val progressForegroundColor =
            ContextCompat.getColor(this@MainActivity, R.color.cycle_progress_foreground)
        val progressBackgroundColor =
            ContextCompat.getColor(this@MainActivity, R.color.cycle_progress_background)
        val progressCornerRadius = dimen(R.dimen.corner_radius_4dp)
        val width = dimen(R.dimen.button_rectangle_width).toInt()
        val height = dimen(R.dimen.button_progress_height).toInt()
        val duration = integer(R.integer.animation_duration)

        morphToProgress(
            color,
            progressForegroundColor,
            progressBackgroundColor,
            progressCornerRadius,
            width,
            height,
            duration
        )
    }

    /**
     * Если используется градиентный прогресс (gradient sweep), то цвет меняется по кругу CW
     * от градуса 0 (startColor) до градуса 360 (endColor). Канву мы также вращаем CW, то есть
     * получается, что цвет движется CW и endColor движется в "голове". "Голова" вращения должна
     * быть более выраженного цвета, чем "хвост". Здесь primaryColor - это цвет "головы", а
     * secondaryColor - цвет "хвоста".
     */
    private fun ProgressMorphingButton.startCircularProgress(
        primaryColor: Int,
        secondaryColor: Int,
        backgroundColor: Int,
        padding: Float
    ) {
        val progressCornerRadius = dimen(R.dimen.corner_radius_56dp)
        val width = dimen(R.dimen.button_rectangle_height).toInt()
        val height = dimen(R.dimen.button_rectangle_height).toInt()
        val duration = integer(R.integer.animation_duration)

        // Это коррекция для градиентного прогресса
        val (headColor, tailColor) = if (this is CircularGradientProgressButton) {
            secondaryColor to primaryColor
        } else {
            primaryColor to secondaryColor
        }

        morphToProgress(
            backgroundColor,
            headColor,
            tailColor,
            progressCornerRadius,
            width,
            height,
            duration,
            padding
        )
    }

    private fun ProgressMorphingButton.stopCircularProgress(
        result: OpResult,
        successColorNormal: Int,
        successColorPressed: Int,
        failureColorNormal: Int,
        failureColorPressed: Int,
    ) {
        when (result) {
            OpResult.Success -> {
                morphToFinish(
                    successColorNormal,
                    successColorPressed,
                    dimen(R.dimen.corner_radius_56dp),
                    dimen(R.dimen.button_square).toInt(),
                    dimen(R.dimen.button_square).toInt(),
                    300,
                    R.drawable.ic_done
                )
            }
            OpResult.Failure -> {
                morphToFinish(
                    failureColorNormal,
                    failureColorPressed,
                    dimen(R.dimen.corner_radius_56dp),
                    dimen(R.dimen.button_square).toInt(),
                    dimen(R.dimen.button_square).toInt(),
                    300,
                    R.drawable.ic_error
                )
            }
        }
    }
}