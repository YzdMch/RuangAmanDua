package com.ubermensch.ruangamandua.data.local

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ubermensch.ruangamandua.data.local.dao.*
import com.ubermensch.ruangamandua.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Report::class, Article::class, CommunityPost::class],
    version = 1, exportSchema = false
)
abstract class RuangAmanDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun reportDao(): ReportDao
    abstract fun articleDao(): ArticleDao
    abstract fun communityPostDao(): CommunityPostDao

    companion object {
        @Volatile private var INSTANCE: RuangAmanDatabase? = null

        fun getInstance(context: Context): RuangAmanDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, RuangAmanDatabase::class.java, "ruangaman.db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { seedDatabase(it) }
                            }
                        }
                    }).build().also { INSTANCE = it }
            }
        }

        private suspend fun seedDatabase(db: RuangAmanDatabase) {
            db.articleDao().insertArticles(listOf(
                Article(judul = "Mengenal Batasan: Kapan Candaan Berubah Menjadi Bullying?",
                    kategori = "Tips Pencegahan",
                    deskripsi = "Pahami perbedaan candaan sehat dan bullying.",
                    konten = "Bullying adalah perilaku agresif yang dilakukan secara berulang dengan tujuan menyakiti orang lain...",
                    waktuBaca = "5 Menit Baca", viewCount = 1200),
                Article(judul = "Langkah Hukum Menjadi Korban",
                    kategori = "Hukum",
                    deskripsi = "Panduan hukum bagi korban bullying di Indonesia.",
                    konten = "Di Indonesia, bullying dapat dituntut secara hukum berdasarkan UU Perlindungan Anak...",
                    waktuBaca = "7 Menit Baca", viewCount = 850),
                Article(judul = "Membangun Kepercayaan Diri Pasca Perundungan",
                    kategori = "Psikologi",
                    deskripsi = "Cara membangun kembali kepercayaan diri.",
                    konten = "Pemulihan dari trauma bullying membutuhkan waktu dan dukungan...",
                    waktuBaca = "6 Menit Baca", viewCount = 650),
                Article(judul = "Mengenal Cyberbullying",
                    kategori = "Tips Pencegahan",
                    deskripsi = "Apa itu cyberbullying dan cara menghadapinya.",
                    konten = "Cyberbullying adalah tindakan perundungan melalui media digital...",
                    waktuBaca = "4 Menit Baca", viewCount = 1200),
            ))
            listOf(
                CommunityPost(userId = 0, authorName = "Anonymous", topic = "Peer Support",
                    judul = "Feeling alone after what happened yesterday...",
                    konten = "I finally stood up for myself but now I feel like everyone is looking at me differently.",
                    isAnonim = true, likeCount = 24, commentCount = 8),
                CommunityPost(userId = 0, authorName = "Counsellor Maya", topic = "Counselor Q&A",
                    judul = "Setting boundaries: A simple guide",
                    konten = "Boundary setting is about defining where you end and others begin.",
                    isAnonim = false, likeCount = 112, commentCount = 45),
            ).forEach { db.communityPostDao().insertPost(it) }
        }
    }
}