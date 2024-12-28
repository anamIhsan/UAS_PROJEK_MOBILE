package id.ihsan.uts_projek

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.ComponentActivity
import id.ihsan.uts_projek.api.RetrofitClient
import id.ihsan.uts_projek.model.ApiResponseKlasemen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide

class KlasemenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_klasemen)

        // Mendefinisikan tombol navigasi untuk mengakses halaman Pertandingan, Klasemen, dan Pemain
        val ButtonPertandingan: Button =findViewById(R.id.buttonPertandingan)
        val ButtonKlasemen: Button =findViewById(R.id.buttonKlasemen)
        val ButtonPemain: Button =findViewById(R.id.buttonPemain)

        // Mendefinisikan TableLayout untuk menampilkan data klasemen tim
        val tableKlasemen = findViewById<TableLayout>(R.id.tableKlasemen)

        // Aksi pada tombol untuk berpindah ke halaman Pertandingan dengan menghapus aktivitas sebelumnya
        ButtonPertandingan.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Aksi pada tombol untuk tetap berada di halaman Klasemen, dengan menghapus aktivitas sebelumnya
        ButtonKlasemen.setOnClickListener {
            val intent = Intent(this, KlasemenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Aksi pada tombol untuk menuju ke halaman Pemain dengan menghapus aktivitas sebelumnya
        ButtonPemain.setOnClickListener {
            val intent = Intent(this, PemainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Mendapatkan referensi layout utama untuk menampilkan daftar pertandingan
        val linearLayoutCard = findViewById<LinearLayout>(R.id.linearLayoutCard)

        // Mengatur inflater untuk menggandakan item layout untuk setiap pertandingan
        val inflater = LayoutInflater.from(this)

        val tableRowHead = TableRow(this@KlasemenActivity).apply {
            layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Klub
        val posisiTextView = TextView(this@KlasemenActivity).apply {
            text = "Klub"
//            gravity = Gravity.CENTER
            setPadding(16, 16, 16, 16)
        }
        tableRowHead.addView(posisiTextView)

        val posisiNullView = TextView(this@KlasemenActivity).apply {
            text = ""
            gravity = Gravity.CENTER
            setPadding(16, 16, 16, 16)
        }
        tableRowHead.addView(posisiNullView)

        val posisiNullView2 = TextView(this@KlasemenActivity).apply {
            text = ""
            gravity = Gravity.CENTER
            setPadding(16, 16, 16, 16)
        }
        tableRowHead.addView(posisiNullView2)

        // Header (Tanding, Menang, Seri, Kalah, Poin)
        listOf(
            "Tanding",
            "Menang",
            "Seri",
            "Kalah",
            "Poin"
        ).forEach { value ->
            val detailTextView = TextView(this@KlasemenActivity).apply {
                text = value
                gravity = Gravity.CENTER
                setPadding(16, 16, 16, 16)
            }
            tableRowHead.addView(detailTextView)
        }

        // Tambahkan TableRow ke TableLayout
        tableKlasemen.addView(tableRowHead)

        RetrofitClient.apiService.getKlasemen().enqueue(object : Callback<ApiResponseKlasemen> {
            val baseUrl = "https://sportupdate.site/";
            override fun onResponse(call: Call<ApiResponseKlasemen>, response: Response<ApiResponseKlasemen>) {
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()

                    // Tambahkan setiap baris data ke dalam TableLayout
                    data.forEachIndexed { index, klasemen ->
                        val tableRow = TableRow(this@KlasemenActivity).apply {
                            layoutParams = TableLayout.LayoutParams(
                                TableLayout.LayoutParams.MATCH_PARENT,
                                TableLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        val numberTextView = TextView(this@KlasemenActivity).apply {
                            text = (index + 1).toString() + ". "
                            gravity = Gravity.START
                            setPadding(10, 10, 10, 10)
                        }
                        tableRow.addView(numberTextView)

                        // Logo Klub
                        val logoImageView = ImageView(this@KlasemenActivity).apply {
                            layoutParams = TableRow.LayoutParams(60, 60)
                            Glide.with(this@KlasemenActivity)
                                .load(baseUrl+klasemen.gclub) // URL logo dari API
                                .into(this)
                        }
                        tableRow.addView(logoImageView)

                        // Klub
                        val posisiTextView = TextView(this@KlasemenActivity).apply {
                            text = klasemen.club.toString()
                            gravity = Gravity.START
                            setPadding(16, 16, 16, 16)
                        }
                        tableRow.addView(posisiTextView)

                        // Detail Lainnya (Tanding, Menang, Seri, Kalah, Poin)
                        listOf(
                            klasemen.tanding,
                            klasemen.menang,
                            klasemen.seri,
                            klasemen.kalah,
                            klasemen.poin
                        ).forEach { value ->
                            val detailTextView = TextView(this@KlasemenActivity).apply {
                                text = value.toString()
                                gravity = Gravity.CENTER
                                setPadding(16, 16, 16, 16)
                            }
                            tableRow.addView(detailTextView)
                        }

                        // Tambahkan TableRow ke TableLayout
                        tableKlasemen.addView(tableRow)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponseKlasemen>, t: Throwable) {
                data class MessageErrorCard(val message: String, val merror: String)

                if (t is java.net.UnknownHostException || t is java.net.SocketTimeoutException) {
                    // Jika koneksi terputus atau waktu habis
                    // Menyusun daftar pertandingan termasuk logo tim, nama tim, dan skor atau tanggal pertandingan
                    val messageErrorCard = MessageErrorCard("KONEKSI TERPUTUS, Silahkan cek jaringan!", t.toString())

                    // Inflate item layout dan tambahkan ke linear layout utama
                    val itemView = inflater.inflate(R.layout.item_card_error, linearLayoutCard, false)

                    // Menghubungkan elemen UI dengan elemen layout item card
                    val textViewMessage = itemView.findViewById<TextView>(R.id.textViewNameTim1)
                    val textViewError = itemView.findViewById<TextView>(R.id.textViewNameTim2)

                    // Mengatur gambar dan teks sesuai data pertandingan
                    textViewMessage.text = messageErrorCard.message
                    textViewError.text = messageErrorCard.merror

                    // Menambahkan item card ke layout utama
                    linearLayoutCard.addView(itemView)
                } else {
                    // Error lain
                    // Menyusun daftar pertandingan termasuk logo tim, nama tim, dan skor atau tanggal pertandingan
                    val messageErrorCard = MessageErrorCard("GAGAL MEMUAT DATA!", t.toString())

                    // Inflate item layout dan tambahkan ke linear layout utama
                    val itemView = inflater.inflate(R.layout.item_card_error, linearLayoutCard, false)

                    // Menghubungkan elemen UI dengan elemen layout item card
                    val textViewMessage = itemView.findViewById<TextView>(R.id.textViewNameTim1)
                    val textViewError = itemView.findViewById<TextView>(R.id.textViewNameTim2)

                    // Mengatur gambar dan teks sesuai data pertandingan
                    textViewMessage.text = messageErrorCard.message
                    textViewError.text = messageErrorCard.merror

                    // Menambahkan item card ke layout utama
                    linearLayoutCard.addView(itemView)
                }
            }
        })
    }
}

//data.forEachIndexed { index1, row ->
//    val tableRow = TableRow(this)
//    tableRow.layoutParams = TableLayout.LayoutParams(
//        TableLayout.LayoutParams.MATCH_PARENT,
//        TableLayout.LayoutParams.WRAP_CONTENT
//    )
//
//    // Menambahkan setiap data di baris sebagai cell di dalam row tabel
//    row.forEachIndexed { index, cellData ->
//        // Baris pertama (header tabel), setiap cell diatur sebagai TextView
//        if (index1 === 0) {
//            val cellTextView = TextView(this)
//            cellTextView.text = cellData.toString()
//            cellTextView.setPadding(16, 16, 16, 16)
//            cellTextView.gravity = Gravity.CENTER
//            tableRow.addView(cellTextView)
//        } else {
//            // Untuk cell kedua pada baris data, gunakan ImageView sebagai logo tim
//            if (index === 1) {
//                val cellImageView = ImageView(this).apply {
//                    setImageResource(cellData as Int)
//
//                    layoutParams = TableRow.LayoutParams(60,  60) // Mengatur ukuran gambar
//
//                    background = resources.getDrawable(R.drawable.image_klasemen_rounded, null) // Gambar dengan sudut membulat
//                }
//                tableRow.addView(cellImageView)
//            } else {
//                // Selain cell logo, tambahkan TextView untuk nama tim dan detail lainnya
//                val cellTextView = TextView(this)
//                cellTextView.text = cellData.toString()
//                cellTextView.setPadding(16, 16, 16, 16)
//                cellTextView.gravity = Gravity.CENTER
//                tableRow.addView(cellTextView)
//            }
//        }
//    }
//
//    // Tambahkan TableRow ke TableLayout
//    tableKlasemen.addView(tableRow)
//}