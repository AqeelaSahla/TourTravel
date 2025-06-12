package util;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import tourtravel.*;

public class HomeFrame extends JFrame {

    private int userId;
    private String userName;

    public HomeFrame(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Beranda - Selamat datang, " + userName);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            BufferedImage bgImage = ImageIO.read(new File("resources/bg_home.jpg"));
            setContentPane(new JLabel(new ImageIcon(bgImage)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.LIGHT_GRAY);
        }

        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Selamat Datang, " + userName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JButton btnProfile = new JButton("Lihat Profil");
        JButton btnPesan = new JButton("Pesan Travel");
        JButton btnRiwayat = new JButton("Riwayat Transaksi");
        JButton btnLogout = new JButton("Logout");

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);
        for (JButton btn : new JButton[]{btnProfile, btnPesan, btnRiwayat, btnLogout}) {
            btn.setFont(buttonFont);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        btnProfile.addActionListener(e -> {
            new ProfileFrame(userId).setVisible(true);
            dispose();
        });

        btnPesan.addActionListener(e -> {
            new PemesananFrame(userId).setVisible(true);
            dispose();
        });

        btnRiwayat.addActionListener(e -> {
            new RiwayatTransaksiFrame(userId).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        menuPanel.add(btnProfile);
        menuPanel.add(btnPesan);
        menuPanel.add(btnRiwayat);
        menuPanel.add(btnLogout);

        add(menuPanel, BorderLayout.CENTER);
    }
    
    public HomeFrame(int userId) {
        this.userId = userId;
        
        setTitle("Beranda - Selamat datang, " + userId);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            BufferedImage bgImage = ImageIO.read(new File("resources/bg_home.jpg"));
            setContentPane(new JLabel(new ImageIcon(bgImage)));
        } catch (IOException e) {
            getContentPane().setBackground(Color.LIGHT_GRAY);
        }

        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Selamat Datang, " + userName);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Menu panel
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JButton btnProfile = new JButton("Lihat Profil");
        JButton btnPesan = new JButton("Pesan Travel");
        JButton btnRiwayat = new JButton("Riwayat Transaksi");
        JButton btnLogout = new JButton("Logout");

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 18);
        for (JButton btn : new JButton[]{btnProfile, btnPesan, btnRiwayat, btnLogout}) {
            btn.setFont(buttonFont);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
        }

        btnProfile.addActionListener(e -> {
            new ProfileFrame(userId).setVisible(true);
            dispose();
        });

        btnPesan.addActionListener(e -> {
            new PemesananFrame(userId).setVisible(true);
            dispose();
        });

        btnRiwayat.addActionListener(e -> {
            new RiwayatTransaksiFrame(userId).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        menuPanel.add(btnProfile);
        menuPanel.add(btnPesan);
        menuPanel.add(btnRiwayat);
        menuPanel.add(btnLogout);

        add(menuPanel, BorderLayout.CENTER);
    }
}
