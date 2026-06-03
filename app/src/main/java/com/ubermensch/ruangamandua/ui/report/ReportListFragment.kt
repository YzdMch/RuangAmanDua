package com.ruangaman.app.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruangaman.app.R
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ruangaman.app.databinding.FragmentReportListBinding
import com.ruangaman.app.utils.SessionManager

class ReportListFragment : Fragment() {
    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var reportAdapter: ReportAdapter

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        reportAdapter = ReportAdapter(
            onItemClick = { report ->
                findNavController().navigate(
                    ReportListFragmentDirections.actionReportListFragmentToReportDetailFragment(report.id)
                )
            },
            onDeleteClick = { report -> confirmDelete(report) }
        )
        binding.rvReports.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reportAdapter
        }

        viewModel.loadReports(sessionManager.getUserId())
        viewModel.reports.observe(viewLifecycleOwner) { reports ->
            reportAdapter.submitList(reports)
            binding.tvEmpty.visibility = if (reports.isEmpty()) View.VISIBLE else View.GONE
            binding.rvReports.visibility = if (reports.isEmpty()) View.GONE else View.VISIBLE
        }

        binding.fabBuatLaporan.setOnClickListener {
            findNavController().navigate(R.id.action_reportListFragment_to_reportFragment)
        }
    }

    private fun confirmDelete(report: Report) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Laporan")
            .setMessage("Yakin ingin menghapus laporan ini?")
            .setPositiveButton("Hapus") { _, _ -> viewModel.deleteReport(report) }
            .setNegativeButton("Batal", null).show()
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}