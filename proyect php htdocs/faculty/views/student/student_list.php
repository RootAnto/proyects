<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Show Students</title>
</head>
<body>
    <nav>
        <header>
            <h1>Show All Students</h1>
            <a href="../views/student/student_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    
    <section>
        <?php if (isset($students) && $students->num_rows > 0): ?>
            <table>
                <tr>
                    <th>Student ID</th>
                    <th>NIF</th>
                    <th>Name</th>
                    <th>First Surname</th>
                    <th>Second Surname</th>
                    <th>Email</th>
                    <th>Address</th>
                    <th>Postal Code</th>
                    <th>City</th>
                    <th>Province</th>
                </tr>
                <?php while ($row = $students->fetch_assoc()): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['idAlumno']); ?></td>
                        <td><?php echo htmlspecialchars($row['NIF']); ?></td>
                        <td><?php echo htmlspecialchars($row['nombre']); ?></td>
                        <td><?php echo htmlspecialchars($row['apellido1']); ?></td>
                        <td><?php echo htmlspecialchars($row['apellido2']); ?></td>
                        <td><?php echo htmlspecialchars($row['email']); ?></td>
                        <td><?php echo htmlspecialchars($row['direccion']); ?></td>
                        <td><?php echo htmlspecialchars($row['codigoPostal']); ?></td>
                        <td><?php echo htmlspecialchars($row['municipio']); ?></td>
                        <td><?php echo htmlspecialchars($row['provincia']); ?></td>
                    </tr>
                <?php endwhile; ?>
            </table>
        <?php else: ?>
            <p>No students found in the table.</p>
        <?php endif; ?>
    </section>
</body>
</html>
