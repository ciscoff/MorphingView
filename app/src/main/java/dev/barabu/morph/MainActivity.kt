package dev.barabu.morph

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import dev.barabu.morph.button.*
import dev.barabu.morph.databinding.ActivityMainBinding
import dev.barabu.morph.extentions.*
import dev.barabu.morph.impl.MorphFabric

class MainActivity : AppCompatActivity() {

    private var isRectButtonInTextMode = true
    private var isLinearProgressButtonInTextMode = true
    private var isCycleProgressButtonInTextMode = true
    private var isMultiStateButtonInTextMode = true
    private var isMtsButtonInTextMode = true
    private var isDottedButtonInTextMode = true
    private var isLineAndDottedButtonInTextMode = true

    private val morphFabric: MorphFabric = MorphFabric(this)

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        // Простой morph формы и цвета
        viewBinding.buttonText.apply {
            setOnClickListener {
                if (isRectButtonInTextMode) {
                    morphTo(morphFabric.outlinedIconButtonBrownBackOrangeStroke())
                } else {
                    morphTo(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isRectButtonInTextMode = !isRectButtonInTextMode
            }
            morphTo(morphFabric.containedTextButtonDefault())
        }

        // Горизонтальная линия прогресса
        viewBinding.buttonLinearProgress.apply {
            setOnClickListener {
                if (isLinearProgressButtonInTextMode) {
                    progress(morphFabric.progressLinear())
                } else {
                    morphTo(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isLinearProgressButtonInTextMode = !isLinearProgressButtonInTextMode
            }
            morphTo(morphFabric.containedTextButtonDefault())
        }

        // Круговой прогресс из одноцветного сегмента
        viewBinding.buttonCircularColoredProgress.apply {
            setOnClickListener {
                if (isCycleProgressButtonInTextMode) {
                    // todo Progress
                    progress(morphFabric.progressCircularGreenArcTransparentBack())
                    // todo Result
                    Handler(Looper.getMainLooper()).postDelayed({
                        result(morphFabric.containedIconButtonGreenBackWhiteIcon())
                    }, 2000)
                } else {
                    prepare(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isCycleProgressButtonInTextMode = !isCycleProgressButtonInTextMode
            }
            prepare(morphFabric.containedTextButtonDefault())
        }

        // Круговой прогресс из градиентной окружности по кромке
        viewBinding.buttonCircularGradientProgress.apply {
            setOnClickListener {
                if (isMultiStateButtonInTextMode) {
                    // todo Progress
                    progress(morphFabric.progressCircularGradient())
                    Handler(Looper.getMainLooper()).postDelayed({
                        result(morphFabric.containedIconButtonGreenBackWhiteIcon())
                    }, 2000)
                } else {
                    prepare(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isMultiStateButtonInTextMode = !isMultiStateButtonInTextMode
            }
            prepare(morphFabric.containedTextButtonDefault())
        }
//
//        // Круговой прогресс из градиентной окружности внутри View
//        viewBinding.buttonMts.apply {
//
//            setOnClickListener {
//                if (isMtsButtonInTextMode) {
//                    startCircularProgress(
//                        color(R.color.ds_mts_pink),
//                        Color.WHITE,
//                        color(R.color.ds_mts_red),
//                        dimen(R.dimen.cycle_progress_padding_2dp)
//                    )
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        stopCircularProgressWithDarkBlueIconAndBorder()
//                    }, 2000)
//
//                } else {
//                    morphToFabRect(
//                        integer(R.integer.animation_duration),
//                        string(R.string.button_text_circle_gradient_inside)
//                    )
//                }
//                isMtsButtonInTextMode = !isMtsButtonInTextMode
//            }
//            morphToFabRect(0, string(R.string.button_text_circle_gradient_inside))
//        }
//
//        // Круговой прогресс из градиентной окружности точек внутри View
//        viewBinding.buttonDotted.apply {
//            setOnClickListener {
//                if (isDottedButtonInTextMode) {
//                    startCircularProgress(
//                        primaryColor = Color.WHITE,
//                        secondaryColor = color(R.color.ds_mts_red),
//                        backgroundColor = color(R.color.ds_mts_red),
//                        padding = dimen(R.dimen.cycle_progress_padding_4dp)
//                    )
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        stopCircularProgress(
//                            OpResult.Success,
//                            color(android.R.color.holo_blue_light),
//                            color(android.R.color.holo_blue_dark),
//                            color(android.R.color.holo_red_light),
//                            color(android.R.color.holo_red_dark),
//                        )
//                    }, 2000)
//                } else {
//                    morphToFabRect(
//                        integer(R.integer.animation_duration),
//                        string(R.string.button_text_circle_dots_gradient)
//                    )
//                }
//                isDottedButtonInTextMode = !isDottedButtonInTextMode
//            }
//            morphToFabRect(0, string(R.string.button_text_circle_dots_gradient))
//        }
//
//        viewBinding.buttonDottedOutlined.apply {
//            setOnClickListener {
//                //button_text_circle_dots_gradient_edge
//                if (isLineAndDottedButtonInTextMode) {
//                    startCircularProgress(
//                        primaryColor = Color.WHITE,
//                        secondaryColor = color(R.color.ds_mts_red),
//                        backgroundColor = color(R.color.ds_mts_red),
//                        padding = dimen(R.dimen.cycle_progress_padding_4dp)
//                    )
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        stopCircularProgress(
//                            OpResult.Success,
//                            color(android.R.color.white),
//                            color(R.color.gray_A200),
//                            color(android.R.color.holo_red_light),
//                            color(android.R.color.holo_red_dark),
//                            color(R.color.ds_mts_red),
//                            dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt(),
//                            R.drawable.ic_done_red,
//                            R.drawable.ic_error
//                        )
//                    }, 2000)
//                } else {
//                    morphToFabRect(
//                        integer(R.integer.animation_duration),
//                        string(R.string.button_text_circle_dots_gradient_edge)
//                    )
//                }
//
//                isLineAndDottedButtonInTextMode = !isLineAndDottedButtonInTextMode
//            }
//            morphToFabRect(0, string(R.string.button_text_circle_dots_gradient_edge))
//        }
    }

    //region ""
    private fun MorphingButton.morphToWhiteRectWithBlueTextAndBorder() {
        morphToRect(
            integer(R.integer.animation_duration),
            string(R.string.button_text_simple),
            color(android.R.color.white),
            color(android.R.color.darker_gray),
            color(android.R.color.holo_blue_dark),
            dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt()
        )
    }

    private fun MorphingButton.morphToSuccessWithOrangeIconAndBorder() {
        morphToSuccess(
            color(R.color.brown_normal),
            color(R.color.brown_dark),
            color(android.R.color.holo_orange_dark),
            dimen(R.dimen.cycle_outline_stroke_width_2dp).toInt(),
            R.drawable.ic_done_orange
        )
    }

    private fun ProgressMorphingButton.stopCircularProgressWithDarkBlueIconAndBorder() {
        stopCircularProgress(
            OpResult.Success,
            color(android.R.color.holo_blue_light),
            color(android.R.color.holo_blue_dark),
            color(android.R.color.holo_red_light),
            color(android.R.color.holo_red_dark),
            color(R.color.navy),
            dimen(R.dimen.cycle_outline_stroke_width_3dp).toInt(),
            R.drawable.ic_done_navy,
            R.drawable.ic_error
        )
    }
    //endregion

}