<?php
require_once("config/config.php");
?>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Projet du BUT Informatique">
    <meta name="author" content="Delmas Denis, Edmee Léon">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="../files/style.css?v<?= time() ?>">

    <title>Boggle</title>
    <link rel="icon" href="../files/img/genepi.png">
</head>
<?php
if (isset($_SESSION['user'])) {
    header("Location: ./index.php");
    exit();
}


?>

<body>
    <style>
        body {
            background-repeat: no-repeat;
            background-size: 47%;

        }

        section {
            font-weight: bold;
        }
    </style>
    <main style="display: flex; flex-direction: column; align-items: center; justify-content: center;">
        <section style="margin-top: 10%; margin-bottom: 5%; ">
            <h1><strong> Créez votre compte Boogle !</strong></h1>
        </section>
        <div>
            <h1>S'inscrire</h1>
            <div>
                <span>Un compte ? <a href="login.php">Pas de problème</a></span>
            </div>
        </div>
        <section class="login-section" style="width: 30%; ">
            <fieldset id="fieldset">
                <form class="form" method="POST" action="controller/auth.php" disabled>
                    <div class="form-group">
                        <label for="login">Identifiant</label>
                        <input type="text" class="form-control" id="login" name="login" aria-describedby="emailHelp" placeholder="Identifiant" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Mot de passe</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Mot de passe" required>

                    </div>
                    <div class="form-group">
                        <label for="password_confirm">Confirmer mot de passe</label>
                        <input type="password" class="form-control" id="password_confirm" name="password_confirm" placeholder="Mot de passe" required>

                    </div>
                    <br>
                    <small id="emailHelp" class="form-text text-muted">Nous ne partagerons jamais votre mot de passe avec qui que ce soit, faites-en de même</small>
                    <br>
                    <button type="submit" class="btn btn-primary"> Créer compte </button>
                    <input type="hidden" name="action" value="register">
                </form>
            </fieldset>

        </section>
    </main>

</body>