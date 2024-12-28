package id.ihsan.uts_projek

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.bumptech.glide.Glide
import id.ihsan.uts_projek.api.RetrofitClient
import id.ihsan.uts_projek.model.ApiResponsePertandingan
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Mendeklarasikan tombol untuk navigasi ke halaman lain
        val ButtonPertandingan: Button =findViewById(R.id.buttonPertandingan)
        val ButtonKlasemen: Button =findViewById(R.id.buttonKlasemen)
        val ButtonPemain: Button =findViewById(R.id.buttonPemain)

        // Mengatur tombol "Pertandingan" untuk membuka halaman yang sama, menghapus aktivitas sebelumnya
        ButtonPertandingan.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mengatur tombol "Klasemen" untuk membuka halaman KlasemenActivity
        ButtonKlasemen.setOnClickListener {
            val intent = Intent(this, KlasemenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mengatur tombol "Pemain" untuk membuka halaman PemainActivity
        ButtonPemain.setOnClickListener {
            val intent = Intent(this, PemainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mendapatkan referensi layout utama untuk menampilkan daftar pertandingan
        val linearLayoutCard = findViewById<LinearLayout>(R.id.linearLayoutCard)

        // Mengatur inflater untuk menggandakan item layout untuk setiap pertandingan
        val inflater = LayoutInflater.from(this)

        // Mendapatkan data pertandingan dari API
        RetrofitClient.apiService.getPertandingan().enqueue(object : Callback<ApiResponsePertandingan> {
            val baseUrl = "https://sportupdate.site/";
            override fun onResponse(call: Call<ApiResponsePertandingan>, response: Response<ApiResponsePertandingan>) {
                if (response.isSuccessful) {
                    val pertandingans = response.body()?.data ?: emptyList()

                    // Loop untuk membuat tampilan setiap pertandingan secara dinamis
                    for (pertandingan in pertandingans) {
                        // Inflate item layout dan tambahkan ke linear layout utama
                        val itemView = inflater.inflate(R.layout.item_card_pertandingan, linearLayoutCard, false)

                        // Menghubungkan elemen UI dengan elemen layout item card
                        val imageViewGambarTim1 = itemView.findViewById<ImageView>(R.id.imageViewTim1)
                        val imageViewGambarTim2 = itemView.findViewById<ImageView>(R.id.imageViewTim2)
                        val textViewNameTim1 = itemView.findViewById<TextView>(R.id.textViewNameTim1)
                        val textViewNameTim2 = itemView.findViewById<TextView>(R.id.textViewNameTim2)
                        val textViewScoreTim1 = itemView.findViewById<TextView>(R.id.textViewScoreTim1)
                        val textViewScoreTim2 = itemView.findViewById<TextView>(R.id.textViewScoreTim2)

                        // Mengatur gambar dan teks sesuai data pertandingan
                        Glide.with(this@MainActivity)
                            .load(baseUrl+pertandingan.gtim1)
                            .into(imageViewGambarTim1)

                        Glide.with(this@MainActivity)
                            .load(baseUrl+pertandingan.gtim2)
                            .into(imageViewGambarTim2)

                        textViewNameTim1.text = pertandingan.tim1
                        textViewNameTim2.text = pertandingan.tim2
                        textViewScoreTim1.text = pertandingan.stim1
                        textViewScoreTim2.text = pertandingan.stim2

                        // Menambahkan item card ke layout utama
                        linearLayoutCard.addView(itemView)
                    }

                }
            }

            override fun onFailure(call: Call<ApiResponsePertandingan>, t: Throwable) {
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
