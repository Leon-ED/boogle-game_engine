<?php
/*
Module s'occupant de la connexion et de l'inscription des utilistateurs
*/
require_once("../config/config.php");

if (!isset($_POST["action"])) {
    header("Location: ../login.php");
    exit();
}

$action = $_POST["action"];
if ($action == "login") {
    login();
} else if ($action == "register") {
    register();
} else {
    header("Location: ../login.php");
    exit();
}

function login()
{
    global $conn;

    $login = $_POST["login"];
    $password = $_POST["password"];
    $sql = "SELECT * FROM utilisateur WHERE login = :login";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(
        ":login" => $login
    ));
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    $goodPassword = password_verify($password, $user["password"]);
    if (!$user) {
        header("Location: ../login.php?error=1");
        exit();
    }
    if ($goodPassword) {
        session_start();
        $_SESSION["user"] = $user;
        $_SESSION["idUser"] = $user["idUser"];
        $_SESSION["email"] = $user["email"];

        header("Location: ../index.php");
        exit();
    } else {
        header("Location: ../login.php?error=1");
        exit();
    }
}

function register()
{
    global $conn;

    $login = $_POST["login"];
    $password = $_POST["password"];
    $password_confirm = $_POST["password_confirm"];
    $email = $_POST["email"];

    if ($password != $password_confirm) {
        header("Location: ../login.php?error=1");
        exit();
    }

    $password = password_hash($password, PASSWORD_DEFAULT);

    $sql = "INSERT INTO utilisateur (pseudoUser,login, password,email) VALUES (:login,:login, :password, :email)";
    $stmt = $conn->prepare($sql);
    $stmt->execute(array(
        ":login" => $login,
        ":password" => $password,
        ":email" => $email
    ));
    header("Location: ../login.php");
    exit();
}
