package buyer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GUIOrderBuyer {
	private JFrame frame;
	private int qtyKampung = 0, qtyBrown = 0, qtyMixed = 0;
	private double priceKampung = 12.00, priceBrown = 10.50, priceMixed = 9.00;

	private JLabel lblSubtotal;
	private JLabel lblQtyKampung, lblQtyBrown, lblQtyMixed;
	private JCheckBox chkKampung, chkBrown, chkMixed;
	private int userId;

	public GUIOrderBuyer(int userId) {
		this.userId = userId;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(240, 150, 96));
		frame.setBounds(100, 100, 800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);

		JButton btnBack = new JButton(new ImageIcon(new ImageIcon("C:/personal/UTeM/y2 sem2/App Dev/proj/BACK_4.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		btnBack.setBounds(10, 11, 137, 120);
		btnBack.setContentAreaFilled(false);
		btnBack.setBorderPainted(false);
		btnBack.setFocusPainted(false);
		btnBack.setOpaque(false);
		btnBack.addActionListener(e -> {
			frame.dispose();
			new GUIHomePageBuyer(userId);
		});
		frame.getContentPane().add(btnBack);

		JButton btnDelete = new JButton(new ImageIcon(new ImageIcon("C:/personal/UTeM/y2 sem2/App Dev/proj/Delete2R.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		btnDelete.setBounds(668, 0, 118, 102);
		btnDelete.setContentAreaFilled(false);
		btnDelete.setBorderPainted(false);
		btnDelete.setFocusPainted(false);
		btnDelete.setOpaque(false);
		btnDelete.addActionListener(e -> System.exit(0));
		frame.getContentPane().add(btnDelete);

		chkKampung = new JCheckBox("Ayam Kampung - RM12.00");
		chkKampung.setBounds(50, 150, 200, 25);
		frame.getContentPane().add(chkKampung);

		chkBrown = new JCheckBox("Brown XL - RM10.50");
		chkBrown.setBounds(50, 200, 200, 25);
		frame.getContentPane().add(chkBrown);

		chkMixed = new JCheckBox("Mixed Tray - RM9.00");
		chkMixed.setBounds(50, 250, 200, 25);
		frame.getContentPane().add(chkMixed);

		lblQtyKampung = new JLabel("0");
		lblQtyKampung.setBounds(300, 150, 30, 25);
		frame.getContentPane().add(lblQtyKampung);

		JButton btnAddKampung = new JButton("+");
		btnAddKampung.setBounds(340, 150, 45, 25);
		btnAddKampung.addActionListener(e -> updateQty("kampung", 1));
		frame.getContentPane().add(btnAddKampung);

		JButton btnSubKampung = new JButton("-");
		btnSubKampung.setBounds(390, 150, 45, 25);
		btnSubKampung.addActionListener(e -> updateQty("kampung", -1));
		frame.getContentPane().add(btnSubKampung);

		lblQtyBrown = new JLabel("0");
		lblQtyBrown.setBounds(300, 200, 30, 25);
		frame.getContentPane().add(lblQtyBrown);

		JButton btnAddBrown = new JButton("+");
		btnAddBrown.setBounds(340, 200, 45, 25);
		btnAddBrown.addActionListener(e -> updateQty("brown", 1));
		frame.getContentPane().add(btnAddBrown);

		JButton btnSubBrown = new JButton("-");
		btnSubBrown.setBounds(390, 200, 45, 25);
		btnSubBrown.addActionListener(e -> updateQty("brown", -1));
		frame.getContentPane().add(btnSubBrown);

		lblQtyMixed = new JLabel("0");
		lblQtyMixed.setBounds(300, 250, 30, 25);
		frame.getContentPane().add(lblQtyMixed);

		JButton btnAddMixed = new JButton("+");
		btnAddMixed.setBounds(340, 250, 45, 25);
		btnAddMixed.addActionListener(e -> updateQty("mixed", 1));
		frame.getContentPane().add(btnAddMixed);

		JButton btnSubMixed = new JButton("-");
		btnSubMixed.setBounds(390, 250, 45, 25);
		btnSubMixed.addActionListener(e -> updateQty("mixed", -1));
		frame.getContentPane().add(btnSubMixed);

		lblSubtotal = new JLabel("Subtotal: RM 0.00");
		lblSubtotal.setBounds(50, 320, 200, 25);
		frame.getContentPane().add(lblSubtotal);

		JButton btnConfirm = new JButton("Confirm Order");
		btnConfirm.setBounds(50, 370, 200, 35);
		btnConfirm.addActionListener(this::submitOrder);
		frame.getContentPane().add(btnConfirm);
	}

	private void updateQty(String type, int change) {
		if (type.equals("kampung")) {
			qtyKampung = Math.max(0, qtyKampung + change);
			lblQtyKampung.setText(String.valueOf(qtyKampung));
		} else if (type.equals("brown")) {
			qtyBrown = Math.max(0, qtyBrown + change);
			lblQtyBrown.setText(String.valueOf(qtyBrown));
		} else if (type.equals("mixed")) {
			qtyMixed = Math.max(0, qtyMixed + change);
			lblQtyMixed.setText(String.valueOf(qtyMixed));
		}
		lblSubtotal.setText("Subtotal: RM " + String.format("%.2f", calculateSubtotal()));
	}

	private double calculateSubtotal() {
		double total = 0.0;
		if (chkKampung.isSelected()) total += qtyKampung * priceKampung;
		if (chkBrown.isSelected()) total += qtyBrown * priceBrown;
		if (chkMixed.isSelected()) total += qtyMixed * priceMixed;
		return total;
	}

	private void submitOrder(ActionEvent e) {
		try {
			URL url = new URL("http://localhost/eggdelivery/place_order.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);

			StringBuilder postData = new StringBuilder();
			postData.append("user_id=").append(userId);

			// Send selected items & quantities
			if (chkKampung.isSelected() && qtyKampung > 0)
				postData.append("&kampung=").append(qtyKampung);
			if (chkBrown.isSelected() && qtyBrown > 0)
				postData.append("&brown=").append(qtyBrown);
			if (chkMixed.isSelected() && qtyMixed > 0)
				postData.append("&mixed=").append(qtyMixed);

			OutputStream os = conn.getOutputStream();
			os.write(postData.toString().getBytes());
			os.flush();
			os.close();

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				JOptionPane.showMessageDialog(frame, "Order placed successfully!");
				frame.dispose();
				new GUIHomePageBuyer(userId);
			} else {
				JOptionPane.showMessageDialog(frame, "Server error. Response code: " + responseCode);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
		}
	}
}