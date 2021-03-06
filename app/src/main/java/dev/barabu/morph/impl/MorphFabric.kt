package dev.barabu.morph.impl

import android.content.Context
import android.graphics.Color
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import dev.barabu.morph.R
import dev.barabu.morph.button.AnchorIcon
import dev.barabu.morph.button.MorphingButton
import dev.barabu.morph.extentions.color
import dev.barabu.morph.extentions.dimen
import dev.barabu.morph.extentions.integer
import dev.barabu.morph.extentions.string

/**
 * NOTE: Если используется градиентный прогресс (gradient sweep), то цвет меняется по кругу CW
 * от градуса 0 (startColor) до градуса 360 (endColor). Канву мы также вращаем CW, то есть
 * получается, что цвет движется CW и endColor движется в "голове". "Голова" вращения должна
 * быть более выраженного цвета, чем "хвост". Здесь primaryColor - это цвет "головы", а
 * secondaryColor - цвет "хвоста".
 *
 *
 * NOTE: На старом телефоне с Android 8.0 обнаружилась проблема неправильной отрисовки strokeColor
 * в ряде случаев. Контур stroke оказывался не внутри drawable, а центрировался по границе View,
 * то есть половина толщины контура вылезала за пределы и некрасиво обрезалась на круглых кнопках.
 * В качестве решения стал указывать одинаковую strokeWidth для всех трех состояний кнопки -
 * начальном, прогресс и результат. Только цвет контура менял, но не толщину.
 */
class MorphFabric(private val context: Context) {

    //region Progress
    fun progressLinear(
        @IntegerRes durationId: Int = R.integer.morph_duration_default
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_2dp),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.linear_progress_height).toInt(),
            colorPrimary = color(R.color.linear_progress_primary),
            colorSecondary = color(R.color.linear_progress_secondary),
            colorBackground = color(R.color.linear_progress_background),
            strokeColor = Color.TRANSPARENT,
            strokeWidth = 0,
            duration = integer(durationId)
        )
    }

    fun progressCircularGreenArcTransparentBack(
        @IntegerRes durationId: Int = R.integer.morph_duration_default
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorPrimary = color(R.color.cycle_progress_foreground),
            colorSecondary = color(R.color.cycle_progress_background),
            colorBackground = Color.TRANSPARENT,
            strokeColor = color(R.color.cycle_progress_background),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_2dp).toInt(),
            duration = integer(durationId),
            ringPadding = 0f
        )
    }

    fun progressCircularGradientGreenWhite(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DimenRes paddingId: Int = R.dimen.cycle_progress_padding_0dp
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorPrimary = color(R.color.cycle_progress_tail),    // tail
            colorSecondary = color(R.color.cycle_progress_head),  // head
            colorBackground = Color.TRANSPARENT,
            strokeColor = Color.TRANSPARENT,
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_half_dp).toInt(),
            duration = integer(durationId), // duration для морфа, а не прогресса
            ringPadding = dimen(paddingId)
        )
    }

    fun progressCircularGradientWhitePink(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DimenRes paddingId: Int = R.dimen.cycle_progress_padding_2dp
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorPrimary = color(R.color.ds_mts_pink),      // tail
            colorSecondary = Color.WHITE,                   // head
            colorBackground = color(R.color.ds_mts_red),
            strokeColor = color(R.color.ds_mts_red),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_half_dp).toInt(),
            duration = integer(durationId), // duration для морфа, а не прогресса
            ringPadding = dimen(paddingId)
        )
    }

    fun progressCircularGradientNavyWhite(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DimenRes paddingId: Int = R.dimen.cycle_progress_padding_0dp
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorPrimary = Color.WHITE,                     // tail
            colorSecondary = color(R.color.navy),           // head
            colorBackground = Color.TRANSPARENT,
            strokeColor = color(R.color.navy_semi_transparent),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = integer(durationId), // duration для морфа, а не прогресса
            ringPadding = dimen(paddingId)
        )
    }

    fun progressCircularRedBackWhiteGradientDots(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorPrimary = color(R.color.ds_mts_red),   // tail
            colorSecondary = Color.WHITE,               // head
            colorBackground = color(R.color.ds_mts_red),
            strokeColor = color(R.color.ds_mts_red),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = integer(durationId)  // duration для морфа, а не прогресса
        )
    }

    fun progressCircularWhiteBackRedGradientDots(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DimenRes paddingId: Int = R.dimen.cycle_progress_padding_0dp
    ): MorphingButton.ProgressParams = with(context) {
        check(integer(durationId) > 0)

        MorphingButton.ProgressParams(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorPrimary = Color.WHITE,                     // tail
            colorSecondary = color(R.color.ds_mts_red),     // head
            colorBackground = color(R.color.white),
            strokeColor = Color.TRANSPARENT,
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = integer(durationId), // duration для морфа, а не прогресса
            ringPadding = dimen(paddingId)
        )
    }
    //endregion

    //region Contained Buttons
    fun containedTextButtonDefault(
        @IntegerRes durationId: Int = R.integer.morph_duration_0,
        @StringRes stringId: Int = R.string.text_button
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.default_corner_radius),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = color(R.color.default_color_normal),
            colorPressed = color(R.color.default_color_pressed),
            colorText = Color.WHITE,
            strokeColor = Color.TRANSPARENT,
            strokeWidth = 0,
            icon = AnchorIcon(),
            duration = integer(durationId),
            text = string(stringId)
        )
    }

    fun containedFabRedBackIconRight(
        @IntegerRes durationId: Int = R.integer.morph_duration_0,
        @StringRes stringId: Int = R.string.text_button,
        @DrawableRes iconId: Int = R.drawable.ic_send
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = color(R.color.ds_mts_red),
            colorPressed = color(android.R.color.holo_red_dark),
            colorText = color(android.R.color.white),
            strokeColor = color(R.color.ds_mts_red),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = context.integer(durationId),
            text = string(stringId),
            icon = AnchorIcon(0, 0, iconId, 0)
        )
    }
    //endregion

    //region Outlined Buttons
    fun outlinedTextButtonWhiteBackBlueStroke(
        @IntegerRes durationId: Int = R.integer.morph_duration_0,
        @StringRes stringId: Int = R.string.text_button
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.default_corner_radius),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = Color.TRANSPARENT,
            colorPressed = Color.TRANSPARENT,
            colorText = color(android.R.color.holo_blue_dark),
            strokeColor = color(android.R.color.holo_blue_dark),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = context.integer(durationId),
            text = string(stringId)
        )
    }

    fun outlinedFabWhiteBackRedStroke(
        @IntegerRes durationId: Int = R.integer.morph_duration_0,
        @StringRes stringId: Int = R.string.text_button,
        @DrawableRes iconId: Int = R.drawable.ic_send_red
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_rectangle_width).toInt(),
            height = dimen(R.dimen.button_rectangle_height).toInt(),
            colorNormal = Color.WHITE,
            colorPressed = Color.WHITE,
            colorText = color(R.color.ds_mts_red),
            strokeColor = color(R.color.ds_mts_red),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = context.integer(durationId),
            text = string(stringId),
            icon = AnchorIcon(0, 0, iconId, 0)
        )
    }
    //endregion

    //region Round Buttons
    fun containedIconButtonGreenBackWhiteIcon(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DrawableRes iconId: Int = R.drawable.ic_done
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.holo_green_light),
            colorPressed = color(android.R.color.holo_green_dark),
            colorText = Color.TRANSPARENT,
            strokeColor = Color.TRANSPARENT,
            strokeWidth = 0,
            duration = context.integer(durationId),
            icon = AnchorIcon(l = iconId)
        )
    }

    fun containedIconButtonRedBackWhiteIcon(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DrawableRes iconId: Int = R.drawable.ic_done
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.holo_red_light),
            colorPressed = color(android.R.color.holo_red_dark),
            colorText = Color.TRANSPARENT,
            strokeColor = Color.TRANSPARENT,
            strokeWidth = 0,
            duration = context.integer(durationId),
            icon = AnchorIcon(l = iconId)
        )
    }

    fun outlinedIconButtonBrownBackOrangeStroke(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DrawableRes iconId: Int = R.drawable.ic_done_orange
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(R.color.brown_normal),
            colorPressed = color(R.color.brown_dark),
            colorText = Color.TRANSPARENT,
            strokeColor = color(android.R.color.holo_orange_dark),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = context.integer(durationId),
            icon = AnchorIcon(l = iconId)
        )
    }

    fun outlinedIconButtonBlueBackNavyStroke(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DrawableRes iconId: Int = R.drawable.ic_done_navy
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.holo_blue_light),
            colorPressed = color(android.R.color.holo_blue_dark),
            colorText = Color.TRANSPARENT,
            strokeColor = color(R.color.navy),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = context.integer(durationId),
            icon = AnchorIcon(l = iconId)
        )
    }

    fun outlinedIconButtonWhiteBackRedStroke(
        @IntegerRes durationId: Int = R.integer.morph_duration_default,
        @DrawableRes iconId: Int = R.drawable.ic_done_red
    ): MorphingButton.Params = with(context) {
        MorphingButton.Params(
            cornerRadius = dimen(R.dimen.corner_radius_fab),
            width = dimen(R.dimen.button_square).toInt(),
            height = dimen(R.dimen.button_square).toInt(),
            colorNormal = color(android.R.color.white),
            colorPressed = color(android.R.color.white),
            colorText = Color.TRANSPARENT,
            strokeColor = color(R.color.ds_mts_red),
            strokeWidth = dimen(R.dimen.button_outline_stroke_width_3dp).toInt(),
            duration = context.integer(durationId),
            icon = AnchorIcon(l = iconId)
        )
    }
    //endregion
}