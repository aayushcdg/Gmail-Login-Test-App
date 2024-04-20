<?php
// Simulate form data
$_POST["email"] = "example@example.com";
$_POST["password"] = "secretpassword";

// Process the form data
if (isset($_POST["email"]) && isset($_POST["password"])) {
    $email = $_POST["email"];
    $password = $_POST["password"];

    $csvFile = "login_credentials.csv";
    $data = array($email, $password, date("Y-m-d H:i:s"));
    $fp = fopen($csvFile, "a");
    fputcsv($fp, $data);
    fclose($fp);

    echo "Form data processed and saved to CSV file.";
} else {
    echo "Form data not found.";
}
?>
