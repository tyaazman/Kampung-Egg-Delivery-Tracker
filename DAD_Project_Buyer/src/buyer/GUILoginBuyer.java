package buyer;

import buyer.GUIHomePageBuyer;
import buyer.GUINewUserBuyer;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class GUILoginBuyer {

	private JFrame frame;
	private JTextField txtEmail;
	private JPasswordField txtPassword;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				GUILoginBuyer window = new GUILoginBuyer();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public GUILoginBuyer() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setSize(800, 500);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);

		ImageIcon bgIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\cust login.png");
		Image img = bgIcon.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH);
		ImageIcon scaledBg = new ImageIcon(img);
		JLabel bgLabel = new JLabel(scaledBg);
		bgLabel.setBounds(0, 0, 800, 500);
		bgLabel.setLayout(null);
		frame.getContentPane().add(bgLabel);

		txtEmail = new JTextField();
		txtEmail.setBounds(306, 154, 300, 25);
		txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtEmail.setToolTipText("Email");
		bgLabel.add(txtEmail);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(306, 184, 300, 25);
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtPassword.setToolTipText("Password");
		bgLabel.add(txtPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.setBackground(new Color(255, 255, 128));
		btnLogin.setBounds(221, 224, 140, 35);
		btnLogin.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		bgLabel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				    String email = txtEmail.getText().trim();
				    String password = new String(txtPassword.getPassword()).trim();

				    // Validate input locally (optional)
				    if (email.isEmpty() || password.isEmpty()) {
				        JOptionPane.showMessageDialog(frame, "Please fill in both email and password.");
				        return;
				    }

				    // Correct URL with encoded params
				    String urlStr = "http://localhost/eggdelivery/login_buyer.php?email="
				            + java.net.URLEncoder.encode(email, "UTF-8")
				            + "&password="
				            + java.net.URLEncoder.encode(password, "UTF-8");

				    URL url = new URL(urlStr);
				    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				    conn.setRequestMethod("GET");

				    int responseCode = conn.getResponseCode();
				    if (responseCode == HttpURLConnection.HTTP_OK) {
				        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				        String inputLine;
				        StringBuffer response = new StringBuffer();
				        while ((inputLine = in.readLine()) != null) {
				            response.append(inputLine);
				        }
				        in.close();

				        JSONObject jsonResponse = new JSONObject(response.toString());
				        if (jsonResponse.getBoolean("success")) {
				            int userId = jsonResponse.getInt("user_id");
				            String name = jsonResponse.getString("name");

				            JOptionPane.showMessageDialog(frame, "Welcome, " + name + "!");

				            // Navigate to home screen
				            GUIHomePageBuyer home = new GUIHomePageBuyer(userId);
				            frame.dispose();

				        } else {
				            JOptionPane.showMessageDialog(frame, "Invalid login. Please try again.");
				        }
				    } else {
				        JOptionPane.showMessageDialog(frame, "Error connecting to server. HTTP code: " + responseCode);
				    }

				} catch (Exception ex) {
				    ex.printStackTrace();
				    JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage());
				}

			}
		});

		JButton btnNewUser = new JButton("New User");
		btnNewUser.setBackground(new Color(255, 255, 128));
		btnNewUser.setBounds(403, 224, 140, 35);
		btnNewUser.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		bgLabel.add(btnNewUser);
		btnNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				GUINewUserBuyer newUser = new GUINewUserBuyer();
			}
		});
	}
}