<?php
header('Content-Type: application/json');
if(isset($_GET['action'])){
    if($_GET['action'] == 'logout'){
        //destroy the session
        session_start();
        unset($_SESSION);
        $_SESSION = array();
        session_destroy();
        exit();
    }
}