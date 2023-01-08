<?php


// ?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;

if (!isset($_GET["word"]) || !isset($_GET["grille"]) || !isset($_GET["nbCol"]) || !isset($_GET["nbRows"])) {
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



$word = $_GET["word"];
$grille = implode(" ", explode(",", $_GET["grille"]));
$nbCols = $_GET["nbCol"];
$nbRows = $_GET["nbRows"];

// var_dump($grille);
$cmd = "..".$DS."dictionnary".$DS."executables".$DS."dictionnary_lookup".$DS." ..".$DS."files".$DS."dumps".$DS."dico_lexe.lex $word";
exec($cmd, $output, $return);
$output = intval($output[0]);

echo json_encode(array(
    "word" => $word,
    "code" => $output,
));
die;


// $cmd = "..\\dictionnary\\executables\\grid_path.exe $word $nbRows $nbCols $grille";
// exec($cmd, $output, $return);
// var_dump($output);


// echo json_encode(array(
//     "word" => $word,
//     "code" => $return,
// ));
