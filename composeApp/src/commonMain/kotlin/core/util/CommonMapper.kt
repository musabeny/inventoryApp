package core.util

fun String.isAllDigits():Boolean{
   return this.toCharArray().all { it.isDigit() }
}