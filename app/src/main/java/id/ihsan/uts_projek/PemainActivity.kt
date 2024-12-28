package id.ihsan.uts_projek

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import id.ihsan.uts_projek.api.RetrofitClient
import id.ihsan.uts_projek.model.ApiResponsePemain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PemainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemain)

        // Menyiapkan tombol untuk berpindah antar halaman aktivitas
        val ButtonPertandingan: Button =findViewById(R.id.buttonPertandingan)
        val ButtonKlasemen: Button =findViewById(R.id.buttonKlasemen)
        val ButtonPemain: Button =findViewById(R.id.buttonPemain)

        // Mengatur tombol untuk membuka halaman Pertandingan
        ButtonPertandingan.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mengatur tombol untuk membuka halaman Klasemen
        ButtonKlasemen.setOnClickListener {
            val intent = Intent(this, KlasemenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mengatur tombol untuk tetap berada di halaman Pemain
        ButtonPemain.setOnClickListener {
            val intent = Intent(this, PemainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mendapatkan referensi layout utama untuk menampilkan daftar pertandingan
        val linearLayoutCard = findViewById<LinearLayout>(R.id.linearLayoutCard)

        // Menginisialisasi LayoutInflater untuk menginflate layout item_card_pemain
        val inflater = LayoutInflater.from(this)

        // Menambahkan data setiap pemain ke dalam grid layout secara otomatis
        RetrofitClient.apiService.getPemain().enqueue(object : Callback<ApiResponsePemain> {
            val baseUrl = "https://sportupdate.site/";
            override fun onResponse(call: Call<ApiResponsePemain>, response: Response<ApiResponsePemain>) {
                if (response.isSuccessful) {
                    val pemains = response.body()?.data ?: emptyList()

                    // Tambahkan data ke GridLayout
                    val gridLayoutPemain = findViewById<GridLayout>(R.id.gridLayoutPemain)
                    val inflater = LayoutInflater.from(this@PemainActivity)

                    pemains.forEach { pemain ->
                        val itemViewPemain = inflater.inflate(R.layout.item_card_pemain, gridLayoutPemain, false)

                        val imageViewFoto = itemViewPemain.findViewById<ImageView>(R.id.imageViewFoto)
                        val imageViewGclub = itemViewPemain.findViewById<ImageView>(R.id.imageViewGclub)
                        val textViewName = itemViewPemain.findViewById<TextView>(R.id.textViewName)
                        val textViewPosisi = itemViewPemain.findViewById<TextView>(R.id.textViewPosisi)
                        val textViewNclub = itemViewPemain.findViewById<TextView>(R.id.textViewNclub)

                        // Gunakan Glide untuk memuat gambar dari URL
                        Glide.with(this@PemainActivity)
                            .load(baseUrl+pemain.foto)
                            .into(imageViewFoto)

                        Glide.with(this@PemainActivity)
                            .load(baseUrl+pemain.gclub)
                            .into(imageViewGclub)

                        textViewName.text = pemain.nama
                        textViewPosisi.text = pemain.posisi
                        textViewNclub.text = pemain.nclub

                        gridLayoutPemain.addView(itemViewPemain)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponsePemain>, t: Throwable) {
                data class MessageErrorCard(val message: String, val merror: String)

                if (t is java.net.UnknownHostException || t is java.net.SocketTimeoutException) {
                    // Jika koneksi terputus atau waktu habis
                    // Menyusun daftar pertandingan termasuk logo tim, nama tim, dan skor atau tanggal pertandingan
                    val pertandingan = MessageErrorCard("KONEKSI TERPUTUS, Silahkan cek jaringan!", t.toString())

                    // Inflate item layout dan tambahkan ke linear layout utama
                    val itemView = inflater.inflate(R.layout.item_card_error, linearLayoutCard, false)

                    // Menghubungkan elemen UI dengan elemen layout item card
                    val textViewMessage = itemView.findViewById<TextView>(R.id.textViewNameTim1)
                    val textViewError = itemView.findViewById<TextView>(R.id.textViewNameTim2)

                    // Mengatur gambar dan teks sesuai data pertandingan
                    textViewMessage.text = pertandingan.message
                    textViewError.text = pertandingan.merror

                    // Menambahkan item card ke layout utama
                    linearLayoutCard.addView(itemView)
                } else {
                    // Error lain
                    // Menyusun daftar pertandingan termasuk logo tim, nama tim, dan skor atau tanggal pertandingan
                    val pertandingan = MessageErrorCard("GAGAL MEMUAT DATA!", t.toString())

                    // Inflate item layout dan tambahkan ke linear layout utama
                    val itemView = inflater.inflate(R.layout.item_card_error, linearLayoutCard, false)

                    // Menghubungkan elemen UI dengan elemen layout item card
                    val textViewMessage = itemView.findViewById<TextView>(R.id.textViewNameTim1)
                    val textViewError = itemView.findViewById<TextView>(R.id.textViewNameTim2)

                    // Mengatur gambar dan teks sesuai data pertandingan
                    textViewMessage.text = pertandingan.message
                    textViewError.text = pertandingan.merror

                    // Menambahkan item card ke layout utama
                    linearLayoutCard.addView(itemView)
                }
            }
        })
    }
}