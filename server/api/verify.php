<?php


// ?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;

if(!isset($_GET["word"]) || !isset($_GET["grille"])){
    http_response_code(400);
    die;
}

$word = $_GET["word"];
$grille = implode(" ", explode(",", $_GET["grille"]));


$cmd = "..\\dictionnary\\executables\\dictionnary_lookup.exe ..\\files\\dumps\\dico_lexe.lex $word";
exec($cmd, $output, $return);
$output = intval($output[0]);
// if($output == 2){
//     echo json_encode(array(
//         "word" => $word,
//         "code" => $output,
//     ));
//     die;
// }

$cmd = "..\\dictionnary\\executables\\grid_path.exe $word 7 7 $grille";
exec($cmd, $output, $return);
var_dump($output);


echo json_encode(array(
    "word" => $word,
    "code" => $return,
));








