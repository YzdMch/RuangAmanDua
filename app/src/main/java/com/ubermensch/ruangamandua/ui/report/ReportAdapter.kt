package com.ubermensch.ruangamandua.ui.report

import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ubermensch.ruangamandua.R
import com.ubermensch.ruangamandua.data.local.entity.Report
import com.ubermensch.ruangamandua.databinding.ItemReportBinding

class ReportAdapter(
    private val onItemClick: (Report) -> Unit,
    private val onDeleteClick: (Report) -> Unit
) : ListAdapter<Report, ReportAdapter.VH>(object : DiffUtil.ItemCallback<Report>() {
    override fun areItemsTheSame(o: Report, n: Report) = o.id == n.id
    override fun areContentsTheSame(o: Report, n: Report) = o == n
}) {
    inner class VH(val binding: ItemReportBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val b = holder.binding
        val r = getItem(position)
        b.tvReportId.text = r.reportId
        b.tvJenis.text    = r.jenisBullying
        b.tvLokasi.text   = r.lokasi
        b.tvTanggal.text  = r.tanggalKejadian
        b.tvStatus.text   = r.status

        val colorRes = when (r.status) {
            "Terkirim"      -> R.color.status_terkirim
            "Sedang Ditinjau" -> R.color.status_ditinjau
            "Investigasi"   -> R.color.status_investigasi
            "Selesai"       -> R.color.status_selesai
            else            -> R.color.teal_primary
        }
        b.tvStatus.setTextColor(ContextCompat.getColor(b.root.context, colorRes))
        b.root.setOnClickListener { onItemClick(r) }
        b.ivDelete.setOnClickListener { onDeleteClick(r) }
    }
}