<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>View Professor</title>
</head>
<body>
    <nav>
        <header>
            <h1>View Professor</h1>
            <a href="../views/teacher/teacher_crud.php" style="color: whitesmoke;">Go Back</a>
        </header>
    </nav>

    <section>
        <?php if ($professor): // Check if $professor is not empty ?>
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
                <tr>
                    <td><?php echo htmlspecialchars($professor['idProfesor']); ?></td>
                    <td><?php echo htmlspecialchars($professor['NIF']); ?></td>
                    <td><?php echo htmlspecialchars($professor['nombre']); ?></td>
                    <td><?php echo htmlspecialchars($professor['apellido1']); ?></td>
                    <td><?php echo htmlspecialchars($professor['apellido2']); ?></td>
                    <td><?php echo htmlspecialchars($professor['email']); ?></td>
                    <td><?php echo htmlspecialchars($professor['direccion']); ?></td>
                    <td><?php echo htmlspecialchars($professor['codigoPostal']); ?></td>
                    <td><?php echo htmlspecialchars($professor['municipio']); ?></td>
                    <td><?php echo htmlspecialchars($professor['provincia']); ?></td>
                    <td><?php echo htmlspecialchars($professor['categoria']); ?></td>
                </tr>
            </table>
        <?php else: ?>
            <p>No results found for the provided professor NIF.</p>
        <?php endif; ?>
    </section>

    <footer>
        <img src="../assets/img/logo.jpg" style="height: 85px;" alt="Logo">
        <p>&copy; Torrijos Faculty 2024</p>
    </footer>
</body>
</html>
