package com.ubermensch.ruangamandua.ui.education

import androidx.lifecycle.*
import com.ubermensch.ruangamandua.data.local.entity.Article
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EducationViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    fun loadArticles(category: String = "Semua") = viewModelScope.launch {
        val flow = if (category == "Semua") repository.getAllArticles()
        else repository.getArticlesByCategory(category)
        flow.catch { emit(emptyList()) }.collect { _articles.value = it }
    }

    fun searchArticles(query: String) = viewModelScope.launch {
        repository.searchArticles(query).catch { emit(emptyList()) }.collect { _articles.value = it }
    }

    fun toggleSave(articleId: Int, isSaved: Boolean) = viewModelScope.launch {
        repository.toggleSaveArticle(articleId, !isSaved)
    }
}

class EducationViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(EducationViewModel::class.java)) EducationViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}