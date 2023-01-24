<?php
require_once("../../public/controller/Partie.php");
require_once("../../public/config/config.php");
if(!isset($_SESSION["user"])){
    http_response_code(401);
    die;
}
// ?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;

if(!isset($_GET["nbCol"]) || !isset($_GET["nbRows"]) || !isset($_GET["langue"])){
    http_response_code(400);
    die;
}

$nbCol = $_GET["nbCol"];
$nbRows = $_GET["nbRows"];
$langue = $_GET["langue"];

//Si sur windows
if (strtoupper(substr(PHP_OS, 0, 3)) === 'WIN') {
    $ext = ".exe";
}else{
    $ext = "";
}
$DS = DIRECTORY_SEPARATOR; 

$cmd = "..".$DS."dictionnary".$DS."executables".$DS."grid_build".$ext. " ..".$DS."files".$DS."frequences.txt $nbCol $nbRows";
// ../files/dumps/frequences.txt $nbCol $nbRows
// exec("../dictionnary/executables/grid_build.exe ../files/dumps/frequences.txt 4 4", $output, $return);

exec($cmd, $output, $return);
if($return != 0){
    http_response_code(500);
    die;
}
$output = implode(" ", $output);
$output = explode(" ", $output);


for($i = 0; $i < $nbRows; $i++){
    $grille[$i] = array();
    for($j = 0; $j < $nbCol; $j++){
        $grille[$i][$j] = $output[$i * $nbCol + $j];
    }
}
if(isset($_SESSION["partie"])){
    if($_SESSION["partie"] )

    $partie = unserialize($_SESSION["partie"]);
    $partie->setTimeStampFin(date("Y-m-d H:i:s", time()));
    $partie->savePartie($conn);
}

$partie = new Partie();
$partie->setDimensions($nbRows, $nbCol);
$partie->setGrille($grille);
$partie->addJoueur($_SESSION["idUser"]);
$partie->setTimeStampDebut(date("Y-m-d H:i:s", time()));

$_SESSION["partie"] = serialize($partie);

echo json_encode(array(
    "grille" => $grille,
));



