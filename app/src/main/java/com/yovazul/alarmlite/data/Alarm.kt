package com.yovazul.alarmlite.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val label: String = "",
    val soundUri: String = "", // URI of the custom MP3 file
    val duration: Int = 60, // Duration in seconds
    val monday: Boolean = false,
    val tuesday: Boolean = false,
    val wednesday: Boolean = false,
    val thursday: Boolean = false,
    val friday: Boolean = false,
    val saturday: Boolean = false,
    val sunday: Boolean = false
) {
    fun getTimeString(): String {
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minStr = if (minute < 10) "0$minute" else minute.toString()
        return "$hourStr:$minStr"
    }
    
    fun getDaysString(): String {
        val days = mutableListOf<String>()
        if (monday) days.add("L")
        if (tuesday) days.add("M")
        if (wednesday) days.add("X")
        if (thursday) days.add("J")
        if (friday) days.add("V")
        if (saturday) days.add("S")
        if (sunday) days.add("D")
        return if (days.isEmpty()) "Una vez" else days.joinToString(", ")
    }
    
    fun hasAtLeastOneDay(): Boolean {
        return monday || tuesday || wednesday || thursday || friday || saturday || sunday
    }
}
