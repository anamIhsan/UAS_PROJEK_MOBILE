package id.ihsan.uts_projek.model;

data class ApiResponseKlasemen(
    val status: String,
    val data: List<KlasemenModel>
)

data class KlasemenModel(
    val club: String,
    val gclub: String,
    val namaTim: String,
    val tanding: Int,
    val menang: Int,
    val seri: Int,
    val kalah: Int,
    val poin: Int

)
