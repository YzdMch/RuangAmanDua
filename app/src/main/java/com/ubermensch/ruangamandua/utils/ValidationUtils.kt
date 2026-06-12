package com.ubermensch.ruangamandua.utils

object ValidationUtils {
    fun isValidEmail(email: String) =
        email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(password: String) = password.length >= 8

    fun validateLoginForm(email: String, password: String): String? = when {
        email.isEmpty()            -> "Email tidak boleh kosong"
        !isValidEmail(email)       -> "Format email tidak valid"
        password.isEmpty()         -> "Kata sandi tidak boleh kosong"
        !isValidPassword(password) -> "Kata sandi minimal 8 karakter"
        else -> null
    }

    fun validateRegisterForm(
        nama: String, email: String, instansi: String,
        password: String, konfirmasi: String, isTermsAccepted: Boolean
    ): String? = when {
        nama.trim().length < 3       -> "Nama minimal 3 karakter"
        email.isEmpty()              -> "Email tidak boleh kosong"
        !isValidEmail(email)         -> "Format email tidak valid"
        instansi.isEmpty()           -> "Nama instansi tidak boleh kosong"
        password.isEmpty()           -> "Kata sandi tidak boleh kosong"
        !isValidPassword(password)   -> "Kata sandi minimal 8 karakter"
        konfirmasi != password       -> "Kata sandi tidak cocok"
        !isTermsAccepted             -> "Anda harus menyetujui Syarat & Ketentuan"
        else -> null
    }

    fun validateReportForm(
        jenisBullying: String, tanggal: String, lokasi: String, deskripsi: String
    ): String? = when {
        jenisBullying.isEmpty()        -> "Pilih jenis perundungan"
        tanggal.isEmpty()              -> "Tanggal kejadian tidak boleh kosong"
        lokasi.isEmpty()               -> "Lokasi kejadian tidak boleh kosong"
        deskripsi.trim().length < 20   -> "Deskripsi minimal 20 karakter"
        else -> null
    }

    fun validatePostForm(judul: String, konten: String): String? = when {
        judul.isEmpty()             -> "Judul tidak boleh kosong"
        konten.trim().length < 10   -> "Konten minimal 10 karakter"
        else -> null
    }
}