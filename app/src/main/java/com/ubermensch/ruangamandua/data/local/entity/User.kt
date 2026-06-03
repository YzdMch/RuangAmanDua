package com.ubermensch.ruangamandua.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaLengkap: String,
    val email: String,
    val password: String,
    val namaInstansi: String,
    val createdAt: Long = System.currentTimeMillis()
)