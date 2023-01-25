<?php
if (PHP_SESSION_NONE === session_status()) {
    session_start();
}
// error_reporting(E_ERROR | E_PARSE);
require_once __DIR__ . "/base.php";
define("DEF_DISPO",true);

