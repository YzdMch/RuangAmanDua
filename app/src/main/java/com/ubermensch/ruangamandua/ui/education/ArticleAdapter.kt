package com.ubermensch.ruangamandua.ui.education

import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ubermensch.ruangamandua.data.local.entity.Article
import com.ubermensch.ruangamandua.databinding.ItemArticleBinding

class ArticleAdapter(
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.VH>(object : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(o: Article, n: Article) = o.id == n.id
    override fun areContentsTheSame(o: Article, n: Article) = o == n
}) {
    inner class VH(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = holder.binding; val a = getItem(position)
        b.tvJudul.text      = a.judul
        b.tvKategori.text   = a.kategori
        b.tvWaktuBaca.text  = a.waktuBaca
        b.tvViewCount.text  = "${a.viewCount} dilihat"
        b.root.setOnClickListener { onItemClick(a) }
    }
}