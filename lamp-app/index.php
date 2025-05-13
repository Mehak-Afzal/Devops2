<?php
$host = 'db';
$user = 'root';
$pass = 'example';
$dbname = 'guestbook';

$conn = new mysqli($host, $user, $pass, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_POST['name'];
    $message = $_POST['message'];
    $stmt = $conn->prepare("INSERT INTO entries (name, message) VALUES (?, ?)");
    $stmt->bind_param("ss", $name, $message);
    $stmt->execute();
    $stmt->close();
}

$result = $conn->query("SELECT * FROM entries ORDER BY id DESC");
?>

<!DOCTYPE html>
<html>
<head>
    <title>Guestbook</title>
</head>
<body>
    <h1>Leave a message</h1>
    <form method="POST">
        Name: <input name="name"><br>
        Message: <input name="message"><br>
        <input type="submit" value="Submit">
    </form>
    <h2>Messages</h2>
    <?php while ($row = $result->fetch_assoc()): ?>
        <p><strong><?= htmlspecialchars($row['name']) ?>:</strong> <?= htmlspecialchars($row['message']) ?></p>
    <?php endwhile; ?>
</body>
</html>
