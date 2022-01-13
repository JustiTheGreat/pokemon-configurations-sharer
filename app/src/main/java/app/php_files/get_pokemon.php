<?php
require "connection.php";
// $username = "a";
$username = $_POST["username"];
if($connection){
    $result = "";
    $user = mysqli_fetch_array(mysqli_query($connection,"SELECT id_user FROM users WHERE username = '$username'"));
    $id_user = $user["id_user"];
    $pokemon = mysqli_query($connection,"SELECT * FROM pokemon WHERE id_user = '$id_user'");
    while($row=mysqli_fetch_array($pokemon)){
        $result = $result
                    .$row["id_pokemon"]
                    .";".$row["name"]
                    .";".$row["species"]
                    .";".$row["gender"]
                    .";".$row["level"]
                    .";".$row["ability"]
                    .";".$row["nature"]
                    .";".$row["move1"]
                    .";".$row["move2"]
                    .";".$row["move3"]
                    .";".$row["move4"]
                    .";";
        $id_pokemon = $row["id_pokemon"];
        $ivs = mysqli_query($connection,"SELECT * FROM ivs WHERE id_pokemon = '$id_pokemon'");
        while($row2=mysqli_fetch_array($ivs)){
            $result = $result
                        .$row2["hp"]
                        .";".$row2["attack"]
                        .";".$row2["defense"]
                        .";".$row2["special_attack"]
                        .";".$row2["special_defense"]
                        .";".$row2["speed"]
                        .";";
        }
        $evs = mysqli_query($connection,"SELECT * FROM evs WHERE id_pokemon = '$id_pokemon'");
        while($row2=mysqli_fetch_array($evs)){
            $result = $result
                        .$row2["hp"]
                        .";".$row2["attack"]
                        .";".$row2["defense"]
                        .";".$row2["special_attack"]
                        .";".$row2["special_defense"]
                        .";".$row2["speed"]
                        .";";
        }
        $result = $result."\n";
    }
    echo $result;
} else {
    echo "Connection error!";
}
?>