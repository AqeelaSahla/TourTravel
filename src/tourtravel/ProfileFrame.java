package tourtravel;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;

public class ProfileFrame extends JFrame {

    private int userId;
    private JTextField tfNama, tfEmail, tfTelepon, tfGender, tfDomisili;
    private JButton btnUpdate, btnKembali;

    public ProfileFrame(int userId) {
        this.userId = userId;

        setTitle("Profil Pengguna");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage bg = ImageIO.read(new File("resources/bg_profile.jpg"));
            setContentPane(new JLabel(new ImageIcon(bg)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.GRAY);
        }

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Profil Anda", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Nama", "Email", "Nomor Telepon", "Gender", "Domisili"};
        JTextField[] fields = new JTextField[labels.length];

        tfNama = new JTextField(20);
        tfNama.setEnabled(false);
        tfEmail = new JTextField(20);
        tfTelepon = new JTextField(20);
        tfGender = new JTextField(10);
        tfGender.setEnabled(false);
        tfDomisili = new JTextField(20);
        tfDomisili.setEnabled(false);

        fields[0] = tfNama;
        fields[1] = tfEmail;
        fields[2] = tfTelepon;
        fields[3] = tfGender;
        fields[4] = tfDomisili;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel lbl = new JLabel(labels[i] + ":");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            lbl.setForeground(Color.WHITE);
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        btnUpdate = new JButton("Update Profil");
        btnKembali = new JButton("Kembali ke Home");
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnKembali);
        add(bottomPanel, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> {
            new HomeFrame(userId).setVisible(true);
            dispose();
        });

        btnUpdate.addActionListener(e -> updateProfile());

        loadProfile();
    }

    private void loadProfile() {
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM [USER] WHERE id_user = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tfNama.setText(rs.getString("nama"));
                tfEmail.setText(rs.getString("email"));
                tfTelepon.setText(rs.getString("no_telepon"));
                tfGender.setText(rs.getString("gender"));
                tfDomisili.setText(rs.getString("domisili"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data profil.");
        }
    }

    private void updateProfile() {
        String email = tfEmail.getText().trim();
        String telp = tfTelepon.getText().trim();

        if (email.isEmpty() || telp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan nomor telepon tidak boleh kosong.");
            return;
        }

        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE [USER] SET email = ?, no_telepon = ? WHERE id_user = ?");
            ps.setString(1, email);
            ps.setString(2, telp);
            ps.setInt(3, userId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Profil berhasil diperbarui.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui profil.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memperbarui profil.");
        }
    }
}
