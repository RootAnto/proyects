<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <title>Impartir Management</title>
</head>
<body>
    <nav>
        <header>
            <h1>Impartir Management</h1>
            <a href="../teacher/teacher_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    <section>
        <!-- Display Messages -->
        <h2>Display Messages</h2>
        <?php
        // Display messages from the URL parameter
        if (isset($_GET['message'])) {
            echo "<p>" . htmlspecialchars($_GET['message']) . "</p>";
        }
        ?>

        <!-- Form to Search Enrollment by Professor ID -->
        <h2>Search Enrollment by Professor ID</h2>
        <form action="../../controller/controller_impart.php" method="GET">
            <input type="hidden" name="action" value="viewByProfessor">
            <label for="idProfesor">Professor ID:</label><br>
            <input type="text" id="idProfesor" name="idProfesor" maxlength="5" required><br>
            <input type="submit" value="Search">
        </form>

        <!-- Form to Search Enrollment by Subject ID -->
        <h2>Search Enrollment by Subject ID</h2>
        <form action="../../controller/controller_impart.php" method="GET">
            <input type="hidden" name="action" value="viewBySubject">
            <label for="idAsignatura">Subject ID:</label><br>
            <input type="text" id="idAsignatura" name="idAsignatura" maxlength="5" required><br>
            <input type="submit" value="Search">
        </form>

        <!-- Form to Show All Enrollments -->
        <h2>Show All Enrollments</h2>
        <form action="../../controller/controller_impart.php" method="GET">
            <input type="hidden" name="action" value="list">
            <input type="submit" value="Show Enrollments">
        </form>
    </section>
</body>
</html>
