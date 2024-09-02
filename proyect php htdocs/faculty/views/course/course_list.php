<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Show Courses</title>
</head>
<body>
    <nav>
        <header>
            <h1>Show All Courses</h1>
            <a href="../views/course/course_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    
    <section>
        <?php if ($courses && $courses->num_rows > 0): ?>
            <table>
                <tr>
                    <th>Course ID</th>
                    <th>Name</th>
                    <th>Number of Subjects</th>
                </tr>
                <?php while ($row = $courses->fetch_assoc()): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['idCurso']); ?></td>
                        <td><?php echo htmlspecialchars($row['nombreDescriptivo']); ?></td>
                        <td><?php echo htmlspecialchars($row['nAsignaturas']); ?></td>
                    </tr>
                <?php endwhile; ?>
            </table>
        <?php else: ?>
            <p>No courses found in the table.</p>
        <?php endif; ?>
    </section>
</body>
</html>
