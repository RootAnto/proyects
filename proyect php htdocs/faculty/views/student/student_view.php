<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Search Student</title>
</head>
<body>
    <nav>
        <header>
            <h1>Search Student</h1>
            <a href="../student/student_index.php" style="color: whitesmoke;">Go Back</a>
        </header>
    </nav>

    <main>
        <?php if ($student && $student->num_rows > 0): ?>
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
                <?php while ($row = $student->fetch_assoc()): ?>
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
            <p>No results found for the provided student ID.</p>
        <?php endif; ?>
    </main>

    <footer>
        <img src="../assets/img/logo.jpg" style="height: 85px;" alt="TorrijosFact">
        <p>&copy; Facultad de Torrijos 2024</p>
    </footer>
</body>
</html>
