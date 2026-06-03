package com.ruangaman.app.ui.auth

import androidx.lifecycle.*
import com.ubermensch.ruangamandua.data.local.entity.User
import com.ubermensch.ruangamandua.data.local.repository.RuangAmanRepository
import com.ubermensch.ruangamandua.utils.ValidationUtils
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    data class Success(val user: User)    : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: RuangAmanRepository) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, password: String) {
        val error = ValidationUtils.validateLoginForm(email.trim(), password)
        if (error != null) { _authState.value = AuthState.Error(error); return }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val user = repository.login(email.trim().lowercase(), password)
                _authState.value = if (user != null) AuthState.Success(user)
                else AuthState.Error("Email atau kata sandi salah")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun register(nama: String, email: String, instansi: String,
                 password: String, konfirmasi: String, isTermsAccepted: Boolean) {
        val error = ValidationUtils.validateRegisterForm(nama.trim(), email.trim(), instansi.trim(), password, konfirmasi, isTermsAccepted)
        if (error != null) { _authState.value = AuthState.Error(error); return }

        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val emailLower = email.trim().lowercase()
                if (repository.isEmailExists(emailLower)) {
                    _authState.value = AuthState.Error("Email sudah terdaftar"); return@launch
                }
                val user = User(namaLengkap = nama.trim(), email = emailLower,
                    password = password, namaInstansi = instansi.trim())
                val id = repository.register(user)
                val saved = if (id > 0) repository.getUserByIdSync(id.toInt()) else null
                _authState.value = if (saved != null) AuthState.Success(saved)
                else AuthState.Error("Gagal menyimpan akun")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun resetState() { _authState.value = AuthState.Idle }
}

class AuthViewModelFactory(private val repository: RuangAmanRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) return AuthViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}