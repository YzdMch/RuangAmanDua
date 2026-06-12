package com.ubermensch.ruangamandua.ui.report

import androidx.lifecycle.*
import com.ubermensch.ruangamandua.data.local.entity.Report
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.utils.ValidationUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class ReportState {
    object Idle    : ReportState()
    object Loading : ReportState()
    data class Success(val reportId: String) : ReportState()
    data class Error(val message: String)    : ReportState()
}

class ReportViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _reportState = MutableLiveData<ReportState>(ReportState.Idle)
    val reportState: LiveData<ReportState> = _reportState

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> = _reports

    fun loadReports(userId: Int) {
        viewModelScope.launch {
            repository.getReportsByUser(userId).catch { emit(emptyList()) }.collect { _reports.value = it }
        }
    }

    fun createReport(userId: Int, jenisBullying: String, tanggalKejadian: String,
                     lokasi: String, deskripsi: String, isAnonim: Boolean) {
        val error = ValidationUtils.validateReportForm(jenisBullying, tanggalKejadian, lokasi, deskripsi)
        if (error != null) { _reportState.value = ReportState.Error(error); return }

        _reportState.value = ReportState.Loading
        viewModelScope.launch {
            try {
                val reportId = "RA-${System.currentTimeMillis().toString().takeLast(8)}"
                val report = Report(userId = userId, reportId = reportId, jenisBullying = jenisBullying,
                    tanggalKejadian = tanggalKejadian, lokasi = lokasi, deskripsi = deskripsi, isAnonim = isAnonim)
                val id = repository.createReport(report)
                _reportState.value = if (id > 0) ReportState.Success(reportId)
                else ReportState.Error("Gagal mengirim laporan")
            } catch (e: Exception) {
                _reportState.value = ReportState.Error("Error: ${e.message}")
            }
        }
    }

    fun updateReport(report: Report) {
        viewModelScope.launch {
            repository.updateReport(report)
            _reportState.value = ReportState.Success(report.reportId)
        }
    }

    fun deleteReport(report: Report) {
        viewModelScope.launch { repository.deleteReport(report) }
    }

    fun resetState() { _reportState.value = ReportState.Idle }
}

class ReportViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) ReportViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}