<?php
include 'db.php';
header('Content-Type: application/json');

// Optional filter by user_id
$user_id = isset($_GET['user_id']) ? $_GET['user_id'] : null;

if ($user_id) {
    $stmt = $conn->prepare("SELECT 
                                o.order_id,
                                o.user_id,
                                o.order_date,
                                o.status AS order_status,
                                o.total_price,
                                u.name AS customer_name,
                                u.address,
                                d.delivery_id,
                                d.current_status AS delivery_status
                            FROM orders o
                            JOIN users u ON o.user_id = u.user_id
                            LEFT JOIN delivery d ON o.order_id = d.order_id
                            WHERE o.user_id = ?
                            ORDER BY o.order_date ASC");
    $stmt->bind_param("i", $user_id);
} else {
    $stmt = $conn->prepare("SELECT 
                                o.order_id,
                                o.user_id,
                                o.order_date,
                                o.status AS order_status,
                                o.total_price,
                                u.name AS customer_name,
                                u.address,
                                d.delivery_id,
                                d.current_status AS delivery_status
                            FROM orders o
                            JOIN users u ON o.user_id = u.user_id
                            LEFT JOIN delivery d ON o.order_id = d.order_id
                            ORDER BY o.order_date ASC");
}

$stmt->execute();
$result = $stmt->get_result();
$orders = [];

while ($row = $result->fetch_assoc()) {
    $order_id = $row['order_id'];

    // Get items for this order
    $items_stmt = $conn->prepare("SELECT ep.name, oi.quantity 
                                  FROM order_items oi 
                                  JOIN egg_products ep ON oi.egg_id = ep.egg_id 
                                  WHERE oi.order_id = ?");
    $items_stmt->bind_param("i", $order_id);
    $items_stmt->execute();
    $items_result = $items_stmt->get_result();

    $items = [];
    while ($item = $items_result->fetch_assoc()) {
        $items[] = $item['name'] . " x" . $item['quantity'];
    }
    $row['items'] = implode(", ", $items);

    $orders[] = $row;
}

echo json_encode($orders);
$conn->close();
?>
