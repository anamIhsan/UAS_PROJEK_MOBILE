package id.ihsan.uts_projek.model;

data class ApiResponsePertandingan(
    val status: String,
    val data: List<PertandinganModel>
)

data class PertandinganModel(
    val gtim1: String,
    val gtim2: String,
    val tim1: String,
    val tim2: String,
    val stim1: String,
    val stim2: String
)
