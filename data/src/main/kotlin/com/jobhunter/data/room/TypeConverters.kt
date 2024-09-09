package com.jobhunter.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ListConverter {
    @TypeConverter
    fun List<String>.convertToString(): String = Gson().toJson(this)

    @TypeConverter
    fun String.toList(): List<String> = Gson().fromJson<Array<String>>(
        this,
        TypeToken.getArray(String::class.java).type
    ).toList()
}


object LocalDateConverter {
    private const val DATE_PATTERN = "yyyy-MM-dd"

    @TypeConverter
    fun String.toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return LocalDate.parse(this, formatter)
    }

    @TypeConverter
    fun LocalDate.convertToString(): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return format(formatter)
    }
}