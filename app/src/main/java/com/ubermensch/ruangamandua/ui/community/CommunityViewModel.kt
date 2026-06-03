package com.ubermensch.ruangamandua.ui.community

import androidx.lifecycle.*
import com.ubermensch.ruangamandua.data.local.entity.CommunityPost
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.utils.ValidationUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed class PostState {
    object Idle    : PostState()
    object Loading : PostState()
    object Success : PostState()
    data class Error(val message: String) : PostState()
}

class CommunityViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _posts     = MutableLiveData<List<CommunityPost>>()
    private val _postState = MutableLiveData<PostState>(PostState.Idle)
    val posts:     LiveData<List<CommunityPost>> = _posts
    val postState: LiveData<PostState> = _postState

    init { loadPosts() }

    fun loadPosts(topic: String = "Semua") = viewModelScope.launch {
        val flow = if (topic == "Semua") repository.getAllPosts()
        else repository.getPostsByTopic(topic)
        flow.catch { emit(emptyList()) }.collect { _posts.value = it }
    }

    fun searchPosts(query: String) = viewModelScope.launch {
        repository.searchPosts(query).catch { emit(emptyList()) }.collect { _posts.value = it }
    }

    fun likePost(postId: Int) = viewModelScope.launch { repository.likePost(postId) }
    fun unlikePost(postId: Int) = viewModelScope.launch { repository.unlikePost(postId) }

    fun createPost(userId: Int, authorName: String, judul: String, konten: String, topic: String, isAnonim: Boolean) {
        val error = ValidationUtils.validatePostForm(judul, konten)
        if (error != null) { _postState.value = PostState.Error(error); return }
        _postState.value = PostState.Loading
        viewModelScope.launch {
            try {
                val post = CommunityPost(userId = userId,
                    authorName = if (isAnonim) "Anonymous" else authorName,
                    topic = topic, judul = judul, konten = konten, isAnonim = isAnonim)
                val id = repository.createPost(post)
                _postState.value = if (id > 0) PostState.Success else PostState.Error("Gagal membuat post")
            } catch (e: Exception) {
                _postState.value = PostState.Error("Error: ${e.message}")
            }
        }
    }

    fun deletePost(post: CommunityPost) = viewModelScope.launch { repository.deletePost(post) }
    fun resetState() { _postState.value = PostState.Idle }
}

class CommunityViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) CommunityViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}