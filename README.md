🧳 TourTravel — Java Swing Travel Booking System
TourTravel adalah aplikasi desktop berbasis Java Swing yang terhubung ke database SQL Server. Aplikasi ini memungkinkan user untuk melakukan login, mendaftar, memilih paket wisata berdasarkan wilayah dan destinasi, memesan travel, serta melakukan pembayaran dengan metode QRIS atau transfer rekening. Transaksi pengguna disimpan dan dapat dilihat dalam riwayat.

🚀 Fitur Utama
✅ Login & Register (dengan validasi email/telepon unik)
✅ Halaman Home dengan navigasi utama
✅ Pemesanan Travel berdasarkan:

Jenis Kendaraan (Innova, Avanza, Hiace, dll)

Region → Wisata → Paket Tour (Relasional dinamis)
✅ Transaksi & Pembayaran:

Form data pemesan

Pembayaran dengan QR Code (ZXing)

Status: pending, lunas, batal
✅ Riwayat Transaksi & Detail Info
✅ Profil User: view dan update data
✅ GUI Java Swing dengan layout konsisten dan gambar latar belakang
✅ Terhubung ke database SQL Server

🧱 Teknologi yang Digunakan
Java 8+ & Java Swing (GUI)

SQL Server (Database)

JDBC (Koneksi DB)

ZXing (QR Code Generator)

Git (Versi Kontrol)

NetBeans (IDE pengembangan)

🗃️ Struktur Database
Database terdiri dari tabel:

ARMADA

SUPIR

REGION

WISATA

PAKET_TOUR

USER

TRANSAKSI

Relasi antara tabel mengikuti skema relasional yang saling terhubung (one-to-many, many-to-many, one-to-one).

📝 Lihat struktur lengkap & contoh data di folder /db atau dokumentasi ERD.

🖼️ Tampilan Antarmuka (GUI)
Tampilan aplikasi terdiri dari:

LoginFrame

RegisterFrame

HomeFrame

PemesananFrame

TransaksiFrame

PembayaranFrame

SuksesFrame

ProfilFrame

RiwayatFrame

GUI memiliki background gambar eksternal (customizable) dan icon navigasi.

📦 Instalasi & Konfigurasi
Clone repository:

bash
Salin
Edit
git clone https://github.com/username/TourTravel.git
Buka di NetBeans / IDE favoritmu.

Pastikan sudah mengimpor:

JDBC Driver untuk SQL Server

ZXing library (QR Code)

Sesuaikan konfigurasi database di file DBConnection.java:

java
Salin
Edit
String url = "jdbc:sqlserver://localhost:1433;databaseName=TourTravel";
String user = "sa";
String password = "your_password";
Jalankan Main.java

🛠️ Kontribusi
Pull request dan kontribusi terbuka!
Silakan fork repo ini dan ajukan perubahan melalui PR.

📄 Lisensi
Lisensi bebas untuk keperluan akademik dan pengembangan pribadi.
Dilarang menjual ulang tanpa izin tertulis.

👤 Author
Developed by Aqeela Sahla
Fakultas Ilmu Komputer, Universitas Brawijaya
Project Tugas Akhir Mata Kuliah Sistem Terdistribusi & Pemrograman Java
