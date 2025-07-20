
package buyer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIHomePageBuyer {
	private JFrame frame;
	private int userId;

	// Updated constructor with userId
	public GUIHomePageBuyer(int userId) {
		this.userId = userId;
		initialize();
	}

	// Main for testing only (optional)
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				new GUIHomePageBuyer(1); // Dummy ID for testing
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void initialize() {
		frame = new JFrame("Home Page");
		frame.getContentPane().setBackground(new Color(240, 150, 96));
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblHomePage = new JLabel("Home Page");
		lblHomePage.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
		lblHomePage.setBounds(297, 24, 177, 52);
		frame.getContentPane().add(lblHomePage);

		JButton btnOrder = new JButton("Order");
		btnOrder.setBackground(new Color(255, 255, 128));
		btnOrder.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		btnOrder.setBounds(272, 150, 210, 35);
		frame.getContentPane().add(btnOrder);
		btnOrder.addActionListener(e -> {
			frame.dispose();
			new GUIOrderBuyer(userId);
		});

		JButton btnHistory = new JButton("History");
		btnHistory.setBackground(new Color(255, 255, 128));
		btnHistory.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		btnHistory.setBounds(272, 214, 210, 35);
		frame.getContentPane().add(btnHistory);
		btnHistory.addActionListener(e -> {
			frame.dispose();
			new GUIHistoryBuyer(userId);
		});

		// BACK BUTTON
		ImageIcon eggIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\BACK_4.png");
		Image Eggimg = eggIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon scaledIcon = new ImageIcon(Eggimg);

		JButton btnBack = new JButton(scaledIcon);
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
		ImageIcon deleteScaled = new ImageIcon(deleteImg);

		JButton btnDelete = new JButton(deleteScaled);
		btnDelete.addActionListener(e -> System.exit(0));
		btnDelete.setBounds(668, 0, 118, 102);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setFocusPainted(false);
		btnDelete.setOpaque(false);
		frame.getContentPane().add(btnDelete);

		// Background image
		ImageIcon bgIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\aye-removebg-preview (1).png");
		Image img = bgIcon.getImage().getScaledInstance(800, 50, Image.SCALE_SMOOTH);
		ImageIcon scaledBg = new ImageIcon(img);
		JLabel bgLabel = new JLabel(scaledBg);
		bgLabel.setBounds(0, 166, 800, 500);
		bgLabel.setLayout(null);
		frame.getContentPane().add(bgLabel);

		frame.setVisible(true);
	}
}