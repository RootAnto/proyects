<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>View Subject</title>
</head>
<body>
    <nav>
        <header>
            <h1>View Subject</h1>
            <a href="../views/subject/subject_index.php" style="color: whitesmoke;">Go Back</a>
        </header>
    </nav>

    <section>
        <?php if ($subject): ?>
            <table>
                <tr>
                    <th>Course</th>
                    <th>Subject ID</th>
                    <th>Name</th>
                    <th>Semester</th>
                    <th>Credits</th>
                    <th>Type</th>
                    <th>Coordinator</th>
                </tr>
                <tr>
                    <td><?php echo htmlspecialchars($subject['curso']); ?></td>
                    <td><?php echo htmlspecialchars($subject['idAsignatura']); ?></td>
                    <td><?php echo htmlspecialchars($subject['nombre']); ?></td>
                    <td><?php echo htmlspecialchars($subject['cuatrimestre']); ?></td>
                    <td><?php echo htmlspecialchars($subject['creditos']); ?></td>
                    <td><?php echo htmlspecialchars($subject['caracter']); ?></td>
                    <td><?php echo htmlspecialchars($subject['coordinador']); ?></td>
                </tr>
            </table>
        <?php else: ?>
            <p>No results found for the provided subject.</p>
        <?php endif; ?>
    </section>

    <footer>
        <img src="../assets/img/logo.jpg" style="height: 85px;" alt="Logo">
        <p>&copy; Torrijos Faculty 2024</p>
    </footer>
</body>
</html>
