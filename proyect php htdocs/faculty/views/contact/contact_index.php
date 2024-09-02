<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <title>Contact Numbers Management</title>
</head>
<body>
    <nav>
        <header>
            <h1>Contact Numbers Management</h1>
            <a href="../../views/teacher/teacher_index.php" style="color: whitesmoke;">Back</a>
        </header>
    </nav>
    <section>
        <!-- Display Messages -->
        <h2>Display Messages</h2>
        <?php
        // Display messages from the URL parameter
        if(isset($_GET['message'])) {
            echo "<p>" . htmlspecialchars($_GET['message']) . "</p>";
        }
        ?>
        
        <!-- Form to Insert New Contact Number -->
        <h2>Insert New Contact Number</h2>
        <form action="../../controller/controller_contact.php?action=create" method="POST">
            <label for="idProfesor">Professor ID:</label><br>
            <input type="text" id="idProfesor" name="idProfesor" required><br>
            
            <label for="telefono">Phone Number:</label><br>
            <input type="number" id="telefono" name="telefono" required><br><br>
            
            <input type="submit" name="submit" value="Insert Phone Number">
        </form>

        <!-- Form to Search Contact Number by Professor ID -->
        <h2>Search Contact Number</h2>
        <form action="../../controller/controller_contact.php" method="GET">
            <input type="hidden" name="action" value="view">
            <label for="idProfesor">Professor ID:</label><br>
            <input type="text" id="idProfesor" name="idProfesor" required><br>
            <input type="submit" value="Search">
        </form>

        <!-- Form to Delete Contact Number by Professor ID -->
        <h2>Delete Contact Number</h2>
        <form action="../../controller/controller_contact.php" method="GET">
            <input type="hidden" name="action" value="delete">
            <label for="idProfesor">Professor ID:</label><br>
            <input type="text" id="idProfesor" name="idProfesor" required><br>
            <label for="telefono">Phone Number:</label><br>
            <input type="number" id="telefono" name="telefono" required><br><br>
            <input type="submit" value="Delete">
        </form> 

        <!-- Form to Modify Contact Number -->
        <h2>Modify Contact Number</h2>
        <form action="../../controller/controller_contact.php?action=update" method="POST" style="text-align: center;">
            
            <label for="idProfesor">Professor ID:</label><br>
            <input type="text" id="idProfesor" name="idProfesor" required><br>
            
            <label for="telefono">Current Phone Number:</label><br>
            <input type="number" id="telefono" name="telefono" required><br>

            <label for="newTelefono">New Phone Number:</label><br>
            <input type="number" id="newTelefono" name="newTelefono" required><br><br>

            <input type="submit" name="submit" value="Modify">
        </form>

        <!-- Form to Show All Contact Numbers -->
        <h2>Show All Contact Numbers</h2>
        <form action="../../controller/controller_contact.php" method="GET">
            <input type="hidden" name="action" value="list">
            <input type="submit" value="Show Contact Numbers">
        </form>
    </section>
</body>
</html>
