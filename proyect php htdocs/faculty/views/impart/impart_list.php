<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Show All Teaching Assignments</title>
</head>
<body>
    <nav>
        <header>
            <h1>Show All Teaching Assignments</h1>
            <a href="../views/impart/impart_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    
    <section>
        <?php if (isset($impartir) && !empty($impartir)): ?>
            <table>
                <tr>
                    <th>Professor ID</th>
                    <th>Subject ID</th>
                </tr>
                <?php foreach ($impartir as $record): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($record['idProfesor']); ?></td>
                        <td><?php echo htmlspecialchars($record['idAsignatura']); ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php else: ?>
            <p>No enrollments found.</p>
        <?php endif; ?>
    </section>
</body>
</html>
