package settings.domain.useCase.productUseCase

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateMillsToDate {
    operator fun invoke(date:Long): String {
        val instant = Instant.fromEpochMilliseconds(date)

       val format =instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${format.dayOfMonth} / ${format.monthNumber} / ${format.year}"
    }
}