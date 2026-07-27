package com.numconversion.app.domain.conversion

import androidx.annotation.StringRes
import com.numconversion.app.R

/**
 * User-selectable rounding resolution for the converter's FRACTION target unit. 1/16" suits rough
 * carpentry, 1/32" trim/cabinet work, 1/64" machining/tape-measure-precision work (the app's
 * original hardcoded default).
 */
enum class FractionPrecision(val denominator: Long, val id: String, @StringRes val displayNameRes: Int) {
    SIXTEENTH(16L, "sixteenth", R.string.precision_sixteenth),
    THIRTY_SECOND(32L, "thirty_second", R.string.precision_thirty_second),
    SIXTY_FOURTH(64L, "sixty_fourth", R.string.precision_sixty_fourth);

    companion object {
        val Default = SIXTY_FOURTH

        fun fromId(id: String?): FractionPrecision = entries.find { it.id == id } ?: Default
    }
}
