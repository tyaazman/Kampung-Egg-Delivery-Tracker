package buyer;

import buyer.GUIHomePageBuyer;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class GUIHistoryBuyer {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private int userId;

    public GUIHistoryBuyer(int userId) {
        this.userId = userId;
        initialize();
        fetchOrderHistory();
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(240, 150, 96));
        frame.setBounds(100, 100, 800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        // Back Button
        ImageIcon eggIcon = new ImageIcon("C:\\personal\\UTeM\\y2 sem2\\App Dev\\proj\\BACK_4.png");
        Image Eggimg = eggIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(Eggimg);

        JButton btnBack = new JButton(scaledIcon);
        btnBack.addActionListener(e -> {
            frame.dispose();
            new GUIHomePageBuyer(userId);
        });
        btnBack.setBounds(10, 11, 137, 120);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setOpaque(false);
        frame.getContentPane().add(btnBack);

        // Delete Button
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

        // Table setup
        String[] columnNames = {"Order ID", "Date", "Total (RM)", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 150, 680, 250);
        frame.getContentPane().add(scrollPane);

        // Refresh Button
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        btnRefresh.setBackground(new Color(255, 255, 128));
        btnRefresh.setBounds(50, 420, 120, 30);
        btnRefresh.addActionListener(e -> fetchOrderHistory());
        frame.getContentPane().add(btnRefresh);
    }

    private void fetchOrderHistory() {
        try {
            URL url = new URL("http://localhost/eggdelivery/get_order_history.php?user_id=" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();

            JSONArray orders = new JSONArray(content.toString());
            tableModel.setRowCount(0); // clear old rows

            for (int i = 0; i < orders.length(); i++) {
                JSONObject obj = orders.getJSONObject(i);
                tableModel.addRow(new Object[]{
                    obj.getInt("order_id"),
                    obj.getString("order_date"),
                    String.format("%.2f", obj.getDouble("total_price")),
                    obj.getString("status")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to load order history.");
        }
    }
}