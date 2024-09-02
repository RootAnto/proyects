<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Show All Subjects</title>
</head>
<body>
    <nav>
        <header>
            <h1>Show All Subjects</h1>
            <a href="../views/subject/subject_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    
    <section>
        <?php if (isset($subjects) && !empty($subjects)): ?>
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
                <?php foreach ($subjects as $row): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['curso']); ?></td>
                        <td><?php echo htmlspecialchars($row['idAsignatura']); ?></td>
                        <td><?php echo htmlspecialchars($row['nombre']); ?></td>
                        <td><?php echo htmlspecialchars($row['cuatrimestre']); ?></td>
                        <td><?php echo htmlspecialchars($row['creditos']); ?></td>
                        <td><?php echo htmlspecialchars($row['caracter']); ?></td>
                        <td><?php echo htmlspecialchars($row['coordinador']); ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <?php else: ?>
            <p>No subjects found in the table.</p>
        <?php endif; ?>
    </section>
</body>
</html>
