<?php
require "connection.php";
if($connection) {
    $id_pokemon = $_POST["id_pokemon"];
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
    // $id_pokemon = 9;
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
    $pokemon = mysqli_query($connection,"SELECT * FROM pokemon WHERE id_pokemon = $id_pokemon");
    if(mysqli_num_rows($pokemon) == 0) {
        echo 'Pokemon ID doesn\'t exist!';
    } else {
        $update_pokemon = "UPDATE pokemon SET 
                                                name = '$name', 
                                                species = '$species', 
                                                gender = '$gender', 
                                                level = $level, 
                                                ability = '$ability', 
                                                move1 = '$move1', 
                                                move2 = '$move2', 
                                                move3 = '$move3', 
                                                move4 = '$move4'
                                        WHERE id_pokemon = '$id_pokemon'";
        $update_pokemon_ivs = "UPDATE ivs SET
                                                hp = $ivs[0], 
                                                attack = $ivs[1], 
                                                defense = $ivs[2], 
                                                special_attack = $ivs[3], 
                                                special_defense = $ivs[4], 
                                                speed = $ivs[5] 
                                        WHERE id_pokemon = '$id_pokemon'";
        $update_pokemon_evs = "UPDATE evs SET
                                                hp = $evs[0], 
                                                attack = $evs[1], 
                                                defense = $evs[2], 
                                                special_attack = $evs[3], 
                                                special_defense = $evs[4], 
                                                speed = $evs[5] 
                                        WHERE id_pokemon = '$id_pokemon'";
        if(!mysqli_query($connection,$update_pokemon)) {
            echo "Couldn\'t update your pokemon!";
        } else if(!mysqli_query($connection,$update_pokemon_ivs)) {
            echo "Couldn\'t update your pokemon\'s IVs!";
        } else if(!mysqli_query($connection,$update_pokemon_evs)) {
            echo "Couldn\'t update your pokemon\'s EVs!";
        } else {
            echo "Update successful!";
        }
    }
} else {
    echo "Connection error!";
}
?>