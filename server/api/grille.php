<?php


// ?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;

if(!isset($_GET["nbCol"]) || !isset($_GET["nbRows"]) || !isset($_GET["langue"])){
    http_response_code(400);
    die;
}

$nbCol = $_GET["nbCol"];
$nbRows = $_GET["nbRows"];
$langue = $_GET["langue"];

$cmd = "..\\dictionnary\\executables\\grid_build.exe ..\\files\\dumps\\frequences.txt $nbCol $nbRows ";
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

echo json_encode(array(
    "grille" => $grille,
));


