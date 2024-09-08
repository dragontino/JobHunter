package com.jobhunter.app.utils

fun String.toTitleCase(): String = StringBuilder(this)
    .apply { this[0] = this[0].titlecaseChar() }
    .toString()