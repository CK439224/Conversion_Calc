package com.numconversion.app.domain.conversion

/** Length units offered in both conversion dropdowns, in the order shown on the wireframe. */
enum class MeasurementUnit(val displayLabel: String) {
    FT_IN("Ft In"),
    IN("In"),
    M("M"),
    MM("mm"),
    FRACTION("Fraction")
}
