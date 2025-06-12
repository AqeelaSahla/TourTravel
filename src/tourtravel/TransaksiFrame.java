package tourtravel;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransaksiFrame extends JFrame {

    private JTextField tfNama, tfEmail, tfTelepon;
    private JComboBox<String> cmbMetode;
    private JButton btnLanjut, btnKembali;
    private int userId;
    private String noStnk, paketId;

    public TransaksiFrame(int userId, String noStnk, String paketId) {
        this.userId = userId;
        this.noStnk = noStnk;
        this.paketId = paketId;

        setTitle("Transaksi Pembayaran");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage bgImage = ImageIO.read(new File("resources/bg_transaksi.jpg"));
            setContentPane(new JLabel(new ImageIcon(bgImage)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.GRAY);
        }

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Konfirmasi Data Pemesan", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

//        JLabel lblNama = new JLabel("Nama:");
//        JLabel lblEmail = new JLabel("Email:");
//        JLabel lblTelepon = new JLabel("Nomor Telepon:");
        JLabel lblMetode = new JLabel("Metode Pembayaran:");

        for (JLabel lbl : new JLabel[]{lblMetode}) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setForeground(Color.WHITE);
        }

//        tfNama = new JTextField(25);
//        tfEmail = new JTextField();
//        tfTelepon = new JTextField();
        cmbMetode = new JComboBox<>(new String[]{"QRIS", "Transfer Bank"});

//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        formPanel.add(lblNama, gbc);
//        gbc.gridx = 1;
//        formPanel.add(tfNama, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy++;
//        formPanel.add(lblEmail, gbc);
//        gbc.gridx = 1;
//        formPanel.add(tfEmail, gbc);
//
//        gbc.gridx = 0;
//        gbc.gridy++;
//        formPanel.add(lblTelepon, gbc);
//        gbc.gridx = 1;
//        formPanel.add(tfTelepon, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblMetode, gbc);
        gbc.gridx = 1;
        formPanel.add(cmbMetode, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnLanjut = new JButton("Lanjutkan");
        btnKembali = new JButton("Kembali");
        btnLanjut.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnKembali.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnPanel.add(btnLanjut);
        btnPanel.add(btnKembali);
        add(btnPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> {
            new PemesananFrame(userId).setVisible(true);
            dispose();
        });

        btnLanjut.addActionListener(e -> prosesTransaksi());
    }

    private void prosesTransaksi() {
//        String nama = tfNama.getText().trim();
//        String email = tfEmail.getText().trim();
//        String telepon = tfTelepon.getText().trim();
        String metode = cmbMetode.getSelectedItem().toString();

//        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Semua data harus diisi!");
//            return;
//        }

        try (Connection conn = KoneksiDB.getConnection()) {
            // Ambil biaya dari paket
            PreparedStatement psPaket = conn.prepareStatement("SELECT biaya FROM PAKET_TOUR WHERE id_paket = ?");
            psPaket.setString(1, paketId);
            ResultSet rs = psPaket.executeQuery();
//            int userID = rs.getInt("id_user");
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Paket tidak ditemukan.");
                return;
            }

            double biaya = rs.getDouble("biaya");
            String status = "pending";
            String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO TRANSAKSI (id_transaksi, id_user, id_paket, no_stnk, tanggal_transaksi, total_biaya, status_pembayaran) "
                    + "OUTPUT INSERTED.id_transaksi VALUES (?, ?, ?, ?, ?, ?, ?)");
            String idTransaksi = TransactionIdGenerator.generateTransactionId();
            ps.setString(1, idTransaksi);
            ps.setInt(2, userId);
            ps.setString(3, paketId);
            ps.setString(4, noStnk);
            ps.setString(5, tanggal);
            ps.setDouble(6, biaya);
            ps.setString(7, status);         
            ResultSet inserted = ps.executeQuery();
            if (inserted.next()) {
                new PembayaranFrame(userId, idTransaksi ).setVisible(true);
                dispose();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memproses transaksi.");
        }
    }
}
