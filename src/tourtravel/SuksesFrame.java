package tourtravel;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;

public class SuksesFrame extends JFrame {

    private int userId;
    private String transaksiId;

    public SuksesFrame(int userId, String transaksiId) {
        this.userId = userId;
        this.transaksiId = transaksiId;

        setTitle("Transaksi Berhasil");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage bg = ImageIO.read(new File("resources/bg_sukses.jpg"));
            setContentPane(new JLabel(new ImageIcon(bg)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.GRAY);
        }

        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Transaksi Berhasil", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {
            "ID Transaksi", "ID User", "ID Paket",
            "Jenis Kendaraan", "Nomor STNK", "Nama Supir", "No. Telepon Supir",
            "Tanggal", "Total Biaya"
        };
        JLabel[] labelComponents = new JLabel[labels.length];
        JLabel[] valueComponents = new JLabel[labels.length];

        for (int i = 0; i < labels.length; i++) {
            labelComponents[i] = new JLabel(labels[i] + ":");
            labelComponents[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
            labelComponents[i].setForeground(Color.WHITE);
            valueComponents[i] = new JLabel("...");
            valueComponents[i].setFont(new Font("Segoe UI", Font.BOLD, 16));
            valueComponents[i].setForeground(Color.YELLOW);

            gbc.gridx = 0;
            gbc.gridy = i;
            infoPanel.add(labelComponents[i], gbc);
            gbc.gridx = 1;
            infoPanel.add(valueComponents[i], gbc);
        }

        add(infoPanel, BorderLayout.CENTER);

        JButton btnHome = new JButton("Kembali ke Home");
        btnHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnHome);
        add(bottomPanel, BorderLayout.SOUTH);

        btnHome.addActionListener(e -> {
            new HomeFrame(userId).setVisible(true);
            dispose();
        });

        loadData(valueComponents);
    }

    private void loadData(JLabel[] valueComponents) {
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT t.id_transaksi, t.id_user, t.id_paket, t.no_stnk, t.tanggal_transaksi, t.total_biaya, "
                    + "a.jenis, s.nama_supir, s.no_telepon "
                    + "FROM TRANSAKSI t "
                    + "JOIN ARMADA a ON t.no_stnk = a.no_stnk "
                    + "JOIN SUPIR s ON a.id_supir = s.id_supir "
                    + "WHERE t.id_transaksi = ?");
            ps.setString(1, transaksiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valueComponents[0].setText(rs.getString("id_transaksi"));
                valueComponents[1].setText(rs.getString("id_user"));
                valueComponents[2].setText(rs.getString("id_paket"));
                valueComponents[3].setText(rs.getString("jenis"));
                valueComponents[4].setText(rs.getString("no_stnk"));
                valueComponents[5].setText(rs.getString("nama_supir"));
                valueComponents[6].setText(rs.getString("no_telepon"));
                valueComponents[7].setText(rs.getString("tanggal_transaksi"));
                valueComponents[8].setText("Rp" + String.format("%,.0f", rs.getDouble("total_biaya")));
            } else {
                JOptionPane.showMessageDialog(this, "Transaksi tidak ditemukan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data transaksi.");
        }
    }
}
