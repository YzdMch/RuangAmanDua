package com.ubermensch.ruangamandua.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val reportId: String,
    val jenisBullying: String,
    val tanggalKejadian: String,
    val lokasi: String,
    val deskripsi: String,
    val isAnonim: Boolean = true,
    val status: String = "Terkirim",
    val buktiPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)