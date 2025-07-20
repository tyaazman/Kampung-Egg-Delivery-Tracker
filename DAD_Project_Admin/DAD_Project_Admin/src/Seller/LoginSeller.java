package Seller;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginSeller {
    private JFrame frame;
    private JTextField textFielduser;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginSeller window = new LoginSeller();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LoginSeller() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Seller Login");
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null); // Use absolute layout

        // Add background label FIRST
        ImageIcon bgIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\sellerloginbg.png");
        Image img = bgIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
        JLabel bgLabel = new JLabel(new ImageIcon(img));
        bgLabel.setBounds(0, 0, 800, 500);
        frame.getContentPane().add(bgLabel);
        bgLabel.setLayout(null);

        textFielduser = new JTextField();
        textFielduser.setBounds(355, 195, 184, 25);
        bgLabel.add(textFielduser);

        passwordField = new JPasswordField();
        passwordField.setBounds(355, 226, 184, 25);
        bgLabel.add(passwordField);

        JButton btnEnter = new JButton("ENTER");
        btnEnter.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        btnEnter.setBackground(new Color(255, 255, 128));
        btnEnter.setBounds(467, 328, 121, 32);
        bgLabel.add(btnEnter);

        btnEnter.addActionListener(e -> {
            String username = textFielduser.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both username and password.");
                return;
            }

            try {
                URL url = new URL("http://localhost/eggdelivery/login_seller.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("username", username);
                payload.put("password", password);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.toString().getBytes());
                    os.flush();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseStr = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseStr.append(line);
                }
                reader.close();

                JSONObject response = new JSONObject(responseStr.toString());
                if (response.getBoolean("success")) {
                    int sellerId = response.getInt("user_id");
                    String name = response.getString("name");

                    JOptionPane.showMessageDialog(frame, "Welcome, " + name + "!");
                    frame.dispose();
                    ViewOrder view = new ViewOrder(sellerId);
                    view.showWindow();
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed: " + response.optString("message", "Unknown error"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Login error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}