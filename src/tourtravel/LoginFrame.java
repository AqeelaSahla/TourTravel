package tourtravel;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import util.HomeFrame;
import util.KoneksiDB;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());

        // Create scaled background
        ImageIcon originalIcon = new ImageIcon("Assets/Bg.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(900, 700, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setLayout(new GridBagLayout());
        add(background); // Create form panel with better layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(new Color(0, 0, 0, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Create and style components
        emailField = new JTextField(25);
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));

        passwordField = new JPasswordField(25);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14)); // Add LOGIN title
        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 40, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc); // Add email label and field
        JLabel emailLabel = new JLabel("Email atau Nomor Telepon");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(emailField, gbc); // Add password label and field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(passwordField, gbc);

        // Add button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Masuk");
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        registerButton = new JButton("Daftar");
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(100, 149, 237));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 15, 15);
        formPanel.add(buttonPanel, gbc);

        background.add(formPanel);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }

    private void login() {
        String input = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT * FROM [USER] WHERE (email = ? OR no_telepon = ?) AND password = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, input);
            ps.setString(2, input);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nama = rs.getString("nama");
                int userId = rs.getInt("id_user");
                JOptionPane.showMessageDialog(this, "Login berhasil!");
                dispose();
                new HomeFrame(userId, nama).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Email/No. Telp atau Password salah", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

class RegisterFrame extends JFrame {

    private JTextField namaField, emailField, telpField, domisiliField;
    private JPasswordField passwordField;
    private JRadioButton maleRadio, femaleRadio;
    private JButton registerButton;

    public RegisterFrame() {
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());

        // Create scaled background
        ImageIcon originalIcon = new ImageIcon("Assets/Bg.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(900, 700, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setLayout(new BorderLayout());
        add(background); // Create main wrapper panel to center the form
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setOpaque(false);

        // Create form panel with better layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.setPreferredSize(new Dimension(400, 500));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Add top spacer to push form down
        JLabel topSpacer = new JLabel(" ");
        topSpacer.setPreferredSize(new Dimension(1, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(topSpacer, gbc);

        // Reset insets for form components
        gbc.insets = new Insets(6, 10, 6, 10); // Create and style components with consistent sizing
        namaField = new JTextField(25);
        namaField.setPreferredSize(new Dimension(300, 40));
        namaField.setFont(new Font("Arial", Font.PLAIN, 14));

        emailField = new JTextField(25);
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));

        telpField = new JTextField(25);
        telpField.setPreferredSize(new Dimension(300, 40));
        telpField.setFont(new Font("Arial", Font.PLAIN, 14));

        passwordField = new JPasswordField(25);
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        domisiliField = new JTextField(25);
        domisiliField.setPreferredSize(new Dimension(300, 40));
        domisiliField.setFont(new Font("Arial", Font.PLAIN, 14));
        maleRadio = new JRadioButton("Laki-laki");
        maleRadio.setOpaque(false);
        maleRadio.setForeground(new Color(30, 30, 30));
        maleRadio.setFont(new Font("Arial", Font.BOLD, 14));

        femaleRadio = new JRadioButton("Perempuan");
        femaleRadio.setOpaque(false);
        femaleRadio.setForeground(new Color(30, 30, 30));
        femaleRadio.setFont(new Font("Arial", Font.BOLD, 14));
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio); // Add form components with labels
        JLabel namaLabel = new JLabel("Nama Lengkap");
        namaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        namaLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 3, 15);
        formPanel.add(namaLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 6, 15);
        formPanel.add(namaField, gbc);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 3, 15);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 6, 15);
        formPanel.add(emailField, gbc);
        JLabel telpLabel = new JLabel("Nomor Telepon");
        telpLabel.setFont(new Font("Arial", Font.BOLD, 14));
        telpLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 3, 15);
        formPanel.add(telpLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 6, 15);
        formPanel.add(telpField, gbc);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 3, 15);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 6, 15);
        formPanel.add(passwordField, gbc);
        JLabel domisiliLabel = new JLabel("Domisili");
        domisiliLabel.setFont(new Font("Arial", Font.BOLD, 14));
        domisiliLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 3, 15);
        formPanel.add(domisiliLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 6, 15);
        formPanel.add(domisiliField, gbc);
        JLabel genderLabel = new JLabel("Jenis Kelamin");
        genderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        genderLabel.setForeground(new Color(50, 50, 50));
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 3, 15);
        formPanel.add(genderLabel, gbc);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        genderPanel.setOpaque(false);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 8, 15);
        formPanel.add(genderPanel, gbc);

        registerButton = new JButton("Daftar");
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 10, 15);
        formPanel.add(registerButton, gbc);// Add the form panel to wrapper panel and then to background
        wrapperPanel.add(formPanel);
        background.add(wrapperPanel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> register());

        setVisible(true);
    }

    private void register() {
        String nama = namaField.getText().trim();
        String email = emailField.getText().trim();
        String telp = telpField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String gender = maleRadio.isSelected() ? "Laki-laki" : femaleRadio.isSelected() ? "Perempuan" : "";
        char gender2 = gender.charAt(0);
        String gender3 = String.valueOf(gender2);
        String domisili = domisiliField.getText().trim();

        if (nama.isEmpty() || email.isEmpty() || telp.isEmpty() || password.isEmpty() || gender.isEmpty()
                || domisili.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi semua data.", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = KoneksiDB.getConnection()) {
            // cek apakah email atau no telp sudah ada
            String checkQuery = "SELECT * FROM [USER] WHERE email = ? OR no_telepon = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setString(2, telp);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Email atau No. Telepon sudah terdaftar.", "Gagal",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String insert = "INSERT INTO [USER](nama, email, no_telepon, password, gender, domisili) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nama);
            ps.setString(2, email);
            ps.setString(3, telp);
            ps.setString(4, password);
            ps.setString(5, gender3);
            ps.setString(6, domisili);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int userId = keys.getInt(1);
                JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!");
                dispose();
                new HomeFrame(userId).setVisible(true);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
