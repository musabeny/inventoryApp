package core.extensions

fun String.addZeroBefore():String{
    return if(this.length == 1){
        "0$this"
    }else this
}