<?php
class Database {
    private $host = "localhost";
    private $db_name = "facultad";
    private $username = "root";
    private $password = "";
    private static $instance = null;
    public $conn;

    private function __construct() {
        $this->conn = new mysqli($this->host, $this->username, $this->password, $this->db_name);
        if ($this->conn->connect_error) {
            die("Connection failed: " . $this->conn->connect_error);
        }
    }

    public static function getInstance() {
        if (self::$instance == null) {
            self::$instance = new Database();
        }
        return self::$instance;
    }

    public function getConnection() {
        return $this->conn;
    }
}
?>
