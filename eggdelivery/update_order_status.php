<?php
include 'db.php';
header('Content-Type: application/json');

$order_id = $_POST['order_id'] ?? null;
$new_status = $_POST['new_status'] ?? null;

if (!$order_id || !$new_status) {
    echo json_encode(["success" => false, "message" => "Missing order_id or new_status"]);
    exit;
}

$update = $conn->prepare("UPDATE orders SET status = ? WHERE order_id = ?");
$update->bind_param("si", $new_status, $order_id);
$success = $update->execute();

if ($success) {
    echo json_encode(["success" => true, "message" => "Status updated"]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to update status"]);
}

$conn->close();
?>
