<?php
require_once '../models/model_impart.php'; // Include the corresponding model

class ImpartController {
    private $model;

    public function __construct() {
        $this->model = new ModelImpart(); // Instantiate the model
    }

    // Method to process the request
    public function processRequest() {
        if (isset($_GET['action'])) {
            $action = $_GET['action'];
            switch ($action) {
                case 'viewByProfessor':
                    $this->viewByProfessor();
                    break;

                case 'viewBySubject':
                    $this->viewBySubject();
                    break;

                case 'list':
                    $this->showAllImpart();
                    break;

                default:
                    echo "Action not recognized.";
            }
        }
    }

    private function viewByProfessor() {
        if (isset($_GET['idProfesor'])) {
            $professorId = $_GET['idProfesor'];
            $impartir = $this->model->getImpartByProfessorId($professorId);
    
            // Check if the record was found
            if ($impartir) {
                // Include the view and pass the record data
                require '../views/impart/impart_list.php';
            } else {
                echo "No results found for the provided professor ID.";
            }
        } else {
            echo "No professor ID provided.";
        }
    }

    private function viewBySubject() {
        if (isset($_GET['idAsignatura'])) {
            $subjectId = $_GET['idAsignatura'];
            $impartir = $this->model->getImpartBySubjectId($subjectId);
    
            // Check if the record was found
            if ($impartir) {
                // Include the view and pass the record data
                require '../views/impart/impart_list.php';
            } else {
                echo "No results found for the provided subject ID.";
            }
        } else {
            echo "No subject ID provided.";
        }
    }

    private function showAllImpart() {
        // Retrieve all records using the model
        $impartir = $this->model->getAllImpart();
    
        // If there's an error, terminate the script
        if ($impartir === false) {
            die("Error retrieving records.");
        }
        
        // Load the list view to display all records
        require '../views/impart/impart_list.php';
    }
}

// Instantiate the ImpartController class to handle the related actions
$controller = new ImpartController();

// Call the processRequest method to determine and execute the appropriate action
$controller->processRequest();
?>
