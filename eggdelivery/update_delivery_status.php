<?php
include 'db.php';
header('Content-Type: application/json');

// Read JSON body from input
$input = json_decode(file_get_contents("php://input"), true);

$delivery_id = $input['delivery_id'] ?? null;
$new_status = $input['status'] ?? null;

if (!$delivery_id || !$new_status) {
    echo json_encode(["success" => false, "message" => "Missing delivery_id or status"]);
    exit;
}

// Step 1: Update delivery table
$updateDelivery = $conn->prepare("UPDATE delivery SET current_status = ? WHERE delivery_id = ?");
$updateDelivery->bind_param("si", $new_status, $delivery_id);
$successDelivery = $updateDelivery->execute();

// Step 2: Get related order_id
$getOrderId = $conn->prepare("SELECT order_id FROM delivery WHERE delivery_id = ?");
$getOrderId->bind_param("i", $delivery_id);
$getOrderId->execute();
$result = $getOrderId->get_result();
$order = $result->fetch_assoc();
$order_id = $order['order_id'] ?? null;

// Step 3: Update orders.status if order_id found
$successOrder = false;
if ($order_id) {
    $updateOrder = $conn->prepare("UPDATE orders SET status = ? WHERE order_id = ?");
    $updateOrder->bind_param("si", $new_status, $order_id);
    $successOrder = $updateOrder->execute();
}

if ($successDelivery && $successOrder) {
    echo json_encode(["success" => true, "message" => "Status updated for delivery and order"]);
} else {
    echo json_encode(["success" => false, "message" => "Failed to update one or both tables"]);
}

$conn->close();
?>
