package com.ubermensch.ruangamandua.ui.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ubermensch.ruangamandua.data.local.RuangAmanDatabase
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.databinding.FragmentReportBinding
import com.ubermensch.ruangamandua.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private var selectedJenis = ""

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModelFactory(RuangAmanRepository(RuangAmanDatabase.getInstance(requireContext())))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        // Jenis Bullying selector
        val jenisMap = mapOf(binding.btnFisik to "Fisik", binding.btnVerbal to "Verbal", binding.btnCyber to "Cyber")
        jenisMap.forEach { (btn, jenis) ->
            btn.setOnClickListener {
                selectedJenis = jenis
                jenisMap.keys.forEach { it.isSelected = false }
                btn.isSelected = true
            }
        }

        // Date Picker
        binding.etTanggal.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                cal.set(y, m, d)
                binding.etTanggal.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.time))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.ivBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnKirimLaporan.setOnClickListener {
            viewModel.createReport(
                userId        = sessionManager.getUserId(),
                jenisBullying = selectedJenis,
                tanggalKejadian = binding.etTanggal.text.toString(),
                lokasi        = binding.etLokasi.text.toString(),
                deskripsi     = binding.etDeskripsi.text.toString(),
                isAnonim      = binding.switchAnonim.isChecked
            )
        }

        viewModel.reportState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ReportState.Loading -> { binding.btnKirimLaporan.isEnabled = false; binding.progressBar.visibility = View.VISIBLE }
                is ReportState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Laporan berhasil dikirim!", Snackbar.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                is ReportState.Error -> {
                    binding.btnKirimLaporan.isEnabled = true; binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    viewModel.resetState()
                }
                else -> { binding.btnKirimLaporan.isEnabled = true; binding.progressBar.visibility = View.GONE }
            }
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}