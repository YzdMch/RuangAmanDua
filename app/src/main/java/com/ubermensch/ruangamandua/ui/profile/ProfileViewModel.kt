package com.ruangaman.app.ui.profile

import androidx.lifecycle.*
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _laporanSelesai  = MutableLiveData(0)
    private val _artikelDisimpan = MutableLiveData(0)
    val laporanSelesai:  LiveData<Int> = _laporanSelesai
    val artikelDisimpan: LiveData<Int> = _artikelDisimpan

    fun loadProfile(userId: Int) {
        viewModelScope.launch {
            repository.getLaporanSelesai(userId).catch { emit(0) }.collect { _laporanSelesai.value = it }
        }
        viewModelScope.launch {
            repository.getSavedArticles().catch { emit(emptyList()) }.collect { _artikelDisimpan.value = it.size }
        }
    }
}

class ProfileViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) ProfileViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}