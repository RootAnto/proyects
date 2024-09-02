<?php
require_once 'database.php'; 

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);



class ModelCourse {
    
    // Get connection
    private $conn;

    public function __construct() {
        // Open the connection
        $this->conn = Database::getInstance()->getConnection();
        
        // Check for connection errors
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
    }

    // SQL query functions

    public function createCourse($idCurso, $nombreDescriptivo, $nAsignaturas) {
        // SQL query to insert a new course into the 'curso' table
        $sql = "INSERT INTO curso (idCurso, nombreDescriptivo, nAsignaturas) VALUES (?, ?, ?)";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the parameters to the SQL query (integer, string, integer)
            $stmt->bind_param('isi', $idCurso, $nombreDescriptivo, $nAsignaturas);
    
            // Execute the prepared statement
            if ($stmt->execute()) {
                // Close the statement and return true if execution was successful
                $stmt->close();
                return true;
            } else {
                // Print error if execution fails
                echo "Error executing statement: " . $stmt->error;
                return false;
            }
        } else {
            // Print error if statement preparation fails
            echo "Error preparing statement: " . $this->conn->error;
            return false;
        }
    }
    
    public function getCourseById($courseId) {
        // SQL query to select a course by its ID
        $sql = "SELECT * FROM curso WHERE idCurso = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the course ID parameter (integer)
            $stmt->bind_param('i', $courseId);
            $stmt->execute();
            
            // Get the result set from the executed statement
            $result = $stmt->get_result();
            
            // Close the statement and return the result
            $stmt->close();
            return $result;
        } else {
            // Print error if statement preparation fails
            echo "Error preparing statement: " . $this->conn->error;
            return false;
        }
    }
    
    public function deleteCourseById($courseId) {
        // SQL query to delete a course by its ID
        $sql = "DELETE FROM curso WHERE idCurso = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the course ID parameter (integer)
            $stmt->bind_param('i', $courseId);
            $stmt->execute();
            
            // Check the number of affected rows
            $affectedRows = $stmt->affected_rows > 0 ? $stmt->affected_rows : 0;
            
            // Close the statement and return the number of affected rows
            $stmt->close();
            return $affectedRows;
        } else {
            // Print error if statement preparation fails
            echo "Error preparing statement: " . $this->conn->error;
            return false;
        }
    }
    
    public function updateCourseName($courseId, $courseName) {
        // SQL query to update the course name for a specific course ID
        $sql = "UPDATE curso SET nombreDescriptivo = ? WHERE idCurso = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new course name (string) and course ID (integer)
            $stmt->bind_param('si', $courseName, $courseId);
            $stmt->execute();
            
            // Check the number of affected rows
            $affectedRows = $stmt->affected_rows;
            
            // Close the statement and return the number of affected rows
            $stmt->close();
            return $affectedRows;
        } else {
            // Print error if statement preparation fails
            echo "Error preparing statement: " . $this->conn->error;
            return false;
        }
    }
    
    public function updateNumSubjects($courseId, $numSubjects) {
        // SQL query to update the number of subjects for a specific course ID
        $sql = "UPDATE curso SET nAsignaturas = ? WHERE idCurso = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new number of subjects (integer) and course ID (integer)
            $stmt->bind_param('ii', $numSubjects, $courseId);
            $stmt->execute();
            
            // Check the number of affected rows
            $affectedRows = $stmt->affected_rows;
            
            // Close the statement and return the number of affected rows
            $stmt->close();
            return $affectedRows;
        } else {
            // Print error if statement preparation fails
            echo "Error preparing statement: " . $this->conn->error;
            return false;
        }
    }
    
    public function getAllCourses() {
        // SQL query to select all courses from the 'curso' table
        $sql = "SELECT * FROM curso";
    
        // Execute the query and return the result set
        if ($result = $this->conn->query($sql)) {
            return $result;
        } else {
            // Print error if query execution fails
            echo "Error executing query: " . $this->conn->error;
            return false;
        }
    }
    
}
?>
