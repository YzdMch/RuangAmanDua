package com.ubermensch.ruangamandua.data.local.dao

import com.ubermensch.ruangamandua.data.local.entity.Article
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Update
    suspend fun updateArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles ORDER BY createdAt DESC")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE kategori = :kategori ORDER BY createdAt DESC")
    fun getArticlesByCategory(kategori: String): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE isSaved = 1 ORDER BY createdAt DESC")
    fun getSavedArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :articleId LIMIT 1")
    fun getArticleById(articleId: Int): Flow<Article?>

    @Query("UPDATE articles SET isSaved = :isSaved WHERE id = :articleId")
    suspend fun toggleSaved(articleId: Int, isSaved: Boolean)

    @Query("UPDATE articles SET viewCount = viewCount + 1 WHERE id = :articleId")
    suspend fun incrementViewCount(articleId: Int)

    @Query("SELECT * FROM articles WHERE judul LIKE '%' || :query || '%' OR kategori LIKE '%' || :query || '%'")
    fun searchArticles(query: String): Flow<List<Article>>

    @Query("SELECT COUNT(*) FROM articles")
    suspend fun getArticleCount(): Int
}