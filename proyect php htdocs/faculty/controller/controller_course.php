<?php
    require_once '../models/model_course.php'; // Include the model

    class CourseController {
        private $model;

        public function __construct() {
            $this->model = new ModelCourse(); // Instantiate the model
        }

        // Method to process the request
        public function processRequest() {
            if (isset($_GET['action'])) {
                $action = $_GET['action'];
                switch ($action) {
                    case 'view':
                        $this->viewCourse();
                        break;

                    case 'delete':
                        $this->deleteCourse();
                        break;

                    case 'create':
                        $this->createCourse();
                        break;

                    case 'update':
                        $this->updateCourse();
                        break;

                    case 'list':
                        $this->showAllCourses();
                        break;

                    default:
                        echo "Action not recognized.";
                }
            }
        }

        private function createCourse() {
            // Check if all required POST parameters are set
            if (isset($_POST['idCurso']) && isset($_POST['courseName']) && isset($_POST['numSubjects'])) {
                // Retrieve the input values
                $idCurso = $_POST['idCurso'];
                $courseName = $_POST['courseName'];
                $numSubjects = $_POST['numSubjects'];
                
                // Call the model's method to create the course
                $success = $this->model->createCourse($idCurso, $courseName, $numSubjects);
                
                // Set the success or error message based on the result
                $message = $success ? "Course successfully created." : "Error creating the course.";
                
                // Redirect to the course index page with the message
                header("Location: ../views/course/course_index.php?message=" . urlencode($message));
                exit();
            }
        }
        

        private function viewCourse() {
            // Check if the GET parameter 'idCurso' is set
            if (isset($_GET['idCurso'])) {
                // Retrieve the course ID from the GET parameter
                $idCourse = $_GET['idCurso'];
                
                // Call the model's method to get the course by ID
                $course = $this->model->getCourseById($idCourse);
                
                // Load the course view to display the course details
                require '../views/course/course_view.php';
            }
        }
        

        private function deleteCourse() {
            // Check if the GET parameter 'idCurso' is set
            if (isset($_GET['idCurso'])) {
                // Retrieve the course ID from the GET parameter
                $idCourse = $_GET['idCurso'];
                
                // Call the model's method to delete the course by ID
                $result = $this->model->deleteCourseById($idCourse);
                
                // Set the success or error message based on the result
                $message = $result > 0 ? "Course successfully deleted" : "Course not found or could not be deleted.";
                
                // Redirect to the course index page with the message
                header("Location: ../views/course/course_index.php?message=" . urlencode($message));
                exit();
            }
        }
        
        
        private function updateCourse() {
            // Check if all required POST parameters are set
            if (isset($_POST['idCourse']) && isset($_POST['field']) && isset($_POST['newValue'])) {
                // Retrieve the input values
                $idCourse = $_POST['idCourse'];
                $field = $_POST['field']; 
                $newValue = $_POST['newValue']; 
        
                // Determine which field to update and call the appropriate model method
                if ($field === 'courseName') {
                    $result = $this->model->updateCourseName($idCourse, $newValue);
                } elseif ($field === 'numSubjects') {
                    $result = $this->model->updateNumSubjects($idCourse, (int)$newValue);
                } else {
                    // If the field is not recognized, display an error message
                    echo "Field not recognized.";
                    return;
                }
        
                // Set the success or error message based on the result
                $message = $result > 0 ? "Course successfully updated." : "Error updating the course.";
                
                // Redirect to the course index page with the message
                header("Location: ../views/course/course_index.php?message=" . urlencode($message));
                exit();
            }
        }
        
        

        private function showAllCourses() {
            // Retrieve all courses using the model
            $courses = $this->model->getAllCourses();
        
            // If there's an error, terminate the script
            if ($courses === false) {
                die("Error retrieving courses.");
            }
        
            // Load the course list view to display all courses
            require '../views/course/course_list.php';
        }
        
    }

    // Instantiate the CourseController class to handle the course-related actions.
    $controller = new CourseController();

    // Call the processRequest method to determine and execute the appropriate action 
    // (e.g., create, view, delete, update) based on the 'action' parameter in the request.
    // This method ensures that the correct functionality is triggered depending on the user's request.
    $controller->processRequest();

?>
