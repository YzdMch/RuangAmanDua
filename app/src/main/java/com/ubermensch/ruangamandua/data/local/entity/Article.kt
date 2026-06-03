package com.ubermensch.ruangamandua.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val kategori: String,
    val deskripsi: String,
    val konten: String,
    val waktuBaca: String,
    val viewCount: Int = 0,
    val isSaved: Boolean = false,
    val imageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)