package buyer;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GUINewUserBuyer {

	private JFrame frame;
	private JTextField textFieldName;
	private JTextField textFieldEmail;
	private JTextField textFieldPhone;
	private JTextField textFieldAddress;
	private JPasswordField passwordField;
	private JPasswordField RepasswordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				GUINewUserBuyer window = new GUINewUserBuyer();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public GUINewUserBuyer() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(240, 150, 96));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		JLabel lblNewUser = new JLabel("New User");
		lblNewUser.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		lblNewUser.setBounds(305, 24, 152, 52);
		frame.getContentPane().add(lblNewUser);

		JLabel lblName = new JLabel("Name :");
		lblName.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblName.setBounds(151, 91, 64, 35);
		frame.getContentPane().add(lblName);

		JLabel lblEmail = new JLabel("Email :");
		lblEmail.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblEmail.setBounds(151, 136, 61, 35);
		frame.getContentPane().add(lblEmail);

		JLabel lblPass = new JLabel("Password :");
		lblPass.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblPass.setBounds(112, 181, 98, 35);
		frame.getContentPane().add(lblPass);

		JLabel lblRePass = new JLabel("Re-Password :");
		lblRePass.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblRePass.setBounds(80, 224, 130, 35);
		frame.getContentPane().add(lblRePass);

		JLabel lblPhone = new JLabel("Phone.No :");
		lblPhone.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblPhone.setBounds(112, 269, 98, 35);
		frame.getContentPane().add(lblPhone);

		JLabel lblAddress = new JLabel("Address :");
		lblAddress.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		lblAddress.setBounds(119, 314, 90, 35);
		frame.getContentPane().add(lblAddress);

		textFieldName = new JTextField();
		textFieldName.setBounds(222, 99, 384, 28);
		frame.getContentPane().add(textFieldName);

		textFieldEmail = new JTextField();
		textFieldEmail.setBounds(222, 144, 384, 28);
		frame.getContentPane().add(textFieldEmail);

		passwordField = new JPasswordField();
		passwordField.setBounds(220, 188, 384, 28);
		frame.getContentPane().add(passwordField);

		RepasswordField = new JPasswordField();
		RepasswordField.setBounds(220, 231, 384, 28);
		frame.getContentPane().add(RepasswordField);

		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(220, 277, 384, 28);
		frame.getContentPane().add(textFieldPhone);

		textFieldAddress = new JTextField();
		textFieldAddress.setBounds(219, 322, 384, 41);
		frame.getContentPane().add(textFieldAddress);

		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		btnClear.setBackground(new Color(255, 255, 128));
		btnClear.setBounds(410, 385, 140, 35);
		frame.getContentPane().add(btnClear);
		btnClear.addActionListener(e -> {
			textFieldName.setText("");
			textFieldEmail.setText("");
			textFieldPhone.setText("");
			textFieldAddress.setText("");
			passwordField.setText("");
			RepasswordField.setText("");
		});

		JButton btnSignup = new JButton("Sign Up");
		btnSignup.setBackground(new Color(255, 255, 128));
		btnSignup.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		btnSignup.setBounds(241, 385, 140, 35);
		frame.getContentPane().add(btnSignup);
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textFieldName.getText();
				String email = textFieldEmail.getText();
				String phone = textFieldPhone.getText();
				String address = textFieldAddress.getText();
				String pass = new String(passwordField.getPassword());
				String rePass = new String(RepasswordField.getPassword());

				if (!pass.equals(rePass)) {
					JOptionPane.showMessageDialog(frame, "Passwords do not match!");
					return;
				}

				try {
					String urlStr = "http://localhost/eggdelivery/register_buyer.php"
							+ "?name=" + URLEncoder.encode(name, "UTF-8")
							+ "&email=" + URLEncoder.encode(email, "UTF-8")
							+ "&password=" + URLEncoder.encode(pass, "UTF-8")
							+ "&phone=" + URLEncoder.encode(phone, "UTF-8")
							+ "&address=" + URLEncoder.encode(address, "UTF-8");

					URL url = new URL(urlStr);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");

					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						JOptionPane.showMessageDialog(frame, "Registration successful!");
						frame.dispose();
						new GUILoginBuyer();
					} else {
						JOptionPane.showMessageDialog(frame, "Registration failed!");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
				}
			}
		});

		// BACK BUTTON with egg icon
		ImageIcon eggIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\BACK_4.png");
		Image img = eggIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		JButton btnBack = new JButton(new ImageIcon(img));
		btnBack.addActionListener(e -> {
			frame.dispose();
			new GUILoginBuyer();
		});
		btnBack.setBounds(10, 11, 137, 120);
		btnBack.setContentAreaFilled(false);
		btnBack.setBorderPainted(false);
		btnBack.setFocusPainted(false);
		btnBack.setOpaque(false);
		frame.getContentPane().add(btnBack);

		// DELETE BUTTON
		ImageIcon deleteIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\Delete2R.png");
		Image deleteImg = deleteIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		JButton btnDelete = new JButton(new ImageIcon(deleteImg));
		btnDelete.addActionListener(e -> System.exit(0));
		btnDelete.setBounds(668, 0, 118, 102);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setFocusPainted(false);
		btnDelete.setOpaque(false);
		frame.getContentPane().add(btnDelete);
	}
}