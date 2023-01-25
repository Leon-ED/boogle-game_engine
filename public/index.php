<?php
require_once("config/config.php");
if (!isset($_SESSION["user"])) {
    header("Location: login.php");
    exit();
}

?>
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="assets/styles/main.css">
    <link rel="stylesheet" href="assets/styles/definitions.css">
    <script src="assets/scripts/main.js" defer></script>
    <script src="assets/scripts/words.js" defer></script>
    <title>Boogle - Le jeu !</title>
</head>

<body>
    <?php require_once("include/nav_bar.html"); ?>
    <main class="grid-columns">
        <section class="param-section">
            <h1>Paramètres de la partie</h1>
            <div id="form-Choose">
                <div class="form-elems">
                    <div>
                        <label for="langue">Français</label>
                        <input type="radio" name="langue" id="langue" value="fr" checked>
                    </div>
                    <div>
                        <label for="nbCol">Nombre de colonnes</label>
                        <input type="number" name="nbCol" id="nbCol" value="4" min="2" max="10">
                    </div>
                    <div>
                        <label for="nbRows">Nombre de lignes</label>
                        <input type="number" name="nbRows" id="nbRows" value="4" min="2" max="10">
                    </div>
                    <input type="submit" value="Nouvelle partie" id="form-submit">
                </div>
                <?php
                if(!DEF_DISPO){
                    echo "<div class='error'><p>En raison d'un problème lié au gestionnaire d'infrastructure du réseau<br>l'affichage des définitions n'est pas disponible.</p></div>";
                }
?>
                </section>
        <section id="grille-section" class="flex-column hidden">
            <div id="grille">

            </div>
            <label for="word">Mot à chercher</label>
            <div class="flex-row">
                <input type="text" name="word" id="word">
                <input type="submit" value="Chercher" id="word-submit">
            </div>

        </section>
        <section id="word-list" class="hidden">
        </section>
    </main>
    <script defer>
       
    </script>
    <script defer>

    </script>


</body>

</html>