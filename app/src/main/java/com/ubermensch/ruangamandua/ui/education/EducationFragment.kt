package com.ruangaman.app.ui.education

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ruangaman.app.databinding.FragmentEducationBinding

class EducationFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleAdapter: ArticleAdapter
    private var currentCategory = "Semua"

    private val viewModel: EducationViewModel by viewModels {
        EducationViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEducationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter = ArticleAdapter { article ->
            findNavController().navigate(
                EducationFragmentDirections.actionEducationFragmentToArticleDetailFragment(article.id)
            )
        }
        binding.rvArticles.apply { layoutManager = LinearLayoutManager(context); adapter = articleAdapter }

        binding.etSearch.addTextChangedListener { text ->
            val q = text.toString().trim()
            if (q.isEmpty()) viewModel.loadArticles(currentCategory) else viewModel.searchArticles(q)
        }

        val filterMap = mapOf(
            binding.chipSemua     to "Semua",
            binding.chipTips      to "Tips Pencegahan",
            binding.chipHukum     to "Hukum",
            binding.chipPsikologi to "Psikologi"
        )
        filterMap.forEach { (chip, cat) ->
            chip.setOnClickListener {
                currentCategory = cat
                filterMap.keys.forEach { it.isSelected = false }
                chip.isSelected = true
                viewModel.loadArticles(cat)
            }
        }
        binding.chipSemua.isSelected = true
        viewModel.loadArticles()
        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articleAdapter.submitList(articles)
            binding.tvEmpty.visibility = if (articles.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}