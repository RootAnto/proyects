<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Show All Contacts</title>
</head>
<body>
    <nav>
        <header>
            <h1>Show All Contacts</h1>
            <a href="../views/contact/contact_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    
    <section>
        <?php if (isset($contacts) && !empty($contacts)): ?>
            <table>
                <tr>
                    <th>Professor ID</th>
                    <th>Phone Number</th>
                </tr>
                <?php foreach ($contacts as $row): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['idProfesor']); ?></td>
                        <td><?php echo htmlspecialchars($row['telefono']); ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php else: ?>
            <p>No contacts found in the table.</p>
        <?php endif; ?>
    </section>
</body>
</html>
