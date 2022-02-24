package dev.barabu.morph

class BaseClass constructor(val mainInt: Int) {

    constructor(secondInt: Int, mainInt: Int): this(mainInt) {
        println("Secondary constructor mainInt=$mainInt, secondInt=$secondInt")
    }

    init {
        println("Init block mainInt=$mainInt")
    }
}

fun main() {
    val baseClass = BaseClass(1, 2)
}