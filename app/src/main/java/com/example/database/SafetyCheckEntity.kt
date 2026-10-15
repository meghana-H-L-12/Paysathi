package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "safety_checks")
data class SafetyCheckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val amount: Double,
    val isSafe: Boolean,
    val statusBadge: String,
    val timeLabel: String,
    val payeeOrSender: String,
    val category: String
)
