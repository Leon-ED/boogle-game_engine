<?php
// Page d'une partie d'un joueur, permet de voir les statistiques de la partie
require_once("config/config.php");
require_once("controller/Joueur.php");


$joueur = new Joueur($_SESSION["idUser"]);
if (!isset($_SESSION["user"])) {
    header("Location: login.php");
    exit();
}
?>
<script src="./controller/functions.js" defer></script>
<script src="assets/scripts/main.js" defer></script>


<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/styles/main.css">
    <title>Boogle - La partie </title>
</head>

<body>
    <?php require_once("include/nav_bar.html"); 
    
    $partie = $joueur->getStatPartie($_GET["idPartie"], $_SESSION["idUser"]);
    $motsTrouves = $joueur->getListMotsTrouves($_GET["idPartie"], $_SESSION["idUser"]);
    $nbMotsTrouves = count($motsTrouves);
    $dureePartie = date("U", strtotime($partie["DateFinPartie"])) - date("U", strtotime($partie["DateDebutPartie"]));
    $dureePartie = $dureePartie / 60;
    $dureePartie = round($dureePartie, 2);
    $dureePartie = $dureePartie . " minutes (" . $dureePartie * 60 . " secondes)";

    $grille = json_decode($partie["Grille"]);
    $nbCols = explode("x", $partie["dimensionsGrille"])[0];
    $nbRows = explode("x", $partie["dimensionsGrille"])[1];    
    ?>


    <main class="flex-column">
        <section>
            <h1>Ma partie</h1>
            <p>Voici quelques informations en rapport avec votre partie :</p>
            <p>La partie a duré <span class="stat"><?php echo $dureePartie; ?></span>.</p>
            <p>Dans cette partie vous avez trouvé <span class="stat"><?php echo $nbMotsTrouves; ?></span> mot(s).</p>
            <p> Voici la liste des mots que vous avez trouvés : </p>
            <ul>
                <?php
                foreach ($motsTrouves as $mot) {
                    echo "<li>" . $mot["MotsPropose"] . "</li>";
                }
                ?>
        </section>
        <section id="grille-section" class="flex-column">
            <div id="grille" class= "flex-column">
                <?php
                echo "<h1>Voici la grille</h1>";
                echo "<p>La grille a une taille de " . $partie["dimensionsGrille"] . ".</p>";
                for ($row = 0; $row < $nbCols; $row++) {
                    echo "<div class='ligne'>";
                    for ($col = 0; $col < $nbRows; $col++) {
                        echo "<div class='case'>" . $grille[$row][$col] . "</div>";
                    }
                    echo "</div>";
                }
                ?>
            </div>
        </section>
    </main>
</body>

</html>