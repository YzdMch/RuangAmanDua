package com.ubermensch.ruangamandua.data.local.repository

import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class RuangAmanRepository(private val db: RuangAmanDatabase) {
    // USER
    suspend fun register(user: User): Long = db.userDao().insertUser(user)
    suspend fun login(email: String, password: String): User? = db.userDao().login(email, password)
    suspend fun isEmailExists(email: String): Boolean = db.userDao().isEmailExists(email) > 0
    fun getUserById(id: Int): Flow<User?> = db.userDao().getUserById(id)
    suspend fun getUserByIdSync(id: Int): User? = db.userDao().getUserByIdSync(id)
    suspend fun updateUser(user: User) = db.userDao().updateUser(user)

    // REPORTS
    suspend fun createReport(report: Report): Long = db.reportDao().insertReport(report)
    suspend fun updateReport(report: Report) = db.reportDao().updateReport(report)
    suspend fun deleteReport(report: Report) = db.reportDao().deleteReport(report)
    fun getReportsByUser(userId: Int): Flow<List<Report>> = db.reportDao().getReportsByUser(userId)
    fun getReportById(id: Int): Flow<Report?> = db.reportDao().getReportById(id)
    fun getLaporanSelesai(userId: Int): Flow<Int> = db.reportDao().getLaporanSelesaiCount(userId)
    fun getLaporanProses(userId: Int): Flow<Int> = db.reportDao().getLaporanProsesCount(userId)

    // ARTICLES
    fun getAllArticles(): Flow<List<Article>> = db.articleDao().getAllArticles()
    fun getArticlesByCategory(kategori: String): Flow<List<Article>> = db.articleDao().getArticlesByCategory(kategori)
    fun getSavedArticles(): Flow<List<Article>> = db.articleDao().getSavedArticles()
    fun getArticleById(id: Int): Flow<Article?> = db.articleDao().getArticleById(id)
    fun searchArticles(query: String): Flow<List<Article>> = db.articleDao().searchArticles(query)
    suspend fun toggleSaveArticle(id: Int, isSaved: Boolean) = db.articleDao().toggleSaved(id, isSaved)
    suspend fun incrementViewCount(id: Int) = db.articleDao().incrementViewCount(id)

    // COMMUNITY
    fun getAllPosts(): Flow<List<CommunityPost>> = db.communityPostDao().getAllPosts()
    fun getPostsByTopic(topic: String): Flow<List<CommunityPost>> = db.communityPostDao().getPostsByTopic(topic)
    fun getPostsByUser(userId: Int): Flow<List<CommunityPost>> = db.communityPostDao().getPostsByUser(userId)
    fun getPostById(id: Int): Flow<CommunityPost?> = db.communityPostDao().getPostById(id)
    suspend fun createPost(post: CommunityPost): Long = db.communityPostDao().insertPost(post)
    suspend fun updatePost(post: CommunityPost) = db.communityPostDao().updatePost(post)
    suspend fun deletePost(post: CommunityPost) = db.communityPostDao().deletePost(post)
    suspend fun likePost(postId: Int) = db.communityPostDao().likePost(postId)
    suspend fun unlikePost(postId: Int) = db.communityPostDao().unlikePost(postId)
    fun searchPosts(query: String): Flow<List<CommunityPost>> = db.communityPostDao().searchPosts(query)
}