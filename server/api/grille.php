<?php
// S'occupe de la génération de la grille
require_once("../../public/controller/Partie.php");
require_once("../../public/config/config.php");
if(!isset($_SESSION["user"])){
    http_response_code(401);
    die;
}

// ?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;

// Test si les paramètres sont bien passés
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


exec($cmd, $output, $return);
if($return != 0){
    http_response_code(500);
    die;
}
// On récupère la grille
$output = implode(" ", $output);
$output = explode(" ", $output);

$voyelles = array("A", "E", "I", "O", "U", "Y");
// On crée la grille
for($i = 0; $i < $nbRows; $i++){
    $grille[$i] = array();
    for($j = 0; $j < $nbCol; $j++){
        if($i * $nbCol + $j < count($output))
        $grille[$i][$j] = $output[$i * $nbCol + $j];
        else
            $grille[$i][$j] = $voyelles[rand(0, count($voyelles) - 1)];
    }
}

// On récupère la date
date_default_timezone_set('Europe/Paris');
  $time = date('Y-m-d H:i:s');

  // Si le joueur avait déjà une partie en cours, on la récupère et on la sauvegarde
if(isset($_SESSION["partie"])){
    if($_SESSION["partie"] )

    $partie = unserialize($_SESSION["partie"]);
    $partie->setTimeStampFin($time);
    $partie->savePartie($conn);
}



// On crée une nouvelle partie et on lui met ses champs
$partie = new Partie();
$partie->setDimensions($nbRows, $nbCol);
$partie->setGrille($grille);
$partie->addJoueur($_SESSION["idUser"]);
$partie->setTimeStampDebut($time);

// On sauvegarde la partie en cours dans la session 
$_SESSION["partie"] = serialize($partie);



echo json_encode(array(
    "grille" => $grille,
));



