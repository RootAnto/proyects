<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../assets/css/style.css">
    <title>Buscar Cursos</title>
</head>
<body>
    <nav>
        <header>
            <h1>Buscar curso</h1>
            <a href="../curso/curso_index.php" style="color: whitesmoke;">Volver Atras</a>
        </header>
    </nav>

    <nav>
        <?php if ($course && $course->num_rows > 0): ?>
            <table>
                <tr>
                    <th>ID Curso</th>
                    <th>Nombre</th>
                    <th>Num. Asignaturas</th>
                </tr>
                <?php while ($row = $course->fetch_assoc()): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($row['idCurso']); ?></td>
                        <td><?php echo htmlspecialchars($row['nombreDescriptivo']); ?></td>
                        <td><?php echo htmlspecialchars($row['nAsignaturas']); ?></td>
                    </tr>
                <?php endwhile; ?>
            </table>
        <?php else: ?>
            <p>No se encontraron resultados para el ID de curso proporcionado.</p>
        <?php endif; ?>
    </nav>

    <footer>
        <img src="../assets/img/logo.jpg" style="height: 85px;" alt="TorrijosFact">
        <p>&copy; Facultad de Torrijos 2024</p>
    </footer>
</body>
</html>
