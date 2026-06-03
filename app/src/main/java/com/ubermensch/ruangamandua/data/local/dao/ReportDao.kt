package com.ubermensch.ruangamandua.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.ubermensch.ruangamandua.data.local.entity.Report
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report): Long

    @Update
    suspend fun updateReport(report: Report)

    @Delete
    suspend fun deleteReport(report: Report)

    @Query("SELECT * FROM reports WHERE userId = :userId ORDER BY createdAt DESC")
    fun getReportsByUser(userId: Int): Flow<List<Report>>

    @Query("SELECT * FROM reports WHERE id = :reportId LIMIT 1")
    fun getReportById(reportId: Int): Flow<Report?>

    @Query("SELECT COUNT(*) FROM reports WHERE userId = :userId AND status = 'Selesai'")
    fun getLaporanSelesaiCount(userId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM reports WHERE userId = :userId AND status != 'Selesai'")
    fun getLaporanProsesCount(userId: Int): Flow<Int>

    @Query("UPDATE reports SET status = :status WHERE id = :reportId")
    suspend fun updateStatus(reportId: Int, status: String)
}