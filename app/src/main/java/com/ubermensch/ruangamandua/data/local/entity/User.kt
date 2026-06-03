package com.ubermensch.ruangamandua.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaLengkap: String,
    val email: String,
    val password: String,
    val namaInstansi: String,
    val createdAt: Long = System.currentTimeMillis()
)