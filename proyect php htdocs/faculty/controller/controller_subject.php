<?php
require_once '../models/model_subject.php';

class SubjectController {
    private $model;

    public function __construct() {
        $this->model = new ModelSubject();
    }

    public function processRequest() {
        if (isset($_GET['action'])) {
            $action = $_GET['action'];
            switch ($action) {
                case 'view':
                    $this->viewSubject();
                    break;
                case 'delete':
                    $this->deleteSubject();
                    break;
                case 'create':
                    $this->createSubject();
                    break;
                case 'update':
                    $this->updateSubject();
                    break;
                case 'list':
                    $this->showAllSubjects();
                    break;
                default:
                    echo "Action not recognized.";
            }
        } else {
            echo "No action specified.";
        }
    }

    private function createSubject() {
        if (isset($_POST['curso'], $_POST['idAsignatura'], $_POST['nombre'], 
            $_POST['cuatrimestre'], $_POST['creditos'], $_POST['caracter'], $_POST['coordinador'])) {

            $course = (int) $_POST['curso'];
            $subjectId = htmlspecialchars($_POST['idAsignatura']);
            $name = htmlspecialchars($_POST['nombre']);
            $semester = htmlspecialchars($_POST['cuatrimestre']);
            $credits = (float) $_POST['creditos'];
            $type = htmlspecialchars($_POST['caracter']);
            $coordinator = htmlspecialchars($_POST['coordinador']);

            $success = $this->model->createSubject($course, $subjectId, $name, $semester, $credits, $type, $coordinator);

            $message = $success ? "Subject successfully created." : "Error creating the subject.";
            header("Location: ../views/subject/subject_index.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }

    private function viewSubject() {
        if (isset($_GET['idAsignatura'])) {
            $subjectId = htmlspecialchars($_GET['idAsignatura']);
            $subject = $this->model->getSubjectById($subjectId);

            if ($subject) {
                require '../views/subject/subject_view.php';
            } else {
                echo "No results found for the provided subject ID.";
            }
        } else {
            echo "No subject ID provided.";
        }
    }

    private function deleteSubject() {
        if (isset($_GET['idAsignatura'])) {
            $subjectId = htmlspecialchars($_GET['idAsignatura']);
            $result = $this->model->deleteSubjectById($subjectId);
    
            $message = $result > 0 ? "Subject successfully deleted." : "Subject not found or could not be deleted.";
            header("Location: ../views/subject/subject_index.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }
    

    private function updateSubject() {
        if (isset($_POST['idAsignatura'], $_POST['curso'], $_POST['nombre'], 
            $_POST['cuatrimestre'], $_POST['creditos'], $_POST['caracter'], $_POST['coordinador'])) {

            $subjectId = htmlspecialchars($_POST['idAsignatura']);
            $course = (int) $_POST['curso'];
            $name = htmlspecialchars($_POST['nombre']);
            $semester = htmlspecialchars($_POST['cuatrimestre']);
            $credits = (float) $_POST['creditos'];
            $type = htmlspecialchars($_POST['caracter']);
            $coordinator = htmlspecialchars($_POST['coordinador']);

            $result = $this->model->updateSubject($subjectId, $course, $name, $semester, $credits, $type, $coordinator);

            $message = $result > 0 ? "Subject successfully updated." : "Error updating the subject.";
            header("Location: ../views/subject/subject_index.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }

    private function showAllSubjects() {
        $subjects = $this->model->getAllSubjects();

        if ($subjects === false) {
            die("Error retrieving subjects.");
        }

        require '../views/subject/subject_list.php';
    }
}

$controller = new SubjectController();
$controller->processRequest();
?>
