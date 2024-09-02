<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>View Contact</title>
</head>
<body>
    <nav>
        <header>
            <h1>View Contact</h1>
            <a href="../views/contact/contact_index.php" style="color: whitesmoke;">Go Back</a>
        </header>
    </nav>

    <section>
        <?php if ($contacts): ?>
            <table>
                <tr>
                    <th>Professor ID</th>
                    <th>Phone Number</th>
                </tr>
                <?php foreach ($contacts as $contact): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($contact['idProfesor']); ?></td>
                        <td><?php echo htmlspecialchars($contact['telefono']); ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php else: ?>
            <p>No results found for the provided contact.</p>
        <?php endif; ?>
    </section>



    <footer>
        <img src="../assets/img/logo.jpg" style="height: 85px;" alt="Logo">
        <p>&copy; Torrijos Faculty 2024</p>
    </footer>
</body>
</html>
