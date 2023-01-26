<?php
require_once("../../public/config/config.php");
if(!isset($_SESSION["user"])){
    http_response_code(401);
    die;
}

if (!isset($_GET["grille"]) || !isset($_GET["nbCol"]) || !isset($_GET["nbRows"])) {
    http_response_code(400);
    die;
}

//Si sur windows
if (strtoupper(substr(PHP_OS, 0, 3)) === 'WIN') {
    $ext = ".exe";
}else{
    $ext = "";
}
$DS = DIRECTORY_SEPARATOR;



$grille = implode(" ", explode(",", strtoupper($_GET["grille"])));
$nbCols = $_GET["nbCol"];
$nbRows = $_GET["nbRows"];

$min_size  = 2;
$dico_lex = "..".$DS."files".$DS."dico_fr.lex";
$cmd = "..".$DS."dictionnary".$DS."executables".$DS."solve".$ext." $dico_lex $min_size $nbCols $nbRows $grille";
echo $cmd;

exec($cmd, $output, $return);
var_dump($output);
var_dump($return);
