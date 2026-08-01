package com.numconversion.app.domain.conversion

import androidx.annotation.StringRes
import com.numconversion.app.R

/** A group of [MeasurementUnit]s that convert amongst themselves (never across categories). */
enum class UnitCategory(@StringRes val displayNameRes: Int) {
    LENGTH(R.string.category_length),
    WEIGHT(R.string.category_weight),
    VOLUME(R.string.category_volume),
    AREA(R.string.category_area),
    TEMPERATURE(R.string.category_temperature),
    SPEED(R.string.category_speed),
    TIME(R.string.category_time)
}
