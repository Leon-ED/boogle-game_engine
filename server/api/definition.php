<?php
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

$output = implode("\n", $output);
// convert to utf-8
$output = mb_convert_encoding($output, 'UTF-8', mb_detect_encoding($output, 'UTF-8, ISO-8859-1', true));
// convert unicode codes to chars
$output = preg_replace_callback('/\\\\u([0-9a-f]{4})/i', function($match) {
    return mb_convert_encoding(pack('H*', $match[1]), 'UTF-8', 'UCS-2BE');
}, $output);

// replace all }{ by },{ to make it a valid json array
$output = preg_replace('/}\s*{/', '},{', $output);



echo "[".$output."]";
