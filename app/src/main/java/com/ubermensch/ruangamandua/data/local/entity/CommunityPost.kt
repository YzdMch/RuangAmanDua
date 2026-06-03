package com.ubermensch.ruangamandua.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_posts")
data class CommunityPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val authorName: String,
    val topic: String,
    val judul: String,
    val konten: String,
    val isAnonim: Boolean = false,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isLiked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)