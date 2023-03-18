<?php
require "connection.php";
// $username = "a";
// $password = "a";
$username = $_POST["username"];
$password = $_POST["password"];
if($connection){
    $sql = "SELECT * FROM users WHERE username = '$username' AND password = '$password'";
    $loginQuery = mysqli_query($connection,$sql);
    if(mysqli_num_rows($loginQuery) > 0) {
        echo "Login successful!";
    } else {
        echo "Wrong credentials!";
    }
} else {
    echo "Connection error!";
}
?>