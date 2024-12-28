package id.ihsan.uts_projek.model;

data class ApiResponsePemain(
    val status: String,
    val data: List<PemainModel>
)

data class PemainModel(
    val foto: String,
    val gclub: String,
    val nama: String,
    val posisi: String,
    val nclub: String
)
