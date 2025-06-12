package tourtravel;

import util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.imageio.ImageIO;

public class PemesananFrame extends JFrame {

    private JComboBox<String> cmbArmada, cmbRegion, cmbWisata, cmbPaket;
    private JButton btnPesan, btnKembali;
    private int userId;
    private Map<String, String> regionMap = new HashMap<>();
    private Map<String, String> wisataMap = new HashMap<>();
    private Map<String, String> paketMap = new HashMap<>();
    private Map<String, String> armadaMap = new HashMap<>(); // Nama -> no_stnk

    public PemesananFrame(int userId) {
        this.userId = userId;
        setTitle("Pesan Travel");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BufferedImage bgImage = ImageIO.read(new File("resources/bg_pemesanan.jpg"));
            setContentPane(new JLabel(new ImageIcon(bgImage)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.LIGHT_GRAY);
        }

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Formulir Pemesanan Travel", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbArmada = new JComboBox<>();
        cmbRegion = new JComboBox<>();
        cmbWisata = new JComboBox<>();
        cmbPaket = new JComboBox<>();

        JLabel lblArmada = new JLabel("Jenis Armada:");
        JLabel lblRegion = new JLabel("Region:");
        JLabel lblWisata = new JLabel("Wisata:");
        JLabel lblPaket = new JLabel("Paket Tour:");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        for (JLabel lbl : new JLabel[]{lblArmada, lblRegion, lblWisata, lblPaket}) {
            lbl.setFont(labelFont);
            lbl.setForeground(Color.WHITE);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblArmada, gbc);
        gbc.gridx = 1;
        formPanel.add(cmbArmada, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblRegion, gbc);
        gbc.gridx = 1;
        formPanel.add(cmbRegion, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblWisata, gbc);
        gbc.gridx = 1;
        formPanel.add(cmbWisata, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblPaket, gbc);
        gbc.gridx = 1;
        formPanel.add(cmbPaket, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPesan = new JButton("Pesan");
        btnKembali = new JButton("Kembali");
        btnPesan.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnKembali.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnPanel.add(btnPesan);
        btnPanel.add(btnKembali);
        add(btnPanel, BorderLayout.SOUTH);

        // Event
        btnKembali.addActionListener(e -> {
            new HomeFrame(userId, getUserName()).setVisible(true);
            dispose();
        });

        btnPesan.addActionListener(e -> {
            if (cmbArmada.getSelectedIndex() == -1 || cmbRegion.getSelectedIndex() == -1
                    || cmbWisata.getSelectedIndex() == -1 || cmbPaket.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Silakan lengkapi semua pilihan.");
                return;
            }

            String selectedArmada = (String) cmbArmada.getSelectedItem();
            String noStnk = armadaMap.get(selectedArmada);
            String paketId = paketMap.get((String) cmbPaket.getSelectedItem());

            new TransaksiFrame(userId, noStnk, paketId).setVisible(true);
            dispose();
        });

        loadData();
    }

    private void loadData() {
        try (Connection conn = KoneksiDB.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT no_stnk, jenis FROM ARMADA");
            while (rs.next()) {
                String no_stnk = rs.getString("no_stnk");
                String jenis = rs.getString("jenis");
                cmbArmada.addItem(jenis);
                armadaMap.put(jenis, no_stnk);
            }

            rs = st.executeQuery("SELECT id_region, nama_region FROM REGION");
            while (rs.next()) {
                String nama = rs.getString("nama_region");
                String id = rs.getString("id_region");
                cmbRegion.addItem(nama);
                regionMap.put(nama, id);
            }

            cmbRegion.addActionListener(e -> loadWisata());

            cmbWisata.addActionListener(e -> loadPaket());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data.");
        }
    }

    private void loadWisata() {
        cmbWisata.removeAllItems();
        wisataMap.clear();
        String id_region = regionMap.get((String) cmbRegion.getSelectedItem());
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id_wisata, nama_wisata FROM WISATA WHERE id_region = ?");
            ps.setString(1, id_region);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nama = rs.getString("nama_wisata");
                String id = rs.getString("id_wisata");
                cmbWisata.addItem(nama);
                wisataMap.put(nama, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPaket() {
        cmbPaket.removeAllItems();
        paketMap.clear();
        String id_wisata = wisataMap.get((String) cmbWisata.getSelectedItem());
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id_paket, nama_paket FROM PAKET_TOUR WHERE id_wisata = ?");
            ps.setString(1, id_wisata);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nama = rs.getString("nama_paket");
                String id = rs.getString("id_paket");
                cmbPaket.addItem(nama);
                paketMap.put(nama, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getUserName() {
        try (Connection conn = KoneksiDB.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT nama FROM [USER] WHERE id_user = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nama");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
