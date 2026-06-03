package com.ruangaman.app.ui.education

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.entity.Article
import com.ruangaman.app.databinding.FragmentArticleDetailBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ArticleDetailFragment : Fragment() {
    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ArticleDetailFragmentArgs by navArgs()

    private val viewModel: ArticleDetailViewModel by viewModels {
        ArticleDetailViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadArticle(args.articleId)
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }
        binding.ivBookmark.setOnClickListener { viewModel.toggleSave() }

        viewModel.article.observe(viewLifecycleOwner) { article ->
            article ?: return@observe
            binding.tvJudul.text      = article.judul
            binding.tvKategori.text   = article.kategori
            binding.tvWaktuBaca.text  = article.waktuBaca
            binding.tvViewCount.text  = "${article.viewCount} dilihat"
            binding.tvKonten.text     = article.konten
            binding.ivBookmark.isSelected = article.isSaved
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

class ArticleDetailViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _article = MutableLiveData<Article?>()
    val article: LiveData<Article?> = _article

    fun loadArticle(id: Int) = viewModelScope.launch {
        repository.getArticleById(id).catch { emit(null) }.collect { _article.value = it }
        repository.incrementViewCount(id)
    }

    fun toggleSave() {
        val current = _article.value ?: return
        viewModelScope.launch { repository.toggleSaveArticle(current.id, !current.isSaved) }
    }
}

class ArticleDetailViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) ArticleDetailViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}