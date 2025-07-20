<?php
include 'db.php';
header('Content-Type: application/json');

$order_id = $_POST['order_id'] ?? null;
$lat = $_POST['location_lat'] ?? null;
$lng = $_POST['location_lng'] ?? null;

if (!$order_id || !$lat || !$lng) {
    echo json_encode(["success" => false, "message" => "Missing order_id or location"]);
    exit;
}

try {
    $conn->begin_transaction();

    // Step 1: Update order status
    $updateOrder = $conn->prepare("UPDATE orders SET status = 'OnTheWay' WHERE order_id = ?");
    $updateOrder->bind_param("i", $order_id);
    $updateOrder->execute();

    // Step 2: Check if delivery already exists
    $check = $conn->prepare("SELECT delivery_id FROM delivery WHERE order_id = ?");
    $check->bind_param("i", $order_id);
    $check->execute();
    $result = $check->get_result();

    if ($result->num_rows === 0) {
        // Get user_id only
        $userStmt = $conn->prepare("SELECT user_id FROM orders WHERE order_id = ?");
        $userStmt->bind_param("i", $order_id);
        $userStmt->execute();
        $userRes = $userStmt->get_result()->fetch_assoc();
        $user_id = $userRes['user_id'];

        // Insert delivery info with coordinates
        $insertDelivery = $conn->prepare("INSERT INTO delivery (order_id, user_id, current_status, location_lat, location_lng) VALUES (?, ?, 'OnTheWay', ?, ?)");
        $insertDelivery->bind_param("iidd", $order_id, $user_id, $lat, $lng);
        $insertDelivery->execute();
    } else {
        // Just update delivery status
        $updateDelivery = $conn->prepare("UPDATE delivery SET current_status = 'OnTheWay' WHERE order_id = ?");
        $updateDelivery->bind_param("i", $order_id);
        $updateDelivery->execute();
    }

    $conn->commit();
    echo json_encode(["success" => true, "message" => "Delivery marked OnTheWay with location saved"]);
} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(["success" => false, "message" => $e->getMessage()]);
}

$conn->close();
?>
