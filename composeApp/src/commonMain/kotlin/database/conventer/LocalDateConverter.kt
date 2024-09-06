package database.conventer

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate): Int {
        return date.toEpochDays()
    }

    @TypeConverter
    fun toLocalDate(epochDays: Int): LocalDate {
        return LocalDate.fromEpochDays(epochDays)
    }
}