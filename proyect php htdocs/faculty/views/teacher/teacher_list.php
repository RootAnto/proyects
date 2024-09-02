<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Show All Professors</title>
</head>
<body>
    <nav>
        <header>
            <h1>Show All Professors</h1>
            <a href="../views/teacher/teacher_crud.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    
    <section>
        <?php if (isset($teachers) && $teachers->num_rows > 0): ?>
            <table>
                <tr>
                    <th>Professor ID</th>
                    <th>NIF</th>
                    <th>Name</th>
                    <th>First Surname</th>
                    <th>Second Surname</th>
                    <th>Email</th>
                    <th>Address</th>
                    <th>Postal Code</th>
                    <th>City</th>
                    <th>Province</th>
                    <th>Category</th>
                </tr>
                <?php while ($row = $teachers->fetch_assoc()): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['idProfesor']); ?></td>
                        <td><?php echo htmlspecialchars($row['NIF']); ?></td>
                        <td><?php echo htmlspecialchars($row['nombre']); ?></td>
                        <td><?php echo htmlspecialchars($row['apellido1']); ?></td>
                        <td><?php echo htmlspecialchars($row['apellido2']); ?></td>
                        <td><?php echo htmlspecialchars($row['email']); ?></td>
                        <td><?php echo htmlspecialchars($row['direccion']); ?></td>
                        <td><?php echo htmlspecialchars($row['codigoPostal']); ?></td>
                        <td><?php echo htmlspecialchars($row['municipio']); ?></td>
                        <td><?php echo htmlspecialchars($row['provincia']); ?></td>
                        <td><?php echo htmlspecialchars($row['categoria']); ?></td>
                    </tr>
                <?php endwhile; ?>
            </table>
        <?php else: ?>
            <p>No professors found in the table.</p>
        <?php endif; ?>
    </section>
</body>
</html>
