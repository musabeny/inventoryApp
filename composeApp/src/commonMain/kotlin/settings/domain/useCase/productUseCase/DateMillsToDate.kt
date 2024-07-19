package settings.domain.useCase.productUseCase

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.toLocalDateTime

class DateMillsToDate {
    operator fun invoke(date:Long): LocalDate {
        val instant = Instant.fromEpochMilliseconds(date)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}