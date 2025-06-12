package util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import tourtravel.*;

public class HomeFrame extends JFrame {

    private int userId;
    private String userName;

    // Modern color scheme
    private Color primaryBlue = new Color(70, 130, 180);
    private Color lightBlue = new Color(135, 206, 250);
    private Color darkBlue = new Color(25, 25, 112);
    private Color whiteSmoke = new Color(245, 245, 245);

    public HomeFrame(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;

        setTitle("Tour and Travel - Selamat datang, " + userName);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Set gradient background
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    public HomeFrame(int userId) {
        this.userId = userId;
        this.userName = "User " + userId; // Default name when no userName provided

        setTitle("Tour and Travel - Selamat datang, User " + userId);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // Set gradient background
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Selamat Datang, " + userName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(welcomeLabel, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Create menu buttons with modern design
        JButton profileBtn = createMenuButton("Lihat Profil", "Kelola informasi profil Anda");
        JButton bookingBtn = createMenuButton("Pesan Travel", "Booking perjalanan impian Anda");
        JButton historyBtn = createMenuButton("Riwayat Transaksi", "Lihat riwayat pemesanan");
        JButton logoutBtn = createMenuButton("Logout", "Keluar dari sistem");

        // Position buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(profileBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(bookingBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(historyBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(logoutBtn, gbc);

        // Add action listeners - preserve original functionality
        profileBtn.addActionListener(e -> {
            new ProfileFrame(userId).setVisible(true);
            dispose();
        });

        bookingBtn.addActionListener(e -> {
            new PemesananFrame(userId).setVisible(true);
            dispose();
        });

        historyBtn.addActionListener(e -> {
            new RiwayatTransaksiFrame(userId).setVisible(true);
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        return panel;
    }

    private JButton createMenuButton(String title, String description) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Create
                                                                                                         // gradient
                                                                                                         // background
                GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), whiteSmoke);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(300, 150));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(darkBlue);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Description label
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(primaryBlue);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        contentPanel.add(titleLabel, BorderLayout.CENTER);
        contentPanel.add(descLabel, BorderLayout.SOUTH);

        button.add(contentPanel, BorderLayout.CENTER);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(lightBlue);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JLabel footerLabel = new JLabel("© 2025 Tour and Travel System - Perjalanan Aman & Nyaman");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(footerLabel);
        return panel;
    }

    // Custom JPanel for gradient background
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Create gradient from light blue to dark blue
            GradientPaint gradient = new GradientPaint(
                    0, 0, lightBlue,
                    0, getHeight(), primaryBlue);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
