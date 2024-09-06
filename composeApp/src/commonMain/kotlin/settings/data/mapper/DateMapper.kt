package settings.data.mapper

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val format =instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${format.dayOfMonth} / ${format.monthNumber} / ${format.year}"
}

