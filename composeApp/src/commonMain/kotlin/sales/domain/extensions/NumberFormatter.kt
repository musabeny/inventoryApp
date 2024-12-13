package sales.domain.extensions

fun Double.roundToTwoDecimals(): Double {
    return kotlin.math.round(this * 100) / 100.0
}

fun Double.removeUnnecessaryDecimals(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString() // Convert to integer if no decimal part
    } else {
        this.toString() // Keep as is if there are meaningful decimal places
    }
}