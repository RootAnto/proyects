<?php
    require_once '../models/model_student.php'; // Include the model

    class StudentController {
        private $model;

        public function __construct() {
            $this->model = new ModelStudent(); // Instantiate the model
        }

        // Method to process the request
        public function processRequest() {
            if (isset($_GET['action'])) {
                $action = $_GET['action'];
                switch ($action) {
                    case 'view':
                        $this->viewStudent();
                        break;

                    case 'delete':
                        $this->deleteStudent();
                        break;

                    case 'create':
                        $this->createStudent();
                        break;

                    case 'update':
                        $this->updateStudent();
                        break;

                    case 'list':
                        $this->showAllStudents();
                        break;

                    default:
                        echo "Action not recognized.";
                }
            }
        }

        private function createStudent() {
            // Check if all required POST parameters are set
            if (isset($_POST['idStudent']) && isset($_POST['NIF']) && isset($_POST['name']) && isset($_POST['surname1']) && isset($_POST['email']) && isset($_POST['address']) && isset($_POST['postalCode']) && isset($_POST['city']) && isset($_POST['province'])) {
                // Retrieve the input values
                $idStudent = $_POST['idStudent'];
                $NIF = $_POST['NIF'];
                $name = $_POST['name'];
                $surname1 = $_POST['surname1'];
                $surname2 = $_POST['surname2'];
                $email = $_POST['email'];
                $address = $_POST['address'];
                $postalCode = $_POST['postalCode'];
                $city = $_POST['city'];
                $province = $_POST['province'];
                
                // Call the model's method to create the student
                $success = $this->model->createStudent($idStudent, $NIF, $name, $surname1, $surname2, $email, $address, $postalCode, $city, $province);
                
                // Set the success or error message based on the result
                $message = $success ? "Student successfully created." : "Error creating the student.";
                
                // Redirect to the student index page with the message
                header("Location: ../views/student/student_index.php?message=" . urlencode($message));
                exit();
            }
        }
        
        private function viewStudent() {
            // Check if the GET parameter 'NIF' is set
            if (isset($_GET['NIF'])) {
                // Retrieve the student NIF from the GET parameter
                $NIF = $_GET['NIF'];
                
                // Call the model's method to get the student by NIF
                $student = $this->model->getStudentByNIF($NIF);
                
                // Load the student view to display the student details
                require '../views/student/student_view.php';
            }
        }
        
        private function deleteStudent() {
            // Check if the GET parameter 'NIF' is set
            if (isset($_GET['NIF'])) {
                // Retrieve the student NIF from the GET parameter
                $NIF = $_GET['NIF'];
                
                // Call the model's method to delete the student by NIF
                $result = $this->model->deleteStudentByNIF($NIF);
                
                // Set the success or error message based on the result
                $message = $result > 0 ? "Student successfully deleted" : "Student not found or could not be deleted.";
                
                // Redirect to the student index page with the message
                header("Location: ../views/student/student_index.php?message=" . urlencode($message));
                exit();
            }
        }
        
        private function updateStudent() {
            // Check if all required POST parameters are set
            if (isset($_POST['NIF']) && isset($_POST['field']) && isset($_POST['newValue'])) {
                // Retrieve the input values
                $NIF = $_POST['NIF'];
                $field = $_POST['field'];
                $newValue = $_POST['newValue'];
                
                // Determine which field to update and call the appropriate model method
                switch ($field) {
                    case 'nombre':
                        $result = $this->model->updateStudentName($NIF, $newValue);
                        break;
        
                    case 'apellido1':
                        $result = $this->model->updateStudentSurname1($NIF, $newValue);
                        break;
        
                    case 'apellido2':
                        $result = $this->model->updateStudentSurname2($NIF, $newValue);
                        break;
        
                    case 'email':
                        $result = $this->model->updateStudentEmail($NIF, $newValue);
                        break;
        
                    case 'direccion':
                        $result = $this->model->updateStudentAddress($NIF, $newValue);
                        break;
        
                    case 'codigoPostal':
                        $result = $this->model->updateStudentPostalCode($NIF, (int)$newValue);
                        break;
        
                    case 'municipio':
                        $result = $this->model->updateStudentCity($NIF, $newValue);
                        break;
        
                    case 'provincia':
                        $result = $this->model->updateStudentProvince($NIF, $newValue);
                        break;
        
                    default:
                        // If the field is not recognized, display an error message
                        echo "Field not recognized.";
                        return;
                }
        
                // Set the success or error message based on the result
                $message = $result > 0 ? "Student successfully updated." : "Error updating the student.";
                
                // Redirect to the student index page with the message
                header("Location: ../views/student/student_index.php?message=" . urlencode($message));
                exit();
            }
        }
        
        
        private function showAllStudents() {
            // Retrieve all students using the model
            $students = $this->model->getAllStudents();
        
            // If there's an error, terminate the script
            if ($students === false) {
                die("Error retrieving students.");
            }
        
            // Load the student list view to display all students
            require '../views/student/student_list.php';
        }
        
    }

    // Instantiate the StudentController class to handle the student-related actions.
    $controller = new StudentController();

    // Call the processRequest method to determine and execute the appropriate action 
    // (e.g., create, view, delete, update) based on the 'action' parameter in the request.
    $controller->processRequest();

?>
