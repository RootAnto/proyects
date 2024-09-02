<?php
require_once '../models/model_teacher.php'; // Include the model

class TeacherController {
    private $model;

    public function __construct() {
        $this->model = new ModelTeacher(); // Instantiate the model
    }

    // Method to process the request
    public function processRequest() {
        if (isset($_GET['action'])) {
            $action = $_GET['action'];
            switch ($action) {
                case 'view':
                    $this->viewTeacher();
                    break;

                case 'delete':
                    $this->deleteTeacher();
                    break;

                case 'create':
                    $this->createTeacher();
                    break;

                case 'update':
                    $this->updateTeacher();
                    break;

                case 'list':
                    $this->showAllTeachers();
                    break;

                default:
                    echo "Action not recognized.";
            }
        }
    }

    private function createTeacher() {
        // Check if all required POST parameters are set
        if (
            isset($_POST['idProfesor']) &&
            isset($_POST['NIF']) &&
            isset($_POST['nombre']) &&
            isset($_POST['apellido1']) &&
            isset($_POST['apellido2']) &&
            isset($_POST['email']) &&
            isset($_POST['direccion']) &&
            isset($_POST['codigoPostal']) &&
            isset($_POST['municipio']) &&
            isset($_POST['provincia']) &&
            isset($_POST['categoria'])
        ) {
            // Retrieve the input values
            $idTeacher = $_POST['idProfesor'];
            $NIF = $_POST['NIF'];
            $name = $_POST['nombre'];
            $surname1 = $_POST['apellido1'];
            $surname2 = $_POST['apellido2'];
            $email = $_POST['email'];
            $address = $_POST['direccion'];
            $postalCode = $_POST['codigoPostal'];
            $municipality = $_POST['municipio'];
            $province = $_POST['provincia'];
            $category = $_POST['categoria'];
            
            // Call the model's method to create the teacher
            $success = $this->model->createTeacher($idTeacher, $NIF, $name, $surname1, $surname2, $email, $address, $postalCode, $municipality, $province, $category);
            
            // Set the success or error message based on the result
            $message = $success ? "Teacher successfully created." : "Error creating the teacher.";
            
            // Redirect to the teacher index page with the message
            header("Location: ../views/teacher/teacher_crud?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }
    

    private function viewTeacher() {
        if (isset($_GET['NIF'])) {
            $NIF = $_GET['NIF'];
            $professor = $this->model->getTeacherByNIF($NIF);
    
            // Check if a professor was found
            if ($professor) {
                // Include the view and pass the professor data to it
                require '../views/teacher/teacher_view.php';
            } else {
                // Handle case where no professor is found
                echo "No results found for the provided professor NIF.";
            }
        }
    }

    private function deleteTeacher() {
        // Check if the GET parameter 'NIF' is set
        if (isset($_GET['NIF'])) {
            // Retrieve the teacher NIF from the GET parameter
            $NIFTeacher = $_GET['NIF'];
            
            // Call the model's method to delete the teacher by NIF
            $result = $this->model->deleteTeacherByNIF($NIFTeacher);
            
            // Set the success or error message based on the result
            $message = $result > 0 ? "Teacher successfully deleted" : "Teacher not found or could not be deleted.";
            
            // Redirect to the teacher index page with the message
            header("Location: ../views/teacher/teacher_crud.php?message=" . urlencode($message));
            exit();
        } else {
            echo "NIF parameter is missing.";
        }
    }

    private function updateTeacher() {
        // Check if all required POST parameters are set
        if (isset($_POST['NIF']) && isset($_POST['field']) && isset($_POST['newValue'])) {
            // Retrieve the input values
            $NIFTeacher = $_POST['NIF'];
            $field = $_POST['field'];
            $newValue = $_POST['newValue'];
    
            // Determine which field to update and call the appropriate model method
            $result = false;
            switch ($field) {
                case 'nombre':
                    $result = $this->model->updateTeacherName($NIFTeacher, $newValue);
                    break;
                case 'apellido1':
                    $result = $this->model->updateTeacherSurname1($NIFTeacher, $newValue);
                    break;
                case 'apellido2':
                    $result = $this->model->updateTeacherSurname2($NIFTeacher, $newValue);
                    break;
                case 'email':
                    $result = $this->model->updateTeacherEmail($NIFTeacher, $newValue);
                    break;
                case 'direccion':
                    $result = $this->model->updateTeacherAddress($NIFTeacher, $newValue);
                    break;
                case 'codigoPostal':
                    $result = $this->model->updateTeacherPostalCode($NIFTeacher, $newValue);
                    break;
                case 'municipio':
                    $result = $this->model->updateTeacherMunicipality($NIFTeacher, $newValue);
                    break;
                case 'provincia':
                    $result = $this->model->updateTeacherProvince($NIFTeacher, $newValue);
                    break;
                case 'categoria':
                    $result = $this->model->updateTeacherCategory($NIFTeacher, $newValue);
                    break;
                default:
                    echo "Field not recognized.";
                    return;
            }
    
            // Set the success or error message based on the result
            $message = $result ? "Teacher successfully updated." : "Error updating the teacher.";
            
            // Redirect to the teacher index page with the message
            header("Location: ../views/teacher/teacher_crud.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }
    
    

    private function showAllTeachers() {
        // Retrieve all teachers using the model
        $teachers = $this->model->getAllTeachers();
    
        // If there's an error, terminate the script
        if ($teachers === false) {
            die("Error retrieving teachers.");
        }
        
        // Load the teacher list view to display all teachers
        require '../views/teacher/teacher_list.php';
    }
}

// Instantiate the TeacherController class to handle the teacher-related actions
$controller = new TeacherController();

// Call the processRequest method to determine and execute the appropriate action 
$controller->processRequest();
?>
