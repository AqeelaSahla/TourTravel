package tourtravel;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class PembayaranFrame extends JFrame {

    private int userId;
    private String transaksiId;
    private JLabel lblStatus, lblInfo;
    private JPanel qrPanel;
    private JButton btnKembali, btnSelesai;

    public PembayaranFrame(int userId, String transaksiId) {
        this.userId = userId;
        this.transaksiId = transaksiId;

        setTitle("Pembayaran");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage bg = ImageIO.read(new File("resources/bg_pembayaran.jpg"));
            setContentPane(new JLabel(new ImageIcon(bg)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.DARK_GRAY);
        }

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Pembayaran", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblInfo = new JLabel("<html><div style='text-align:center;'>Memuat data transaksi...</div></html>");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInfo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        contentPanel.add(lblInfo, gbc);

        qrPanel = new JPanel();
        qrPanel.setOpaque(false);
        gbc.gridy++;
        contentPanel.add(qrPanel, gbc);

        lblStatus = new JLabel("Status: pending");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblStatus.setForeground(Color.YELLOW);
        gbc.gridy++;
        contentPanel.add(lblStatus, gbc);

        add(contentPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnKembali = new JButton("Kembali ke Home");
        btnSelesai = new JButton("Selesai & Lihat Detail");
        btnPanel.add(btnKembali);
        btnPanel.add(btnSelesai);
        add(btnPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> {
            new HomeFrame(userId).setVisible(true);
            dispose();
        });

        btnSelesai.addActionListener(e -> {
            new SuksesFrame(userId, transaksiId).setVisible(true);
            dispose();
        });

        loadTransaksi();
    }

    private void loadTransaksi() {
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM TRANSAKSI WHERE id_transaksi = ?");
            ps.setString(1, transaksiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int idPaket = rs.getInt("id_paket");
                String noStnk = rs.getString("no_stnk");
                String tanggal = rs.getString("tanggal_transaksi");
                double total = rs.getDouble("total_biaya");
                String status = rs.getString("status_pembayaran");

                String info = "<html><div style='text-align:center;'>ID Transaksi: " + transaksiId
                        + "<br>Tanggal: " + tanggal
                        + "<br>Total: Rp" + String.format("%,.0f", total)
                        + "<br>Metode: QRIS</div></html>";
                lblInfo.setText(info);

                lblStatus.setText("Status: " + status);
                lblStatus.setForeground(status.equals("lunas") ? Color.GREEN : status.equals("batal") ? Color.RED : Color.YELLOW);

                qrPanel.removeAll();
                ImageIcon qrIcon = new ImageIcon(generateQRImage("TRX-" + transaksiId + "-Rp" + (int) total));
                JLabel qrLabel = new JLabel(qrIcon);
                qrPanel.add(qrLabel);
                qrPanel.revalidate();
                qrPanel.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat transaksi.");
        }
    }

    private BufferedImage generateQRImage(String text) {
        try {
            int size = 200;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
