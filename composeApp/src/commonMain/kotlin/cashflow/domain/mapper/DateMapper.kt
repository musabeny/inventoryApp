package cashflow.domain.mapper

import androidx.compose.ui.text.toLowerCase
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

fun LocalDate.formatToMonthYear():String{
    return "${this.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }}, ${this.year}"
}

fun LocalDate.isAfter( localDate: LocalDate):Boolean{
  return  this > localDate
}


 fun ClosedRange<LocalDate>.iterator() : Iterator<LocalDate>{
    return object: Iterator<LocalDate> {
        private var next = this@iterator.start
        private val finalElement = this@iterator.endInclusive
        private var hasNext = !next.isAfter(this@iterator.endInclusive)
        override fun hasNext(): Boolean = hasNext

        override fun next(): LocalDate {
            val value = next

            if(value == finalElement) {

                hasNext = false

            }

            else {

                next = next.plus(DatePeriod(days = 1))

            }

            return value

        }

    }

}

fun ClosedRange<LocalDate>.toDateMonthYearFormat():String{
    val start = this.start
    val end  = this.endInclusive
    return if(start == end){
        "${start.dayOfMonth} ${start.shortOrFullMontName(true)} ${start.year}"
    }else{
        "${start.dayOfMonth} ${start.shortOrFullMontName(false)} ${start.year} - ${end.dayOfMonth} ${end.shortOrFullMontName(false)} ${end.year} "
    }

}

fun LocalDate.shortOrFullMontName(isFullMonthName:Boolean):String{
    val month = this.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }
    return if(isFullMonthName){
        month
    }else{
        month.take(3)
    }

}