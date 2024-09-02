<?php
require_once 'database.php';

class ModelSubject {
    private $conn;

    public function __construct() {
        $this->conn = Database::getInstance()->getConnection();
        
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
    }

    // Method to check if a professor exists
    private function professorExists($coordinator) {
        $stmt = $this->conn->prepare("SELECT idProfesor FROM profesor WHERE idProfesor = ?");
        $stmt->bind_param("s", $coordinator);
        $stmt->execute();
        $stmt->store_result();
        
        $exists = $stmt->num_rows > 0;
        
        $stmt->close();
        return $exists;
    }

    // Method to check if a course exists
    private function courseExists($course) {
        $stmt = $this->conn->prepare("SELECT idCurso FROM curso WHERE idCurso = ?");
        $stmt->bind_param("i", $course);
        $stmt->execute();
        $stmt->store_result();
        
        $exists = $stmt->num_rows > 0;
        
        $stmt->close();
        return $exists;
    }

    // Method to create a new subject
    public function createSubject($course, $subjectId, $name, $semester, $credits, $type, $coordinator) {
        // Check if the coordinator and course exist
        if (!$this->professorExists($coordinator)) {
            die('Error: The coordinator professor does not exist.');
        }

        if (!$this->courseExists($course)) {
            die('Error: The course does not exist.');
        }

        $stmt = $this->conn->prepare(
            "INSERT INTO asignatura (curso, idAsignatura, nombre, cuatrimestre, creditos, caracter, coordinador) VALUES (?, ?, ?, ?, ?, ?, ?)"
        );

        if ($stmt === false) {
            die('Prepare failed: ' . $this->conn->error);
        }

        // Bind the parameters to the SQL query
        // Ensure that the types match: curso (int) - "i", creditos (float) - "d", rest are "s" (string)
        $stmt->bind_param("isssssi", $course, $subjectId, $name, $semester, $credits, $type, $coordinator);

        $success = $stmt->execute();

        if ($success === false) {
            die('Execute failed: ' . $stmt->error);
        }

        $stmt->close();

        return $success;
    }

    // Method to get a subject by ID
    public function getSubjectById($subjectId) {
        $stmt = $this->conn->prepare("SELECT * FROM asignatura WHERE idAsignatura = ?");
        $stmt->bind_param("s", $subjectId);
        $stmt->execute();
        $result = $stmt->get_result();
        $subject = $result->fetch_assoc();
        $stmt->close();

        return $subject;
    }

    public function deleteSubjectById($subjectId) {
        $stmt = $this->conn->prepare("DELETE FROM asignatura WHERE idAsignatura = ?");
        if ($stmt === false) {
            die('Prepare failed: ' . $this->conn->error);
        }
    
        $stmt->bind_param("s", $subjectId);
        $stmt->execute();
        
        // Verificar el número de filas afectadas para depuración
        $affectedRows = $stmt->affected_rows;
        $stmt->close();
    
        // Imprimir el número de filas afectadas para depuración
        echo "Affected rows: " . $affectedRows;
    
        return $affectedRows;
    }
    
    

    // Method to update a subject
    public function updateSubject($subjectId, $course, $name, $semester, $credits, $type, $coordinator) {
        $stmt = $this->conn->prepare(
            "UPDATE asignatura SET curso = ?, nombre = ?, cuatrimestre = ?, creditos = ?, caracter = ?, coordinador = ? WHERE idAsignatura = ?"
        );
        
        $stmt->bind_param("isssssi", $course, $name, $semester, $credits, $type, $coordinator, $subjectId);
        $stmt->execute();
        $affectedRows = $stmt->affected_rows;
        $stmt->close();

        return $affectedRows;
    }

    // Method to retrieve all subjects
    public function getAllSubjects() {
        $sql = "SELECT * FROM asignatura";
    
        if ($result = $this->conn->query($sql)) {
            return $result->fetch_all(MYSQLI_ASSOC);
        } else {
            echo "Error executing query: " . $this->conn->error;
            return false;
        }
    }
}
?>
