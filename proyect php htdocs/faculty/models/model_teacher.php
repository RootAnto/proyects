<?php
require_once 'database.php'; 

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

class ModelTeacher {
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

    // Method to create a new teacher
    public function createTeacher($idTeacher, $NIF, $name, $surname1, $surname2, $email, $address, $postalCode, $municipality, $province, $category) {
        // Prepare the SQL statement
        $stmt = $this->conn->prepare(
            "INSERT INTO profesor (idProfesor, NIF, nombre, apellido1, apellido2, email, direccion, codigoPostal, municipio, provincia, categoria) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );

        // Check if prepare() returned false
        if ($stmt === false) {
            die('Prepare failed: ' . $this->conn->error);
        }

        // Bind parameters to the SQL query
        $stmt->bind_param(
            "sssssssssss",  // All are strings
            $idTeacher, 
            $NIF, 
            $name, 
            $surname1, 
            $surname2, 
            $email, 
            $address, 
            $postalCode, 
            $municipality, 
            $province, 
            $category
        );

        // Execute the statement
        $success = $stmt->execute();

        // Check if execute() returned false
        if ($success === false) {
            die('Execute failed: ' . $stmt->error);
        }

        // Close the statement
        $stmt->close();

        // Return whether the insert was successful
        return $success;
    }



    // Method to get a teacher by NIF
    public function getTeacherByNIF($NIF) {
        $stmt = $this->conn->prepare("SELECT * FROM profesor WHERE NIF = ?");
        $stmt->bind_param("s", $NIF);
        $stmt->execute();
        $result = $stmt->get_result();
        $teacher = $result->fetch_assoc();
        $stmt->close();

        return $teacher; // Returns an associative array or false if no result
    }


    // Method to delete a teacher by NIF
    public function deleteTeacherByNIF($NIF) {
        $stmt = $this->conn->prepare("DELETE FROM profesor WHERE NIF = ?");
        $stmt->bind_param("s", $NIF);
        $stmt->execute();
        $affectedRows = $stmt->affected_rows;
        $stmt->close();

        return $affectedRows;
    }

    public function updateTeacherName($NIF, $newValue) {
        return $this->updateTeacherField('nombre', $NIF, $newValue);
    }
    
    public function updateTeacherSurname1($NIF, $newValue) {
        return $this->updateTeacherField('apellido1', $NIF, $newValue);
    }
    
    public function updateTeacherSurname2($NIF, $newValue) {
        return $this->updateTeacherField('apellido2', $NIF, $newValue);
    }
    
    public function updateTeacherEmail($NIF, $newValue) {
        return $this->updateTeacherField('email', $NIF, $newValue);
    }
    
    public function updateTeacherAddress($NIF, $newValue) {
        return $this->updateTeacherField('direccion', $NIF, $newValue);
    }
    
    public function updateTeacherPostalCode($NIF, $newValue) {
        return $this->updateTeacherField('codigoPostal', $NIF, $newValue);
    }
    
    public function updateTeacherMunicipality($NIF, $newValue) {
        return $this->updateTeacherField('municipio', $NIF, $newValue);
    }
    
    public function updateTeacherProvince($NIF, $newValue) {
        return $this->updateTeacherField('provincia', $NIF, $newValue);
    }
    
    public function updateTeacherCategory($NIF, $newValue) {
        return $this->updateTeacherField('categoria', $NIF, $newValue);
    }
    

    // Helper method to update a specific field
    private function updateTeacherField($field, $NIF, $newValue) {
        $stmt = $this->conn->prepare("UPDATE profesor SET $field = ? WHERE NIF = ?");
        $stmt->bind_param("ss", $newValue, $NIF);
        $stmt->execute();
        $affectedRows = $stmt->affected_rows;
        $stmt->close();

        return $affectedRows;
    }

    // Method to retrieve all teachers
    public function getAllTeachers() {
        // SQL query to select all courses from the 'profesor' table
        $sql = "SELECT * FROM profesor";
    
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
