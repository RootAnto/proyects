<?php
require_once 'database.php'; 

class ModelContact {
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

    // Method to create a new contact
    public function createContact($professorId, $phone) {
        // Prepare the SQL statement
        $stmt = $this->conn->prepare(
            "INSERT INTO tlfContactoProf (idProfesor, telefono) VALUES (?, ?)"
        );

        // Check if prepare() returned false
        if ($stmt === false) {
            die('Prepare failed: ' . $this->conn->error);
        }

        // Bind the parameters to the SQL query
        $stmt->bind_param("ss", $professorId, $phone);

        // Execute the statement
        $success = $stmt->execute();

        // Check if execute() returned false
        if ($success === false) {
            die('Execute failed: ' . $stmt->error);
        }

        // Close the statement
        $stmt->close();

        // Return whether the insertion was successful
        return $success;
    }

    // Method to get a contact by professor ID
    public function getContactById($professorId) {
        $stmt = $this->conn->prepare("SELECT * FROM tlfContactoProf WHERE idProfesor = ?");
        $stmt->bind_param("s", $professorId);
        $stmt->execute();
        $result = $stmt->get_result();
        $contacts = $result->fetch_all(MYSQLI_ASSOC);  // Return an associative array of all results
        $stmt->close();

        return $contacts; // Return an associative array or false if no results
    }

    // Method to delete a contact by professor ID and phone
    public function deleteContactByIdAndPhone($professorId, $phone) {
        $stmt = $this->conn->prepare("DELETE FROM tlfContactoProf WHERE idProfesor = ? AND telefono = ?");
        $stmt->bind_param("ss", $professorId, $phone);
        $stmt->execute();
        $affectedRows = $stmt->affected_rows;
        $stmt->close();

        return $affectedRows;
    }

    // Method to update a phone number for a specific professor ID
    public function updateContactPhone($professorId, $phone, $newPhone) {
        $stmt = $this->conn->prepare("UPDATE tlfContactoProf SET telefono = ? WHERE idProfesor = ? AND telefono = ?");
        $stmt->bind_param("sss", $newPhone, $professorId, $phone);
        $stmt->execute();
        $affectedRows = $stmt->affected_rows;
        $stmt->close();

        return $affectedRows;
    }

    // Method to retrieve all contacts
    public function getAllContacts() {
        $sql = "SELECT * FROM tlfContactoProf";
    
        if ($result = $this->conn->query($sql)) {
            return $result->fetch_all(MYSQLI_ASSOC);  // Return all contacts in an associative array
        } else {
            // Print error if query execution fails
            echo "Error executing query: " . $this->conn->error;
            return false;
        }
    }
}
?>
