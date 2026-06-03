package com.ubermensch.ruangamandua.ui.community

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubermensch.ruangamandua.R
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {
    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: CommunityPostAdapter

    private val viewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCommunityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postAdapter = CommunityPostAdapter(
            onLikeClick = { post -> if (post.isLiked) viewModel.unlikePost(post.id) else viewModel.likePost(post.id) },
            onItemClick = { post ->
                findNavController().navigate(
                    CommunityFragmentDirections.actionCommunityFragmentToPostDetailFragment(post.id)
                )
            }
        )
        binding.rvPosts.apply { layoutManager = LinearLayoutManager(context); adapter = postAdapter }

        val filterMap = mapOf(
            binding.chipAllTopics    to "Semua",
            binding.chipPeerSupport  to "Peer Support",
            binding.chipHealingStories to "Healing Stories"
        )
        filterMap.forEach { (chip, topic) ->
            chip.setOnClickListener {
                filterMap.keys.forEach { it.isSelected = false }
                chip.isSelected = true; viewModel.loadPosts(topic)
            }
        }
        binding.chipAllTopics.isSelected = true

        binding.etSearch.addTextChangedListener { text ->
            val q = text.toString().trim()
            if (q.isEmpty()) viewModel.loadPosts() else viewModel.searchPosts(q)
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
            binding.tvEmpty.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabBuatPost.setOnClickListener {
            findNavController().navigate(R.id.action_communityFragment_to_createPostFragment)
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}