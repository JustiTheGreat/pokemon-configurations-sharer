<?php
require "connection.php";
$username = $_POST["username"];
$name = $_POST["name"];
$species = $_POST["species"];
$gender = $_POST["gender"];
$level = $_POST["level"];
$ability = $_POST["ability"];
$nature = $_POST["nature"];
$move1 = $_POST["move1"];
$move2 = $_POST["move2"];
$move3 = $_POST["move3"];
$move4 = $_POST["move4"];
$ivs = explode(":",$_POST["ivs"]);
$evs = explode(":",$_POST["evs"]);
// $username = "a";
// $name = "boi";
// $species = "Caterpie";
// $gender = "♂";
// $level = 1;
// $ability = "Run Away";
// $nature = "Bashful";
// $move1 = "Bug Bite";
// $move2 = "";
// $move3 = "";
// $move4 = "";
// $ivs = explode(":","0:0:0:0:0:0");
// $evs = explode(":","0:0:0:0:0:0");
if($connection) {
    $user = mysqli_fetch_array(mysqli_query($connection,"SELECT id_user FROM users WHERE username = '$username'"));
    $id_user = $user["id_user"];
    $pokemon = mysqli_fetch_array(mysqli_query($connection,"SELECT * FROM pokemon WHERE id_user = $id_user AND name = '$name'"));
    if($pokemon) {
        echo "Pokemon name in use!";
    }
    else {
        mysqli_query($connection,"INSERT INTO pokemon (name,species,gender,level,ability,nature,move1,move2,move3,move4,id_user)
                    VALUES ('$name','$species','$gender',$level,'$ability','$nature','$move1','$move2','$move3','$move4',$id_user)");

        $pokemon = mysqli_fetch_array(mysqli_query($connection,"SELECT * FROM pokemon WHERE id_user = $id_user AND name = '$name'"));
        $id_pokemon = $pokemon["id_pokemon"];

        mysqli_query($connection,"INSERT INTO ivs (id_pokemon,hp,attack,defense,special_attack,special_defense,speed)
                    VALUES ($id_pokemon,$ivs[0],$ivs[1],$ivs[2],$ivs[3],$ivs[4],$ivs[5])");

        mysqli_query($connection,"INSERT INTO evs (id_pokemon,hp,attack,defense,special_attack,special_defense,speed)
                    VALUES ($id_pokemon,$evs[0],$evs[1],$evs[2],$evs[3],$evs[4],$evs[5])");
                    
        echo "Insert successful!";
    }
} else {
    echo "Connection error!";
}
?>