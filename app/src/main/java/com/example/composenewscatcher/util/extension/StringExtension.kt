package com.example.composenewscatcher.util.extension

fun String.firstLetterCapitalize(): String {
    return this.firstOrNull()?.uppercase()?.plus(this.drop(1)) ?: ""
}