<?php
require_once 'database.php'; 

class ModelImpart {
    // Obtain the connection
    private $conn;

    public function __construct() {
        // Open the connection
        $this->conn = Database::getInstance()->getConnection();
        
        // Check for connection errors
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
    }

    // Method to get records by professor ID
    public function getImpartByProfessorId($professorId) {
        $stmt = $this->conn->prepare(
            "SELECT * FROM impartir WHERE idProfesor = ?"
        );
        $stmt->bind_param("s", $professorId);
        $stmt->execute();
        $result = $stmt->get_result();
        $impart = $result->fetch_all(MYSQLI_ASSOC);  // Return an associative array of all results
        $stmt->close();

        return $impart; // Return an associative array or false if no result
    }

    // Method to get records by subject ID
    public function getImpartBySubjectId($subjectId) {
        $stmt = $this->conn->prepare(
            "SELECT * FROM impartir WHERE idAsignatura = ?"
        );
        $stmt->bind_param("s", $subjectId);
        $stmt->execute();
        $result = $stmt->get_result();
        $impart = $result->fetch_all(MYSQLI_ASSOC);  // Return an associative array of all results
        $stmt->close();

        return $impart; // Return an associative array or false if no result
    }

    public function getAllImpart() {
        $sql = "SELECT * FROM impartir";
        if ($result = $this->conn->query($sql)) {
            $data = $result->fetch_all(MYSQLI_ASSOC);
            return $data;
        } else {
            echo "Error executing query: " . $this->conn->error;
            return false;
        }
    }
}
?>
