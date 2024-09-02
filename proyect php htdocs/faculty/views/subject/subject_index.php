<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <title>Subject Management</title>
</head>
<body>
    <nav>
        <header>
            <h1>Subject Management</h1>
            <a href="../../index.php" style="color: whitesmoke;">Back</a>
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
        
        <!-- Form to Insert New Subject -->
        <h2>Insert New Subject</h2>
        <form action="../../controller/controller_subject.php?action=create" method="POST">
            <label for="curso">Course:</label><br>
            <input type="number" id="curso" name="curso" min="1" max="99" required><br>
            
            <label for="idAsignatura">Subject ID:</label><br>
            <input type="text" id="idAsignatura" name="idAsignatura" maxlength="5" required><br>
            
            <label for="nombre">Subject Name:</label><br>
            <input type="text" id="nombre" name="nombre" maxlength="150" required><br>
            
            <label for="cuatrimestre">Semester:</label><br>
            <select id="cuatrimestre" name="cuatrimestre" required>
                <option value="1">First</option>
                <option value="2">Second</option>
            </select><br>
            
            <label for="creditos">Credits:</label><br>
            <input type="number" id="creditos" name="creditos" step="0.1" required><br>
            
            <label for="caracter">Type:</label><br>
            <select id="caracter" name="caracter" required>
                <option value="obligatoria">Mandatory</option>
                <option value="optativa">Optional</option>
            </select><br>
            
            <label for="coordinador">Coordinator ID:</label><br>
            <input type="text" id="coordinador" name="coordinador" maxlength="5" required><br><br>
            
            <input type="submit" name="submit" value="Insert Subject">
        </form>

        <!-- Form to Search Subject by ID -->
        <h2>Search Subject</h2>
        <form action="../../controller/controller_subject.php" method="GET">
            <input type="hidden" name="action" value="view">
            <label for="idAsignatura">Subject ID:</label><br>
            <input type="text" id="idAsignatura" name="idAsignatura" maxlength="5" required><br>
            <input type="submit" value="Search">
        </form>

        <!-- Form to Delete Subject by ID -->
        <h2>Delete Subject</h2>
        <form action="../../controller/controller_subject.php" method="GET">
            <input type="hidden" name="action" value="delete">
            <label for="idAsignatura">Subject ID:</label><br>
            <input type="text" id="idAsignatura" name="idAsignatura" maxlength="5" required><br>
            <input type="submit" value="Delete">
        </form>


        <!-- Form to Modify Subject -->
        <h2>Modify Subject</h2>
        <form action="../../controller/controller_subject.php?action=update" method="POST" style="text-align: center;">
            
            <label for="idAsignatura">Subject ID:</label><br>
            <input type="text" id="idAsignatura" name="idAsignatura" maxlength="5" required><br>
            
            <label for="nombre">New Subject Name:</label><br>
            <input type="text" id="nombre" name="nombre" maxlength="150" required><br>

            <label for="cuatrimestre">New Semester:</label><br>
            <select id="cuatrimestre" name="cuatrimestre" required>
                <option value="1">First</option>
                <option value="2">Second</option>
            </select><br>
            
            <label for="creditos">New Credits:</label><br>
            <input type="number" id="creditos" name="creditos" step="0.1" required><br>

            <label for="caracter">New Type:</label><br>
            <select id="caracter" name="caracter" required>
                <option value="obligatoria">Mandatory</option>
                <option value="optativa">Optional</option>
            </select><br><br>

            <input type="submit" name="submit" value="Modify">
        </form>

        <!-- Form to Show All Subjects -->
        <h2>Show All Subjects</h2>
        <form action="../../controller/controller_subject.php" method="GET">
            <input type="hidden" name="action" value="list">
            <input type="submit" value="Show Subjects">
        </form>
    </section>
</body>
</html>
