package Seller;

import java.awt.*;
import javax.swing.*;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailsPage {
    private JFrame frame;
    private JTextField textFieldOrder;
    private JTextField textFieldCustomer;
    private JTextField textFieldAddress;
    private int deliveryId;

    public DetailsPage(int orderId, String customer, String address, int deliveryId) {
        this.deliveryId = deliveryId;
        initialize();
        textFieldOrder.setText(String.valueOf(orderId));
        textFieldCustomer.setText(customer);
        textFieldAddress.setText(address);
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(240, 150, 96));
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblTitle = new JLabel("ORDER DETAILS");
        lblTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        lblTitle.setBounds(248, 34, 258, 43);
        frame.getContentPane().add(lblTitle);

     // Back button (directly go to ViewOrder GUI without confirmation)
        JButton btnBack = new JButton(new ImageIcon(new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\BACK_4.png")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        btnBack.setBounds(10, 11, 137, 120);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setOpaque(false);
        btnBack.addActionListener(e -> {
        	frame.dispose();
        	ViewOrder viewOrder = new ViewOrder(3);
        	viewOrder.showWindow();
        	});
        frame.getContentPane().add(btnBack);

        // Delete/Exit button
        JButton btnDelete = new JButton(new ImageIcon(new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\Delete2R.png")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        btnDelete.setBounds(668, 0, 118, 102);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setOpaque(false);
        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit the system?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(btnDelete);

        // Labels and fields
        JLabel lblOrder = new JLabel("ORDER ID:");
        lblOrder.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        lblOrder.setBounds(62, 127, 111, 28);
        frame.getContentPane().add(lblOrder);

        JLabel lblCustomer = new JLabel("CUSTOMER:");
        lblCustomer.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        lblCustomer.setBounds(62, 180, 126, 28);
        frame.getContentPane().add(lblCustomer);

        JLabel lblAddress = new JLabel("ADDRESS:");
        lblAddress.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        lblAddress.setBounds(64, 233, 137, 28);
        frame.getContentPane().add(lblAddress);

        textFieldOrder = new JTextField();
        textFieldOrder.setBounds(197, 131, 308, 28);
        textFieldOrder.setEditable(false);
        frame.getContentPane().add(textFieldOrder);

        textFieldCustomer = new JTextField();
        textFieldCustomer.setBounds(198, 184, 308, 28);
        textFieldCustomer.setEditable(false);
        frame.getContentPane().add(textFieldCustomer);

        textFieldAddress = new JTextField();
        textFieldAddress.setBounds(197, 237, 309, 90);
        textFieldAddress.setEditable(false);
        frame.getContentPane().add(textFieldAddress);

        // MAPS button (functionality pending)
        JButton btnMaps = new JButton("MAPS");
        btnMaps.setBackground(new Color(255, 255, 128));
        btnMaps.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        btnMaps.setBounds(204, 357, 111, 28);
        btnMaps.addActionListener(e -> {
            try {
                String rawAddress = textFieldAddress.getText();
                String encodedAddress = java.net.URLEncoder.encode(rawAddress, "UTF-8");
                String url = "https://www.google.com/maps/dir/?api=1&destination=" + encodedAddress;
                Desktop.getDesktop().browse(new URL(url).toURI());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Unable to open Google Maps.");
            }
        });
        frame.getContentPane().add(btnMaps);

        // DELIVERED button
        JButton btnDelivered = new JButton("DELIVERED");
        btnDelivered.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        btnDelivered.setBackground(new Color(255, 255, 128));
        btnDelivered.setBounds(325, 357, 181, 30);
        btnDelivered.addActionListener(e -> {
            try {
                URL url = new URL("http://localhost/eggdelivery/update_delivery_status.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // JSON body with delivery_id and status
                String jsonInput = String.format("{\"delivery_id\": %d, \"status\": \"Delivered\"}", deliveryId);

                try (DataOutputStream os = new DataOutputStream(conn.getOutputStream())) {
                    os.writeBytes(jsonInput);
                    os.flush();
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    JOptionPane.showMessageDialog(frame, "Status was updated successfully.");
                    // Optionally: you could disable the button so users can't click again
                    btnDelivered.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to update status. Server responded with code: " + responseCode);
                }

                conn.disconnect();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        frame.getContentPane().add(btnDelivered);
    }
}