<?php
include 'db.php';
header('Content-Type: application/json');

// Get raw POST body
$input = file_get_contents("php://input");
$data = json_decode($input, true);

$username = $data['username'] ?? '';
$password = $data['password'] ?? '';

if (empty($username) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Missing username or password"]);
    exit;
}

// Check if user exists with role 'seller'
$stmt = $conn->prepare("SELECT * FROM users WHERE email = ? AND password = ? AND role = 'seller'");
$stmt->bind_param("ss", $username, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($user = $result->fetch_assoc()) {
    echo json_encode(["success" => true, "user_id" => $user['user_id'], "name" => $user['name']]);
} else {
    echo json_encode(["success" => false, "message" => "Invalid credentials or not a seller"]);
}

$conn->close();
?>
