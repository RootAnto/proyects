<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course Management</title>
    <link rel="stylesheet" href="../../assets/css/style.css">
</head>
<body>
    <nav>
        <header>
            <h1>Course Management</h1>
            <a href="../../index.php" style="color: whitesmoke;">Back to Home</a>
        </header>
    </nav>

    <section>
        <!-- Display Messages -->
        <h2>Dispay Messages</h2>
        <?php if (isset($_GET['message'])): ?>
            <p><?php echo htmlspecialchars($_GET['message']); ?></p>
        <?php endif; ?>
        <!-- Insert New Course -->
        <h2>Insert New Course</h2>
        <form action="../../controller/controller_course.php?action=create" method="POST">
            <label for="idCurso">ID:</label><br>
            <input type="number" id="idCurso" name="idCurso" required><br>
            
            <label for="courseName">Name:</label><br>
            <input type="text" id="courseName" name="courseName" required><br>
            
            <label for="numSubjects">Number of Subjects:</label><br>
            <input type="number" id="numSubjects" name="numSubjects" required><br>
            
            <input type="submit" name="submit" value="Insert Course">
        </form>

        <!-- Search Course -->
        <h2>Search Course</h2>
        <form action="../../controller/controller_course.php" method="GET">
            <input type="hidden" name="action" value="view">
            <label for="courseId">Course ID:</label><br>
            <input type="number" id="courseId" name="idCurso" required><br>
            <input type="submit" value="Search">
        </form>

        <!-- Delete Course -->
        <h2>Delete Course</h2>
        <form action="../../controller/controller_course.php" method="GET">
            <input type="hidden" name="action" value="delete">
            <label for="courseId">Course ID:</label><br>
            <input type="number" id="courseId" name="idCurso" required><br>
            <input type="submit" value="Delete">
        </form>


        <!-- Modify Course -->
        <h2>Modify Course</h2>
        <form action="../../controller/controller_course.php?action=update" method="POST">
            <label for="courseIdModify">Course ID:</label><br>
            <input type="text" id="courseIdModify" name="idCourse" required><br>
            <label for="field">Field to Modify:<br></label>
            <select id="field" name="field" required>
                <option value="courseName">Name</option>
                <option value="numSubjects">Number of Subjects</option>
            </select><br>
            <label for="newValue">New Value:</label><br>
            <input type="text" id="newValue" name="newValue" required><br>
            <input type="submit" name="submit" value="Modify">
        </form>


        <!-- Show All Courses -->
        <h2>Show All Courses</h2>
        <form action="../../controller/controller_course.php" method="GET">
            <input type="hidden" name="action" value="list">
            <input type="submit" value="Show Courses">
        </form>

    </section>
</body>
</html>
