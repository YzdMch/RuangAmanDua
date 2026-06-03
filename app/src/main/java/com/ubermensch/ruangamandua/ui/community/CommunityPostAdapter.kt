package com.ubermensch.ruangamandua.ui.community

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ubermensch.ruangamandua.data.local.entity.CommunityPost
import com.ubermensch.ruangamandua.databinding.ItemCommunityPostBinding
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class CommunityPostAdapter(
    private val onLikeClick: (CommunityPost) -> Unit,
    private val onItemClick: (CommunityPost) -> Unit
) : ListAdapter<CommunityPost, CommunityPostAdapter.VH>(object : DiffUtil.ItemCallback<CommunityPost>() {
    override fun areItemsTheSame(o: CommunityPost, n: CommunityPost) = o.id == n.id
    override fun areContentsTheSame(o: CommunityPost, n: CommunityPost) = o == n
}) {
    inner class VH(val binding: ItemCommunityPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemCommunityPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = holder.binding; val p = getItem(position)
        b.tvAuthor.text       = p.authorName
        b.tvTopic.text        = p.topic
        b.tvJudul.text        = p.judul
        b.tvKonten.text       = p.konten
        b.tvLikeCount.text    = p.likeCount.toString()
        b.tvCommentCount.text = p.commentCount.toString()
        b.btnLike.isSelected  = p.isLiked

        val sdf = SimpleDateFormat("dd MMM 'Lalu'", Locale("id"))
        b.tvWaktu.text = sdf.format(Date(p.createdAt))

        b.btnLike.setOnClickListener  { onLikeClick(p) }
        b.root.setOnClickListener     { onItemClick(p) }
    }
}