package tourtravel;

import util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;

public class RiwayatTransaksiFrame extends JFrame {

    private int userId;
    private JTable table;
    private DefaultTableModel model;

    public RiwayatTransaksiFrame(int userId) {
        this.userId = userId;

        setTitle("Riwayat Transaksi");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        try {
            BufferedImage bg = ImageIO.read(new File("resources/bg_riwayat.jpg"));
            setContentPane(new JLabel(new ImageIcon(bg)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.GRAY);
        }

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Riwayat Transaksi Anda", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID Transaksi", "Tanggal", "Status", "Total Biaya"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton btnDetail = new JButton("Lihat Detail");
        JButton btnHapus = new JButton("Hapus dari Tampilan");
        JButton btnKembali = new JButton("Kembali ke Home");

        bottomPanel.add(btnDetail);
        bottomPanel.add(btnHapus);
        bottomPanel.add(btnKembali);
        add(bottomPanel, BorderLayout.SOUTH);

        btnDetail.addActionListener(e -> lihatDetail());
        btnHapus.addActionListener(e -> hapusDariGUI());
        btnKembali.addActionListener(e -> {
            new HomeFrame(userId).setVisible(true);
            dispose();
        });

        loadRiwayat();
    }

    private void loadRiwayat() {
        model.setRowCount(0);
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id_transaksi, tanggal_transaksi, status_pembayaran, total_biaya "
                    + "FROM TRANSAKSI WHERE id_user = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("id_transaksi"),
                    rs.getTimestamp("tanggal_transaksi"),
                    rs.getString("status_pembayaran"),
                    String.format("Rp%,.0f", rs.getDouble("total_biaya"))
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat riwayat transaksi.");
        }
    }

    private void lihatDetail() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            String idTransaksi = (String) table.getValueAt(row, 0);
            new SuksesFrame(userId, idTransaksi).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu.");
        }
    }

    private void hapusDariGUI() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Hapus dari tampilan?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus dari tampilan.");
        }
    }
}
