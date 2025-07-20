<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json");

// Include existing DB connection
include 'db.php';

$email = $_GET['email'] ?? '';
$password = $_GET['password'] ?? '';

if (empty($email) || empty($password)) {
    echo json_encode(["success" => false, "message" => "Missing email or password"]);
    exit;
}

$sql = "SELECT * FROM users WHERE email = ? AND password = ? AND role = 'customer'";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ss", $email, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows === 1) {
    $user = $result->fetch_assoc();
    echo json_encode([
        "success" => true,
        "user_id" => $user['user_id'],
        "name" => $user['name']
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Invalid credentials"]);
}

$conn->close();
?>
