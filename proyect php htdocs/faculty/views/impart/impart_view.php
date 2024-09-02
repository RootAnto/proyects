<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>View Teaching Assignment</title>
</head>
<body>
    <nav>
        <header>
            <h1>View Teaching Assignment</h1>
            <a href="../views/impart/impart_index.php" style="color: whitesmoke;">Go Back</a>
        </header>
    </nav>

    <section>
        <?php if ($teachingAssignment): ?>
            <table>
                <tr>
                    <th>Professor ID</th>
                    <th>Subject ID</th>
                </tr>
                <tr>
                    <td><?php echo htmlspecialchars($teachingAssignment['idProfesor']); ?></td>
                    <td><?php echo htmlspecialchars($teachingAssignment['idAsignatura']); ?></td>
                </tr>
            </table>
        <?php else: ?>
            <p>No results found for the provided teaching assignment.</p>
        <?php endif; ?>
    </section>

    <footer>
        <img src="../assets/img/logo.jpg" style="height: 85px;" alt="Logo">
        <p>&copy; Torrijos Faculty 2024</p>
    </footer>
</body>
</html>
