package com.ubermensch.ruangamandua.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Update
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.OnConflictStrategy
import com.ubermensch.ruangamandua.data.local.entity.CommunityPost
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPost): Long

    @Update
    suspend fun updatePost(post: CommunityPost)

    @Delete
    suspend fun deletePost(post: CommunityPost)

    @Query("SELECT * FROM community_posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<CommunityPost>>

    @Query("SELECT * FROM community_posts WHERE topic = :topic ORDER BY createdAt DESC")
    fun getPostsByTopic(topic: String): Flow<List<CommunityPost>>

    @Query("SELECT * FROM community_posts WHERE id = :postId LIMIT 1")
    fun getPostById(postId: Int): Flow<CommunityPost?>

    @Query("SELECT * FROM community_posts WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPostsByUser(userId: Int): Flow<List<CommunityPost>>

    @Query("UPDATE community_posts SET likeCount = likeCount + 1, isLiked = 1 WHERE id = :postId")
    suspend fun likePost(postId: Int)

    @Query("UPDATE community_posts SET likeCount = likeCount - 1, isLiked = 0 WHERE id = :postId AND likeCount > 0")
    suspend fun unlikePost(postId: Int)

    @Query("SELECT * FROM community_posts WHERE judul LIKE '%' || :query || '%' OR konten LIKE '%' || :query || '%'")
    fun searchPosts(query: String): Flow<List<CommunityPost>>
}