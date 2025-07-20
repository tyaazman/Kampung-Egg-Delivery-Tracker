<?php
include 'db.php';
header('Content-Type: application/json');

// Get values from GET parameters
$name = $_GET['name'] ?? '';
$email = $_GET['email'] ?? '';
$password = $_GET['password'] ?? '';
$phone = $_GET['phone'] ?? '';
$address = $_GET['address'] ?? '';
$role = 'customer'; // Fixed role

// Basic validation
if (!$name || !$email || !$password) {
    echo json_encode(['success' => false, 'message' => 'Missing required fields']);
    exit;
}

// Insert into users table
$stmt = $conn->prepare("INSERT INTO users (name, email, password, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)");
$stmt->bind_param("ssssss", $name, $email, $password, $phone, $address, $role);

if ($stmt->execute()) {
    echo json_encode(['success' => true, 'message' => 'User registered successfully']);
} else {
    echo json_encode(['success' => false, 'message' => 'Failed to register']);
}

$conn->close();
?>
