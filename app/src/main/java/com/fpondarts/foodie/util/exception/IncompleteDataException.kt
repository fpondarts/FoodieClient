package com.fpondarts.foodie.util.exception

import java.lang.RuntimeException

class IncompleteDataException(private val mensaje:String) : RuntimeException(mensaje) {
}