package com.ubermensch.ruangamandua.ui.dashboard

import androidx.lifecycle.*
import com.ubermensch.ruangamandua.data.local.entity.Article
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _laporanProses  = MutableLiveData(0)
    private val _laporanSelesai = MutableLiveData(0)
    val laporanProses:  LiveData<Int> = _laporanProses
    val laporanSelesai: LiveData<Int> = _laporanSelesai

    val articles: LiveData<List<Article>> = repository.getAllArticles()
        .catch { emit(emptyList()) }.asLiveData()

    fun loadDashboardData(userId: Int) {
        viewModelScope.launch {
            repository.getLaporanProses(userId).catch { emit(0) }.collect { _laporanProses.value = it }
        }
        viewModelScope.launch {
            repository.getLaporanSelesai(userId).catch { emit(0) }.collect { _laporanSelesai.value = it }
        }
    }
}

class HomeViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) HomeViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}