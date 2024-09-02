<?php
require_once 'database.php'; 

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

class ModelStudent {
    
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

    public function createStudent($idAlumno, $NIF, $nombre, $apellido1, $apellido2, $email, $direccion, $codigoPostal, $municipio, $provincia) {
        // SQL query to insert a new student into the 'alumno' table
        $sql = "INSERT INTO alumno (idAlumno, NIF, nombre, apellido1, apellido2, email, direccion, codigoPostal, municipio, provincia) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the parameters to the SQL query (string, string, string, string, string, string, string, integer, string, string)
            $stmt->bind_param('ssssssssss', $idAlumno, $NIF, $nombre, $apellido1, $apellido2, $email, $direccion, $codigoPostal, $municipio, $provincia);
    
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
    
    public function getStudentByNIF($NIF) {
        // SQL query to select a student by their NIF
        $sql = "SELECT * FROM alumno WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the NIF parameter (string)
            $stmt->bind_param('s', $NIF);
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
    
    public function deleteStudentByNIF($NIF) {
        // SQL query to delete a student by their NIF
        $sql = "DELETE FROM alumno WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the NIF parameter (string)
            $stmt->bind_param('s', $NIF);
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
    
    public function updateStudentName($NIF, $newName) {
        // SQL query to update the student's name by their NIF
        $sql = "UPDATE alumno SET nombre = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new name (string) and NIF (string)
            $stmt->bind_param('ss', $newName, $NIF);
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
    
    public function updateStudentSurname1($NIF, $newSurname1) {
        // SQL query to update the student's first surname by their NIF
        $sql = "UPDATE alumno SET apellido1 = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new first surname (string) and NIF (string)
            $stmt->bind_param('ss', $newSurname1, $NIF);
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
    
    public function updateStudentSurname2($NIF, $newSurname2) {
        // SQL query to update the student's second surname by their NIF
        $sql = "UPDATE alumno SET apellido2 = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new second surname (string) and NIF (string)
            $stmt->bind_param('ss', $newSurname2, $NIF);
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
    
    public function updateStudentEmail($NIF, $newEmail) {
        // SQL query to update the student's email by their NIF
        $sql = "UPDATE alumno SET email = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new email (string) and NIF (string)
            $stmt->bind_param('ss', $newEmail, $NIF);
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
    
    public function updateStudentAddress($NIF, $newAddress) {
        // SQL query to update the student's address by their NIF
        $sql = "UPDATE alumno SET direccion = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new address (string) and NIF (string)
            $stmt->bind_param('ss', $newAddress, $NIF);
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
    
    public function updateStudentPostalCode($NIF, $newPostalCode) {
        // SQL query to update the student's postal code by their NIF
        $sql = "UPDATE alumno SET codigoPostal = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new postal code (integer) and NIF (string)
            $stmt->bind_param('is', $newPostalCode, $NIF);
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
    
    public function updateStudentCity($NIF, $newCity) {
        // SQL query to update the student's city by their NIF
        $sql = "UPDATE alumno SET municipio = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new city (string) and NIF (string)
            $stmt->bind_param('ss', $newCity, $NIF);
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
    
    public function updateStudentProvince($NIF, $newProvince) {
        // SQL query to update the student's province by their NIF
        $sql = "UPDATE alumno SET provincia = ? WHERE NIF = ?";
    
        // Prepare the SQL statement
        if ($stmt = $this->conn->prepare($sql)) {
            // Bind the new province (string) and NIF (string)
            $stmt->bind_param('ss', $newProvince, $NIF);
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
    
    public function getAllStudents() {
        // SQL query to select all students from the 'alumno' table
        $sql = "SELECT * FROM alumno";
    
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
