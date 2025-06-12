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
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel background = new JLabel(new ImageIcon("Assets/Bg.png"));
        background.setLayout(new GridBagLayout());
        add(background);

        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        formPanel.setOpaque(false);

        emailField = new JTextField();
        passwordField = new JPasswordField();

        formPanel.add(new JLabel("Email atau Nomor Telepon"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password"));
        formPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        loginButton = new JButton("Masuk");
        registerButton = new JButton("Daftar");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formPanel.add(buttonPanel);

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
                JOptionPane.showMessageDialog(this, "Email/No. Telp atau Password salah", "Error", JOptionPane.ERROR_MESSAGE);
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
        setTitle("Daftar Akun");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel background = new JLabel(new ImageIcon("Assets/Bg.png"));
        background.setLayout(new GridBagLayout());
        add(background);

        JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        formPanel.setOpaque(false);

        namaField = new JTextField();
        emailField = new JTextField();
        telpField = new JTextField();
        passwordField = new JPasswordField();
        maleRadio = new JRadioButton("Laki-laki");
        femaleRadio = new JRadioButton("Perempuan");
        domisiliField = new JTextField();
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        formPanel.add(new JLabel("Nama Lengkap"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Email"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Nomor Telepon"));
        formPanel.add(telpField);
        formPanel.add(new JLabel("Password"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Domisili"));
        formPanel.add(domisiliField);

        JPanel genderPanel = new JPanel(new FlowLayout());
        genderPanel.setOpaque(false);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(new JLabel("Jenis Kelamin"));
        formPanel.add(genderPanel);

        registerButton = new JButton("Daftar");
        formPanel.add(registerButton);

        background.add(formPanel);

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

        if (nama.isEmpty() || email.isEmpty() || telp.isEmpty() || password.isEmpty() || gender.isEmpty() || domisili.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi semua data.", "Peringatan", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Email atau No. Telepon sudah terdaftar.", "Gagal", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
