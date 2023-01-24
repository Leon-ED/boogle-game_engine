<?php
require_once("config/config.php");
if(!isset($_SESSION["user"])){
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
    <title>Boogle</title>
</head>
<body>
    <h1>Mon compte</h1>
    <p>Bonjour <?php echo $_SESSION["user"]["pseudoUser"]; ?>, voici quelques informations en rapport avec votre compte</p>
    <a onclick="logout()" href="login.php">Deconnexion</a>
</body>
</html>