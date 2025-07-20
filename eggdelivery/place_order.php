<?php
include 'db.php';
header('Content-Type: application/json');

// Show SQL errors for debugging
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

// Get POST values
$user_id = $_POST['user_id'] ?? null;
$qtyKampung = $_POST['kampung'] ?? 0;
$qtyBrown = $_POST['brown'] ?? 0;
$qtyMixed = $_POST['mixed'] ?? 0;

if (!$user_id || ($qtyKampung == 0 && $qtyBrown == 0 && $qtyMixed == 0)) {
    echo json_encode(["success" => false, "message" => "Missing user_id or no items selected"]);
    exit;
}

$conn->begin_transaction();

try {
    // Step 1: Insert into orders table (initially no total price)
    $insertOrder = $conn->prepare("INSERT INTO orders (user_id, status, order_date) VALUES (?, 'Pending', NOW())");
    $insertOrder->bind_param("i", $user_id);
    $insertOrder->execute();
    $order_id = $insertOrder->insert_id;

    // Step 2: Insert selected items into order_items and calculate subtotal
    $total_price = 0.0;

    if ($qtyKampung > 0) {
        $egg_id = 1;
        $subtotal = $qtyKampung * 12.00;
        $total_price += $subtotal;

        $insertItem = $conn->prepare("INSERT INTO order_items (order_id, egg_id, quantity, subtotal_price) VALUES (?, ?, ?, ?)");
        $insertItem->bind_param("iiid", $order_id, $egg_id, $qtyKampung, $subtotal);
        $insertItem->execute();
        $insertItem->close();
    }

    if ($qtyBrown > 0) {
        $egg_id = 2;
        $subtotal = $qtyBrown * 10.50;
        $total_price += $subtotal;

        $insertItem = $conn->prepare("INSERT INTO order_items (order_id, egg_id, quantity, subtotal_price) VALUES (?, ?, ?, ?)");
        $insertItem->bind_param("iiid", $order_id, $egg_id, $qtyBrown, $subtotal);
        $insertItem->execute();
        $insertItem->close();
    }

    if ($qtyMixed > 0) {
        $egg_id = 3;
        $subtotal = $qtyMixed * 9.00;
        $total_price += $subtotal;

        $insertItem = $conn->prepare("INSERT INTO order_items (order_id, egg_id, quantity, subtotal_price) VALUES (?, ?, ?, ?)");
        $insertItem->bind_param("iiid", $order_id, $egg_id, $qtyMixed, $subtotal);
        $insertItem->execute();
        $insertItem->close();
    }

    // Step 3: Update total price in orders table
    $updateOrder = $conn->prepare("UPDATE orders SET total_price = ? WHERE order_id = ?");
    $updateOrder->bind_param("di", $total_price, $order_id);
    $updateOrder->execute();

    $conn->commit();

    echo json_encode(["success" => true, "message" => "Order placed successfully"]);

} catch (Exception $e) {
    $conn->rollback();
    echo json_encode(["success" => false, "message" => "Error: " . $e->getMessage()]);
}

$conn->close();
