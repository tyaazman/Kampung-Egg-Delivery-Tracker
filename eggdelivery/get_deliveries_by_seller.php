<?php
include 'db.php';
header('Content-Type: application/json');

$user_id = isset($_GET['user_id']) ? $_GET['user_id'] : null;

if ($user_id) {
    $stmt = $conn->prepare("SELECT d.delivery_id, d.order_id, d.user_id, u.name AS customer_name, u.address, d.current_status, o.order_date
                            FROM delivery d
                            JOIN orders o ON d.order_id = o.order_id
                            JOIN users u ON o.user_id = u.user_id
                            WHERE d.user_id = ?
                            ORDER BY o.order_date ASC");
    $stmt->bind_param("i", $user_id);
} else {
    $stmt = $conn->prepare("SELECT d.delivery_id, d.order_id, d.user_id, u.name AS customer_name, u.address, d.current_status, o.order_date
                            FROM delivery d
                            JOIN orders o ON d.order_id = o.order_id
                            JOIN users u ON o.user_id = u.user_id
                            ORDER BY o.order_date ASC");
}

$stmt->execute();
$result = $stmt->get_result();
$deliveries = [];

while ($row = $result->fetch_assoc()) {
    $order_id = $row['order_id'];
    $items_query = $conn->prepare("SELECT ep.name, oi.quantity FROM order_items oi JOIN egg_products ep ON oi.egg_id = ep.egg_id WHERE oi.order_id = ?");
    $items_query->bind_param("i", $order_id);
    $items_query->execute();
    $items_result = $items_query->get_result();
    
    $items = [];
    while ($item = $items_result->fetch_assoc()) {
        $items[] = $item['name'] . " x" . $item['quantity'];
    }
    $row['items'] = implode(", ", $items);

    $deliveries[] = $row;
}

echo json_encode($deliveries);
$conn->close();
?>
