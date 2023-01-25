<?php
require_once("../../public/controller/Partie.php");
require_once("../../public/config/config.php");

/*
Récupère les définitions d'un mot depuis le module JAVA (DictionarySearcher) (non disponible sur tous les systèmes)
*/

if(!isset($_SESSION["user"])){
    http_response_code(401);
    die;
}
if(!array_key_exists("word", $_GET)) {
    echo "No word provided";
    exit;
}
header('Content-Type: application/json; charset=utf-8');


//clean $_GET input
foreach($_GET as $key => $value) {
    $_GET[$key] = htmlspecialchars($value);
    $_GET[$key] = escapeshellarg($value);

}

$word = strtoupper($_GET["word"]);

$DS = DIRECTORY_SEPARATOR;
$jarPath = "..".$DS."jdict".$DS."target".$DS."jdict-jar-with-dependencies.jar";
$className = "fr.uge.jdict.DictionarySearcher";
$jsonPath = "..".$DS."files".$DS."definitions_fr.json";
$indexPath = "..".$DS."files".$DS."dictionary.index";

$cmd = "java -cp $jarPath $className definitions $word $indexPath $jsonPath";

exec($cmd, $output, $return);


// On réencode en UTF-8, et on remplace les caractères unicode par leur équivalent UTF-8
$output = implode("\n", $output);
$output = mb_convert_encoding($output, 'UTF-8', mb_detect_encoding($output, 'UTF-8, ISO-8859-1', true));
$output = preg_replace_callback('/\\\\u([0-9a-f]{4})/i', function($match) {
    return mb_convert_encoding(pack('H*', $match[1]), 'UTF-8', 'UCS-2BE');
}, $output);

$output = preg_replace('/}\s*{/', '},{', $output);



echo "[".$output."]";
