<?php
require "connection.php";
$username = $_POST["username"];
$email = $_POST["email"];
$password = $_POST["password"];


if($connection) {
    $usernameExistQuery = mysqli_query($connection,"SELECT * FROM user WHERE username = '$username'");
    $emailExistQuery = mysqli_query($connection,"SELECT * FROM user WHERE email = '$email'");

    if(strlen($username) == 0) {
        echo "Please input your username!";
    } else if(mysqli_num_rows($usernameExistQuery) != 0) {
        echo "Username already in use!";
    } else if(strlen($password) == 0) {
        echo "Please input your password!";
    } else if(strlen($password) < 6 || strlen($password) > 20) {
        echo "Password length must be more than 6 and less than 20!";
    } else if(filter_var($email,FILTER_VALIDATE_EMAIL) == false) {
        echo "Email is not valid!";
    } else if(mysqli_num_rows($emailExistQuery) != 0) {
        echo "Email already in use!";
    } else {
        $sql3 = "INSERT INTO users(username,password,email) VALUES ('$username','$password','$email')";
        if(mysqli_query($connection,$sql3)) {
            echo "Register successful!";
        } else {
            echo "Failed to register you account!";
        }
    }
} else {
    echo "Connection error!";
}
?>