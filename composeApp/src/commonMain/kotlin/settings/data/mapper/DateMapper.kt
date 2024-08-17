package settings.data.mapper

import cashflow.domain.model.YearMonth
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val format =instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${format.dayOfMonth} / ${format.monthNumber} / ${format.year}"
}

