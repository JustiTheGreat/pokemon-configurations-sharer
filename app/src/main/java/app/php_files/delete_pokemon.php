<?php
require "connection.php";
if($connection) {
    // $id_pokemon = 7;
    $id_pokemon = $_POST["id_pokemon"];
    $pokemon = mysqli_query($connection,"SELECT * FROM pokemon WHERE id_pokemon = $id_pokemon");
    if(mysqli_num_rows($pokemon) == 0) {
        echo 'Pokemon ID doesn\'t exist!';
    } else {
        $delete_pokemon = "DELETE FROM pokemon WHERE id_pokemon = $id_pokemon";
        $delete_pokemon_ivs = "DELETE FROM ivs WHERE id_pokemon = $id_pokemon";
        $delete_pokemon_evs = "DELETE FROM evs WHERE id_pokemon = $id_pokemon";
        if(!mysqli_query($connection,$delete_pokemon_ivs)) {
            echo "Couldn\'t delete your pokemon\'s IVs!";
        } else if(!mysqli_query($connection,$delete_pokemon_evs)) {
            echo "Couldn\'t delete your pokemon\'s EVs!";
        } else if(!mysqli_query($connection,$delete_pokemon)) {
            echo "Couldn\'t delete your pokemon!";
        } else  {
            echo "Delete successful!";
        }
    }
} else {
    echo "Connection error!";
}
?>