<?php
include 'db.php';
header('Content-Type: application/json');

$user_id = isset($_GET['user_id']) ? intval($_GET['user_id']) : null;

if (!$user_id) {
    echo json_encode(["success" => false, "message" => "Missing user_id"]);
    exit;
}

$stmt = $conn->prepare("SELECT order_id, order_date, total_price, status FROM orders WHERE user_id = ? ORDER BY order_date DESC");
$stmt->bind_param("i", $user_id);
$stmt->execute();
$result = $stmt->get_result();

$orders = [];
while ($row = $result->fetch_assoc()) {
    $orders[] = $row;
}

echo json_encode($orders);
$conn->close();
?>
