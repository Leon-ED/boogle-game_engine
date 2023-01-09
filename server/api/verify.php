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
$cmd = "..".$DS."dictionnary".$DS."executables".$DS."dictionnary_lookup ..".$DS."files".$DS."dumps".$DS."dico_lexe.lex $word";
// var_dump($cmd);
exec($cmd, $output, $return);
$output = intval($output[0]);
// var_dump($output);
// var_dump($return);
if($output == 2):
echo json_encode(array(
    "word" => $word,
    "code" => $output,
));
die;
endif;

$cmd = "..".$DS."dictionnary".$DS."executables".$DS."grid_path".$ext." $word $nbRows $nbCols $grille";
exec($cmd, $output, $return);
// var_dump($output);
// var_dump($return);


echo json_encode(array(
    "word" => $word,
    "code" => $return,
));
