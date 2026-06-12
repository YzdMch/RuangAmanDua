package com.ubermensch.ruangamandua.ui.report

import android.os.Bundle
import android.view.*
import kotlin.getValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.data.local.entity.Report
import com.ubermensch.ruangamandua.databinding.FragmentReportDetailBinding
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ReportDetailFragment : Fragment() {
    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!
    private val args: ReportDetailFragmentArgs by navArgs()

    private val viewModel: ReportDetailViewModel by viewModels {
        ReportDetailViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadReport(args.reportId)
        binding.ivBack.setOnClickListener { findNavController().navigateUp() }

        viewModel.report.observe(viewLifecycleOwner) { report ->
            report ?: return@observe
            binding.tvReportId.text  = "ID Laporan: ${report.reportId}"
            binding.tvStatus.text    = report.status
            binding.tvTanggalKirim.text = "Dikirim pada ${report.tanggalKejadian}"
            binding.tvJenisBullying.text = report.jenisBullying
            binding.tvLokasi.text    = report.lokasi
            binding.tvAnonim.text    = if (report.isAnonim) "Ya" else "Tidak"

            val steps = listOf("Terkirim","Sedang Ditinjau","Investigasi","Selesai")
            val idx = steps.indexOf(report.status)
            listOf(binding.stepTerkirim, binding.stepDitinjau, binding.stepInvestigasi, binding.stepSelesai)
                .forEachIndexed { i, v -> v.isActivated = i <= idx; v.isSelected = i == idx }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

class ReportDetailViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _report = MutableLiveData<Report?>()
    val report: LiveData<Report?> = _report

    fun loadReport(id: Int) = viewModelScope.launch {
        repository.getReportById(id).catch { emit(null) }.collect { _report.value = it }
    }
}

class ReportDetailViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(ReportDetailViewModel::class.java)) ReportDetailViewModel(repository) as T
        else throw IllegalArgumentException("Unknown ViewModel")
}