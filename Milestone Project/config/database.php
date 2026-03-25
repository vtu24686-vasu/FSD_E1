<?php
// Database Configuration - Dynamic Online Examination System

define('DB_HOST', '127.0.0.1');
define('DB_PORT', '3307');          // same port you see in phpMyAdmin
define('DB_NAME', 'online_exam_db'); // or exact name shown in phpMyAdmin
define('DB_USER', 'root');
define('DB_PASS', '');              // empty because you have no password

function getDBConnection() {
    try {
        $dsn = "mysql:host=" . DB_HOST . ";port=" . DB_PORT . ";dbname=" . DB_NAME . ";charset=utf8mb4";
        $pdo = new PDO($dsn, DB_USER, DB_PASS, [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            PDO::ATTR_EMULATE_PREPARES => false
        ]);
        return $pdo;
    } catch (PDOException $e) {
        die("Database connection failed: " . $e->getMessage());
    }
}