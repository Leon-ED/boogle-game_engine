<?php
require_once("config/config.php");
require_once("controller/Joueur.php");
$joueur = new Joueur($_SESSION["idUser"]);
if (!isset($_SESSION["user"])) {
    header("Location: login.php");
    exit();
}
?>
<script src="./controller/functions.js" defer></script>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/styles/main.css">
    <title>Boogle - Le compte </title>
</head>

<body>
    <?php require_once("include/nav_bar.html"); ?>


    <main class="flex-column">
        <section>
            <h1>Mon compte</h1>
            <p>Bonjour <?php echo $_SESSION["user"]["pseudoUser"]; ?>, voici quelques informations en rapport avec votre compte :</p>
            <p>Vous avez joué <span class="stat"><?php echo $joueur->nombrePartiesJouees(); ?></span> parties.</p>
            <p>La partie la plus longue a duré <span class="stat"><?php echo $joueur->partiePlusLongue(); ?></span>.</p>
            <p>La partie la plus rapide a duré <span class="stat"><?php echo $joueur->partiePlusRapide(); ?></span>.</p>
        </section>
        <section>
            <h1>Vos 5 dernières parties :</h1>
                <?php
                    $joueur->getParties(5);
                ?>
        </section>
    </main>
</body>

</html>