package Seller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

public class ViewOrder {
    private JFrame frame;
    private JTable table;
    private int sellerId;

    public ViewOrder(int sellerId) {
        this.sellerId = sellerId;
        initialize();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 900, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(new Color(240, 150, 96));

        JLabel lblView = new JLabel("VIEW ORDER");
        lblView.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        lblView.setBounds(320, 24, 300, 72);
        frame.getContentPane().add(lblView);

        JButton btnBack = new JButton(new ImageIcon(new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\BACK_4.png")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        btnBack.setBounds(0, 0, 137, 120);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> {
            LoginSeller.main(null);
            frame.dispose();
        });
        frame.getContentPane().add(btnBack);

        JButton btnExit = new JButton(new ImageIcon(new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\Delete2R.png")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        btnExit.setBounds(725, 0, 118, 102);
        btnExit.setContentAreaFilled(false);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(btnExit);

        String[] columnNames = {"Delivery ID", "Order ID", "Customer", "Items", "Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 160, 780, 300);
        frame.getContentPane().add(scrollPane);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        table.getColumn("Action").setCellRenderer((tbl, val, isSelected, hasFocus, row, col) -> (Component) val);
        table.getColumn("Action").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            public Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected, int row, int column) {
                return (Component) value;
            }
        });

        loadData(model);
    }

    private void loadData(DefaultTableModel model) {
        try {
            URL url = new URL("http://localhost/eggdelivery/get_all_orders.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            JSONArray orders = new JSONArray(sb.toString());
            for (int i = 0; i < orders.length(); i++) {
                JSONObject o = orders.getJSONObject(i);

                int deliveryId = o.has("delivery_id") && !o.isNull("delivery_id") ? o.getInt("delivery_id") : 0;
                int orderId = o.getInt("order_id");
                String customer = o.getString("customer_name");
                String items = o.getString("items");
                String status = o.optString("order_status", "N/A");
                String address = o.optString("address", "");

                JButton actionButton;

                if (status.equals("Pending")) {
                    actionButton = new JButton("Prepare Order");
                    actionButton.addActionListener(e -> {
                        updateStatus("http://localhost/eggdelivery/update_order_status.php", orderId, "Preparing");
                    });
                } else if (status.equals("Preparing")) {
                    actionButton = new JButton("On The Way");
                    String finalAddress = address;
                    actionButton.addActionListener(e -> {
                        try {
                            String apiKey = getApiKey();
                            double[] coords = getLatLngFromAddress(finalAddress, apiKey);
                            if (coords == null) {
                                JOptionPane.showMessageDialog(frame, "Failed to get location from address.");
                                return;
                            }
                            updateOnTheWay(orderId, coords[0], coords[1]);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                        }
                    });
                } else if (status.equals("OnTheWay")) {
                    actionButton = new JButton("Details");
                    actionButton.addActionListener(e -> {
                        new DetailsPage(orderId, customer, address, deliveryId);
                        frame.dispose();
                    });
                } else {
                    actionButton = new JButton("-");
                    actionButton.setEnabled(false);
                }

                model.addRow(new Object[]{deliveryId, orderId, customer, items, status, actionButton});
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void updateStatus(String endpointUrl, int orderId, String newStatus) {
        try {
            URL url = new URL(endpointUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String postData = "order_id=" + orderId;
            if (newStatus != null) {
                postData += "&new_status=" + newStatus;
            }

            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String input;
            while ((input = in.readLine()) != null) {
                response.append(input);
            }
            in.close();

            JSONObject res = new JSONObject(response.toString());
            if (res.getBoolean("success")) {
                JOptionPane.showMessageDialog(frame, "Order status updated successfully.");
                frame.dispose();
                new ViewOrder(sellerId).showWindow();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed: " + res.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating status: " + e.getMessage());
        }
    }

    private void updateOnTheWay(int orderId, double lat, double lng) throws IOException {
        URL url = new URL("http://localhost/eggdelivery/update_on_the_way.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String postData = "order_id=" + orderId + "&location_lat=" + lat + "&location_lng=" + lng;
        OutputStream os = conn.getOutputStream();
        os.write(postData.getBytes());
        os.flush();
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String input;
        while ((input = in.readLine()) != null) {
            response.append(input);
        }
        in.close();

        JSONObject res = new JSONObject(response.toString());
        if (res.getBoolean("success")) {
            JOptionPane.showMessageDialog(frame, "Order is now On The Way with location saved.");
            frame.dispose();
            new ViewOrder(sellerId).showWindow();
        } else {
            JOptionPane.showMessageDialog(frame, "Failed: " + res.getString("message"));
        }
    }

    private String getApiKey() throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        }
        return props.getProperty("GOOGLE_MAPS_API_KEY");
    }

    private double[] getLatLngFromAddress(String address, String apiKey) throws Exception {
        String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
        String urlStr = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "&key=" + apiKey;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject json = new JSONObject(response.toString());
        if (json.getString("status").equals("OK")) {
            JSONObject location = json.getJSONArray("results")
                                      .getJSONObject(0)
                                      .getJSONObject("geometry")
                                      .getJSONObject("location");
            return new double[]{location.getDouble("lat"), location.getDouble("lng")};
        }

        return null;
    }
}