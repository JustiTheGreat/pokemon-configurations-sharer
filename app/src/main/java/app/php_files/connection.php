<?php
$db_name = "chs";
$username = "root";
$password = "";
$servename = "localhost";
$connection = mysqli_connect($servename,$username,$password,$db_name) or die(mysqli_error($connection));
mysqli_set_charset($connection ,"utf8");
?>