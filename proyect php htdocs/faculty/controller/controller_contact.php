<?php
require_once '../models/model_contact.php'; // Include the corresponding model

class ContactController {
    private $model;

    public function __construct() {
        $this->model = new ModelContact(); // Instantiate the model
    }

    // Method to process the request
    public function processRequest() {
        if (isset($_GET['action'])) {
            $action = $_GET['action'];
            switch ($action) {
                case 'view':
                    $this->viewContact();
                    break;

                case 'delete':
                    $this->deleteContact();
                    break;

                case 'create':
                    $this->createContact();
                    break;

                case 'update':
                    $this->updateContact();
                    break;

                case 'list':
                    $this->showAllContacts();
                    break;

                default:
                    echo "Action not recognized.";
            }
        }
    }

    private function createContact() {
        // Check if all required POST parameters are set
        if (isset($_POST['idProfesor']) && isset($_POST['telefono'])) {
            // Retrieve input values
            $professorId = $_POST['idProfesor'];
            $phone = $_POST['telefono'];
            
            // Call the model method to create the contact
            $success = $this->model->createContact($professorId, $phone);
            
            // Set success or error message based on the result
            $message = $success ? "Contact successfully created." : "Error creating the contact.";
            
            // Redirect to the contacts index page with the message
            header("Location: ../views/contact/contact_index.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }

    private function viewContact() {
        if (isset($_GET['idProfesor'])) {
            $professorId = $_GET['idProfesor'];
            $contacts = $this->model->getContactById($professorId);
    
            // Check if contacts were found
            if ($contacts && count($contacts) > 0) {
                // Include the view and pass the contact data
                require '../views/contact/contact_view.php';
            } else {
                echo "No results found for the provided professor ID.";
            }
        } else {
            echo "No professor ID provided.";
        }
    }

    private function deleteContact() {
        // Check if the GET parameters 'idProfesor' and 'telefono' are set
        if (isset($_GET['idProfesor']) && isset($_GET['telefono'])) {
            // Retrieve input values
            $professorId = $_GET['idProfesor'];
            $phone = $_GET['telefono'];
            
            // Call the model method to delete the contact by ID and phone
            $result = $this->model->deleteContactByIdAndPhone($professorId, $phone);
            
            // Set success or error message based on the result
            $message = $result > 0 ? "Contact successfully deleted" : "Contact not found or could not be deleted.";
            
            // Redirect to the contacts index page with the message
            header("Location: ../views/contact/contact_index.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }

    private function updateContact() {
        // Check if all required POST parameters are set
        if (isset($_POST['idProfesor']) && isset($_POST['telefono']) && isset($_POST['newTelefono'])) {
            // Retrieve input values
            $professorId = $_POST['idProfesor'];
            $phone = $_POST['telefono'];
            $newPhone = $_POST['newTelefono'];
    
            // Call the model method to update the phone number
            $result = $this->model->updateContactPhone($professorId, $phone, $newPhone);
    
            // Set success or error message based on the result
            $message = $result ? "Contact successfully updated." : "Error updating the contact.";
            
            // Redirect to the contacts index page with the message
            header("Location: ../views/contact/contact_index.php?message=" . urlencode($message));
            exit();
        } else {
            echo "Required parameters are missing.";
        }
    }

    private function showAllContacts() {
        // Retrieve all contacts using the model
        $contacts = $this->model->getAllContacts();
    
        // If there's an error, terminate the script
        if ($contacts === false) {
            die("Error retrieving contacts.");
        }
        
        // Load the contact list view to display all contacts
        require '../views/contact/contact_list.php';
    }
}

// Instantiate the ContactController class to handle contact-related actions
$controller = new ContactController();

// Call the processRequest method to determine and execute the appropriate action
$controller->processRequest();
?>
