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

        // 1. Простой morph формы и цвета
        viewBinding.buttonText.apply {
            setOnClickListener {
                if (isRectButtonInTextMode) {
                    morph(morphFabric.outlinedIconButtonBrownBackOrangeStroke())
                } else {
                    morph(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isRectButtonInTextMode = !isRectButtonInTextMode
            }
            elevation = 0f
            morph(morphFabric.containedTextButtonDefault())
        }

        // 2. Горизонтальная линия прогресса
        viewBinding.buttonLinearProgress.apply {
            setOnClickListener {
                if (isLinearProgressButtonInTextMode) {
                    morphToProgress(morphFabric.progressLinear())
                } else {
                    morph(morphFabric.containedTextButtonDefault(R.integer.morph_duration_default))
                }
                isLinearProgressButtonInTextMode = !isLinearProgressButtonInTextMode
            }
            elevation = 0f
            morph(morphFabric.containedTextButtonDefault())
        }

        // 3. Круговой прогресс из одноцветного сегмента
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
            elevation = 0f
            morph(morphFabric.containedTextButtonDefault())
        }

        // 4. Круговой прогресс из градиентной окружности по кромке
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
            elevation = 0f
            morph(morphFabric.outlinedTextButtonWhiteBackBlueStroke())
        }

        // 5. Круговой прогресс из градиентной окружности внутри View
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
            elevation = 0f
            morph(morphFabric.containedFabRedBackIconRight())
        }

        // 6. Круговой прогресс из градиентной окружности точек внутри View
        viewBinding.buttonDotted.apply {
            setOnClickListener {
                if (isDottedButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularRedBackWhiteGradientDots())
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

        // 7.
        viewBinding.buttonDottedOutlined.apply {
            setOnClickListener {
                if (isLineAndDottedButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularRedBackWhiteGradientDots())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.outlinedIconButtonWhiteBackRedStroke())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.containedFabRedBackIconRight(R.integer.morph_duration_default))
                }
                isLineAndDottedButtonInTextMode = !isLineAndDottedButtonInTextMode
            }
            elevation = 0f
            morph(morphFabric.containedFabRedBackIconRight())
        }

        // 8.
        viewBinding.buttonDottedOutlinedWhite.apply {
            setOnClickListener {
                if (isLineAndDottedWhiteButtonInTextMode) {
                    morphToProgress(morphFabric.progressCircularWhiteBackRedGradientDots())
                    Handler(Looper.getMainLooper()).postDelayed({
                        morphToResult(morphFabric.outlinedIconButtonWhiteBackRedStroke())
                    }, RESULT_DELAY)
                } else {
                    morph(morphFabric.outlinedFabWhiteBackRedStroke(R.integer.morph_duration_default))
                }
                isLineAndDottedWhiteButtonInTextMode = !isLineAndDottedWhiteButtonInTextMode
            }
            elevation = 0f
            morph(morphFabric.outlinedFabWhiteBackRedStroke())
        }

        viewBinding.runnerView.apply {
            setOnClickListener {
                swapAnimation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewBinding.runnerView.swapAnimation()
        viewBinding.runnerCircularView.swapAnimation()
    }

    companion object {
        const val RESULT_DELAY = 2000L
    }
}