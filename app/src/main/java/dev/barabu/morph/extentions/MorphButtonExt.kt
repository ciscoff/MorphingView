package dev.barabu.morph.extentions

import android.graphics.Color
import androidx.annotation.DrawableRes
import dev.barabu.morph.R
import dev.barabu.morph.button.*

fun MorphingButton.morphTo(params: MorphingButton.Params) {
    morph(params)
}

fun ProgressMorphingButton.prepare(params: MorphingButton.Params) {
    morph(params)
}

fun ProgressMorphingButton.progress(progressParams: MorphingButton.ProgressParams) {
    morphToProgress(progressParams)
}

fun ProgressMorphingButton.result(params: MorphingButton.Params) {
    morphToResult(params)
}


/**
 * Все "тяжелые" методы вынес сюда. Во внешнем коде будут создаваться специфические функции
 * с "говорящими" названиями.
 */
fun MorphingButton.morphToRect(
    duration: Int,
    text: String = context.getString(R.string.text_button),
    colorNormal: Int = context.color(android.R.color.holo_blue_light),
    colorPressed: Int = context.color(android.R.color.holo_blue_dark),
    strokeColor: Int = Color.TRANSPARENT,
    strokeWidth: Int = 0
) {
    val params = MorphingButton.Params(
        cornerRadius = context.dimen(R.dimen.corner_radius_2dp),
        width = context.dimen(R.dimen.button_rectangle_width).toInt(),
        height = context.dimen(R.dimen.button_rectangle_height).toInt(),
        colorNormal = colorNormal,
        colorPressed = colorPressed,
        colorText = Color.WHITE,
        strokeColor = strokeColor,
        strokeWidth = strokeWidth,
        duration = duration,
        text = text
    )
    morph(params)
}

fun MorphingButton.morphToFabRect(
    duration: Int,
    text: String = context.getString(R.string.text_button),
    colorNormal: Int = context.color(R.color.ds_mts_red),
    colorPressed: Int = context.color(android.R.color.holo_red_dark),
    strokeColor: Int = Color.TRANSPARENT,
    strokeWidth: Int = 0
) {
    val params = MorphingButton.Params(
        cornerRadius = context.dimen(R.dimen.corner_radius_fab),
        width = context.dimen(R.dimen.button_rectangle_width).toInt(),
        height = context.dimen(R.dimen.button_rectangle_height).toInt(),
        colorNormal = colorNormal,
        colorPressed = colorPressed,
        colorText = Color.WHITE,
        duration = duration,
        strokeColor = strokeColor,
        strokeWidth = strokeWidth,
        text = text,
        icon = AnchorIcon(0, 0, R.drawable.ic_send, 0)
    )
    morph(params)
}

fun MorphingButton.morphToSuccess(
    colorNormal: Int = context.color(android.R.color.holo_green_light),
    colorPressed: Int = context.color(android.R.color.holo_green_dark),
    strokeColor: Int = Color.TRANSPARENT,
    strokeWidth: Int = 0,
    @DrawableRes iconId: Int = R.drawable.ic_done

) {
    val params = MorphingButton.Params(
        cornerRadius = context.dimen(R.dimen.corner_radius_fab),
        width = context.dimen(R.dimen.button_square).toInt(),
        height = context.dimen(R.dimen.button_square).toInt(),
        colorNormal = colorNormal,
        colorPressed = colorPressed,
        colorText = Color.WHITE,
        duration = context.integer(R.integer.animation_duration),
        strokeColor = strokeColor,
        strokeWidth = strokeWidth,
        icon = AnchorIcon(l = iconId)
    )
    morph(params)
}

fun ProgressMorphingButton.startLinearProgress() {
    val color = context.color(R.color.cycle_progress_clipping)
    val progressForegroundColor = context.color(R.color.cycle_progress_foreground)
    val progressBackgroundColor = context.color(R.color.cycle_progress_background)
    val progressCornerRadius = context.dimen(R.dimen.corner_radius_4dp)
    val width = context.dimen(R.dimen.button_rectangle_width).toInt()
    val height = context.dimen(R.dimen.linear_progress_height).toInt()
    val duration = context.integer(R.integer.animation_duration)

    morphToProgress(
        color,
        progressForegroundColor,
        progressBackgroundColor,
        progressCornerRadius,
        width,
        height,
        duration,
        0f,
        Color.TRANSPARENT,
        0
    )
}

/**
 * Если используется градиентный прогресс (gradient sweep), то цвет меняется по кругу CW
 * от градуса 0 (startColor) до градуса 360 (endColor). Канву мы также вращаем CW, то есть
 * получается, что цвет движется CW и endColor движется в "голове". "Голова" вращения должна
 * быть более выраженного цвета, чем "хвост". Здесь primaryColor - это цвет "головы", а
 * secondaryColor - цвет "хвоста".
 */
fun ProgressMorphingButton.startCircularProgress(
    primaryColor: Int,
    secondaryColor: Int,
    backgroundColor: Int,
    padding: Float
) {
    val progressCornerRadius = context.dimen(R.dimen.corner_radius_fab)
    val width = context.dimen(R.dimen.button_square).toInt()
    val height = context.dimen(R.dimen.button_square).toInt()
    val duration = context.integer(R.integer.animation_duration)

    // Это коррекция для градиентного прогресса
    val (headColor, tailColor) = if (this is Gradient) {
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
        padding,
        Color.TRANSPARENT,
        0
    )
}

fun ProgressMorphingButton.stopCircularProgress(
    result: OpResult,
    successColorNormal: Int,
    successColorPressed: Int,
    failureColorNormal: Int,
    failureColorPressed: Int,
    strokeColor: Int = Color.TRANSPARENT,
    strokeWidth: Int = 0,
    @DrawableRes successIcon: Int = R.drawable.ic_done,
    @DrawableRes failureIcon: Int = R.drawable.ic_error
) {
    when (result) {
        OpResult.Success -> {
            morphToResult(
                colorNormal = successColorNormal,
                colorPressed = successColorPressed,
                cornerRadius = context.dimen(R.dimen.corner_radius_fab),
                width = context.dimen(R.dimen.button_square).toInt(),
                height = context.dimen(R.dimen.button_square).toInt(),
                duration = 300,
                iconId = successIcon,
                strokeColor = strokeColor,
                strokeWidth = strokeWidth
            )
        }
        OpResult.Failure -> {
            morphToResult(
                colorNormal = failureColorNormal,
                colorPressed = failureColorPressed,
                cornerRadius = context.dimen(R.dimen.corner_radius_fab),
                width = context.dimen(R.dimen.button_square).toInt(),
                height = context.dimen(R.dimen.button_square).toInt(),
                duration = 300,
                iconId = failureIcon,
                strokeColor = strokeColor,
                strokeWidth = strokeWidth
            )
        }
    }
}

