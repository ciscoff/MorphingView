package dev.barabu.morph

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import dev.barabu.morph.databinding.ActivityMainBinding
import dev.barabu.morph.impl.MorphFabric

class MainActivity : AppCompatActivity() {

    private var isRectButtonInTextMode = true
    private var isLinearProgressButtonInTextMode = true
    private var isCycleProgressButtonInTextMode = true
    private var isMultiStateButtonInTextMode = true
    private var isMtsButtonInTextMode = true
    private var isDottedButtonInTextMode = true
    private var isLineAndDottedButtonInTextMode = true
    private var isLineAndDottedWhiteButtonInTextMode = true

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
                    morph(morphFabric.outlinedIconButtonBrownBackOrangeStroke())
                } else {
                    morph(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isRectButtonInTextMode = !isRectButtonInTextMode
            }
            morph(morphFabric.containedTextButtonDefault())
        }

        // Горизонтальная линия прогресса
        viewBinding.buttonLinearProgress.apply {
            setOnClickListener {
                if (isLinearProgressButtonInTextMode) {
                    morphToProgress(morphFabric.progressLinear())
                } else {
                    morph(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isLinearProgressButtonInTextMode = !isLinearProgressButtonInTextMode
            }
            morph(morphFabric.containedTextButtonDefault())
        }

        // Круговой прогресс из одноцветного сегмента
        viewBinding.buttonCircularColoredProgress.apply {
            setOnClickListener {
                if (isCycleProgressButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularGreenArcTransparentBack())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.containedIconButtonGreenBackWhiteIcon())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isCycleProgressButtonInTextMode = !isCycleProgressButtonInTextMode
            }
            morph(morphFabric.containedTextButtonDefault())
        }

        // Круговой прогресс из градиентной окружности по кромке
        viewBinding.buttonCircularGradientProgress.apply {
            setOnClickListener {
                if (isMultiStateButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularGradientNavyWhite())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.outlinedIconButtonBlueBackNavyStroke())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.outlinedTextButtonWhiteBackBlueStroke(R.integer.morph_duration_default))
                }
                isMultiStateButtonInTextMode = !isMultiStateButtonInTextMode
            }
            morph(morphFabric.outlinedTextButtonWhiteBackBlueStroke())
        }

        // Круговой прогресс из градиентной окружности внутри View
        viewBinding.buttonMts.apply {
            setOnClickListener {
                if (isMtsButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularGradientWhitePink())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.containedIconButtonGreenBackWhiteIcon())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.containedFabRedBackIconRight(R.integer.morph_duration_default))
                }
                isMtsButtonInTextMode = !isMtsButtonInTextMode
            }
            morph(morphFabric.containedFabRedBackIconRight())
        }

        // Круговой прогресс из градиентной окружности точек внутри View
        viewBinding.buttonDotted.apply {
            setOnClickListener {
                if (isDottedButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularWhiteGradientDots())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.containedIconButtonRedBackWhiteIcon())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.containedFabRedBackIconRight(R.integer.morph_duration_default))
                }
                isDottedButtonInTextMode = !isDottedButtonInTextMode
            }
            morph(morphFabric.containedFabRedBackIconRight())
        }

        viewBinding.buttonDottedOutlined.apply {
            setOnClickListener {
                if (isLineAndDottedButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularWhiteGradientDots())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.outlinedIconButtonWhiteBackRedStroke())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.containedFabRedBackIconRight(R.integer.morph_duration_default))
                }
                isLineAndDottedButtonInTextMode = !isLineAndDottedButtonInTextMode
            }
            morph(morphFabric.containedFabRedBackIconRight())
        }

        viewBinding.buttonDottedOutlinedWhite.apply {
            setOnClickListener {
                if (isLineAndDottedWhiteButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularRedGradientDots())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.outlinedIconButtonWhiteBackRedStroke())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.outlinedFabWhiteBackRedStroke(R.integer.morph_duration_default))
                }
                isLineAndDottedWhiteButtonInTextMode = !isLineAndDottedWhiteButtonInTextMode
            }
            morph(morphFabric.outlinedFabWhiteBackRedStroke())
        }
    }

    companion object {
        const val RESULT_DELAY = 2000L
    }
}