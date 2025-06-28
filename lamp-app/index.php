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
    $email = $_POST['email'];
    $message = $_POST['message'];
    $stmt = $conn->prepare("INSERT INTO entries (name, email, message) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $name, $email, $message);
    $stmt->execute();
    $stmt->close();
}

$result = $conn->query("SELECT * FROM entries ORDER BY id DESC");
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Guestbook</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f7f7f7;
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 0;
            padding: 0;
        }
        h1 {
            margin-top: 50px;
            color: #333;
        }
        form {
            background: #fff;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            margin-top: 20px;
            width: 100%;
            max-width: 400px;
        }
        input[type="text"], input[type="email"] {
            width: 100%;
            padding: 12px 15px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 14px;
        }
        input[type="submit"] {
            background-color: #007bff;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 15px;
            margin-top: 10px;
            width: 100%;
        }
        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        .messages {
            margin-top: 80px;
            max-width: 600px;
            width: 90%;
        }
        .message-entry {
            background: #ffffff;
            border-left: 5px solid #007bff;
            margin-bottom: 15px;
            padding: 15px 20px;
            border-radius: 5px;
            box-shadow: 0 1px 5px rgba(0,0,0,0.1);
        }
        .message-entry strong {
            display: block;
            font-size: 16px;
            color: #007bff;
        }
        .message-entry small {
            display: block;
            color: #777;
            margin-top: 2px;
        }
    </style>
</head>
<body>
    <h1>Welcome to Guest Books</h1>
    
    <form method="POST">
        <input type="text" name="name" placeholder="Your Name" required>
        <input type="email" name="email" placeholder="Your Email (optional)">
        <input type="text" name="message" placeholder="Your Messagee" required>
        <input type="submit" value="Submit">
    </form>

    <div class="messages">
        <h2>Messages Stored in Database</h2>
        <?php while ($row = $result->fetch_assoc()): ?>
            <div class="message-entry">
                <strong><?= htmlspecialchars($row['name']) ?></strong>
                <?php if (!empty($row['email'])): ?>
                    <small><?= htmlspecialchars($row['email']) ?></small>
                <?php endif; ?>
                <p><?= htmlspecialchars($row['message']) ?></p>
            </div>
        <?php endwhile; ?>
    </div>
</body>
</html>
