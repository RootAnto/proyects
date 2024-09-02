<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <title>Students Management</title>
</head>
<body>
    <nav>
        <header>
            <h1>Students Management</h1>
            <a href="../../index.php" style="color: whitesmoke;">Back</a>
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
        
        <!-- Form to Insert New Student -->
        <h2>Insert New Student</h2>
        <form action="../../controller/controller_student.php?action=create" method="POST">
            <label for="idStudent">Student ID:</label><br>
            <input type="text" id="idStudent" name="idStudent" required><br>
            
            <label for="NIF">NIF:</label><br>
            <input type="text" id="NIF" name="NIF" required><br>
            
            <label for="name">Name:</label><br>
            <input type="text" id="name" name="name" required><br>
            
            <label for="surname1">First Surname:</label><br>
            <input type="text" id="surname1" name="surname1" required><br>
            
            <label for="surname2">Second Surname:</label><br>
            <input type="text" id="surname2" name="surname2"><br>
            
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" required><br>
            
            <label for="address">Address:</label><br>
            <input type="text" id="address" name="address" required><br>
            
            <label for="postalCode">Postal Code:</label><br>
            <input type="number" id="postalCode" name="postalCode" required><br>
            
            <label for="city">City:</label><br>
            <input type="text" id="city" name="city" required><br>
            
            <label for="province">Province:</label><br>
            <input type="text" id="province" name="province" required><br>
            
            <input type="submit" name="submit" value="Insert Student">
        </form>

        <!-- Form to Search Student by NIF -->
        <h2>Search Student</h2>
        <form action="../../controller/controller_student.php" method="GET">
            <input type="hidden" name="action" value="view">
            <label for="nif">NIF:</label><br>
            <input type="text" id="nif" name="NIF" required><br>
            <input type="submit" value="Search">
        </form>

        <!-- Form to Delete Student by NIF -->
        <h2>Delete Student</h2>
        <form action="../../controller/controller_student.php" method="GET">
            <input type="hidden" name="action" value="delete">
            <label for="nif">NIF:</label><br>
            <input type="text" id="nif" name="NIF" required><br>
            <input type="submit" value="Delete">
        </form> 

        <!-- Form to Modify Student -->
        <h2>Modify Student</h2>
        <form action="../../controller/controller_student.php?action=update" method="POST" style="text-align: center;">
            
            <label for="NIF">NIF:</label><br>
            <input type="text" id="NIF" name="NIF" required><br>
            
            <label for="field">Field to Modify:</label><br>
            <select id="field" name="field" onchange="toggleNewValueInput()" required>
                <option value="name">Name</option>
                <option value="surname1">First Surname</option>
                <option value="surname2">Second Surname</option>
                <option value="email">Email</option>
                <option value="address">Address</option>
                <option value="postalCode">Postal Code</option>
                <option value="city">City</option>
                <option value="province">Province</option>
            </select><br>
            
            <label for="newValue">New Value:</label><br>
            <input type="text" id="newValue" name="newValue" required><br>

            <!-- Dropdown for Category, hidden by default -->
            <select id="newCategoryValue" name="newCategoryValue" style="display: none; text-align: left;">
                <!-- For students, you may not need this, but added for consistency -->
            </select><br>
            
            <input type="submit" name="submit" value="Modify">
        </form>

        <!-- Form to Show All Students -->
        <h2>Show All Students</h2>
        <form action="../../controller/controller_student.php" method="GET">
            <input type="hidden" name="action" value="list">
            <input type="submit" value="Show Students">
        </form>
    </section>
</body>
</html>
