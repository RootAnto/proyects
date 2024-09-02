<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../../assets/css/style.css">
    <title>Professors Management</title>
</head>
<body>
    <nav>
        <header>
            <h1>Professors Management</h1>
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
        
        <!-- Form to Insert New Professor -->
        <h2>Insert New Professor</h2>
        <form action="../../controller/controller_teacher.php?action=create" method="POST">
            <label for="idProfesor">Professor ID:</label><br>
            <input type="text" id="idProfesor" name="idProfesor" required><br>
            
            <label for="NIF">NIF:</label><br>
            <input type="text" id="NIF" name="NIF" required><br>
            
            <label for="nombre">Name:</label><br>
            <input type="text" id="nombre" name="nombre" required><br>
            
            <label for="apellido1">First Surname:</label><br>
            <input type="text" id="apellido1" name="apellido1" required><br>
            
            <label for="apellido2">Second Surname:</label><br>
            <input type="text" id="apellido2" name="apellido2"><br>
            
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email" required><br>
            
            <label for="direccion">Address:</label><br>
            <input type="text" id="direccion" name="direccion" required><br>
            
            <label for="codigoPostal">Postal Code:</label><br>
            <input type="number" id="codigoPostal" name="codigoPostal" required><br>
            
            <label for="municipio">City:</label><br>
            <input type="text" id="municipio" name="municipio" required><br>
            
            <label for="provincia">Province:</label><br>
            <input type="text" id="provincia" name="provincia" required><br>
            
            <label for="categoria">Category:</label><br>
            <select id="categoria" name="categoria" required>
                <option value="Catedráticos de Universidad">University Professors</option>
                <option value="Titulares Universidad">University Lecturers</option>
                <option value="Catedráticos de Escuela Universitaria">University School Professors</option>
                <option value="Titulares de Escuela Universitaria">University School Lecturers</option>
                <option value="Eméritos">Emeritus</option>
                <option value="Contratados Doctores">Contracted Doctors</option>
                <option value="Contratados Doctores Interinos">Interim Contracted Doctors</option>
                <option value="Asociados">Associate Professors</option>
                <option value="Asociado Interino">Interim Associate Professor</option>
                <option value="Ayudantes Doctores">Assistant Professors</option>
                <option value="Otros Investigadores Doctores">Other Research Doctors</option>
                <option value="PDI predoctoral">Predoctoral PDI</option>
            </select><br><br>
            <input type="submit" name="submit" value="Insert Professor">
        </form>

        <!-- Form to Search Professor by NIF -->
        <h2>Search Professor</h2>
        <form action="../../controller/controller_teacher.php" method="GET">
            <input type="hidden" name="action" value="view">
            <label for="nif">NIF:</label><br>
            <input type="text" id="nif" name="NIF" required><br>
            <input type="submit" value="Search">
        </form>


        <!-- Form to Delete Professor by NIF -->
        <h2>Delete Professor</h2>
        <form action="../../controller/controller_teacher.php" method="GET">
            <input type="hidden" name="action" value="delete">
            <label for="nif">NIF:</label><br>
            <input type="text" id="nif" name="NIF" required><br>
            <input type="submit" value="Delete">
        </form> 

        <!-- Form to Modify Professor -->
        <h2>Modify Professor</h2>
        <form action="../../controller/controller_teacher.php?action=update" method="POST" style="text-align: center;">
            
            <label for="NIF">NIF:</label><br>
            <input type="text" id="NIF" name="NIF" required><br>
            
            <label for="field">Field to Modify:</label><br>
            <select id="field" name="field" onchange="toggleNewValueInput()" required>
                <option value="nombre">Name</option>
                <option value="apellido1">First Surname</option>
                <option value="apellido2">Second Surname</option>
                <option value="email">Email</option>
                <option value="direccion">Address</option>
                <option value="codigoPostal">Postal Code</option>
                <option value="municipio">City</option>
                <option value="provincia">Province</option>
                <option value="categoria">Category</option>
            </select><br>
            
            <label for="newValue">New Value:</label><br>
            <input type="text" id="newValue" name="newValue" required><br>

            <!-- Dropdown for Category, hidden by default -->
            <select id="newCategoryValue" name="newCategoryValue" style="display: none; text-align: left;">
            <option value="Catedráticos de Universidad">University Professors</option>
                <option value="Titulares Universidad">University Lecturers</option>
                <option value="Catedráticos de Escuela Universitaria">University School Professors</option>
                <option value="Titulares de Escuela Universitaria">University School Lecturers</option>
                <option value="Eméritos">Emeritus</option>
                <option value="Contratados Doctores">Contracted Doctors</option>
                <option value="Contratados Doctores Interinos">Interim Contracted Doctors</option>
                <option value="Asociados">Associate Professors</option>
                <option value="Asociado Interino">Interim Associate Professor</option>
                <option value="Ayudantes Doctores">Assistant Professors</option>
                <option value="Otros Investigadores Doctores">Other Research Doctors</option>
                <option value="PDI predoctoral">Predoctoral PDI</option>
            </select><br>
            
            <input type="submit" name="submit" value="Modify">
        </form>

        <script>
            function toggleNewValueInput() {
                const field = document.getElementById('field').value;
                const newValueInput = document.getElementById('newValue');
                const newCategorySelect = document.getElementById('newCategoryValue');
                
                if (field === 'categoria') {
                    newValueInput.style.display = 'none';
                    newCategorySelect.style.display = 'inline-block';
                    newCategorySelect.required = true;
                    newValueInput.required = false;
                } else {
                    newValueInput.style.display = 'inline-block';
                    newCategorySelect.style.display = 'none';
                    newCategorySelect.required = false;
                    newValueInput.required = true;
                }
            }
        </script>

 

        <!-- Form to Show All Professors -->
        <h2>Show All Professors</h2>
        <form action="../../controller/controller_teacher.php" method="GET">
            <input type="hidden" name="action" value="list">
            <input type="submit" value="Show Professors">
        </form>
    </section>
</body>
</html>
