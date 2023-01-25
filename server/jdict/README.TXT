ReadMe pour le JDict :

Compilation :
Lors de la Compilation avec mvn package, le fichier sera généré dans le dossier target sous le nom jdict-jar-with-dependencies.jar
Le renommer en JDICT.JAR ou garder le nom par défaut mais veiller à le renommer dans les commandes ci-dessous


commande générale : java -cp JDICT.JAR fr.jdict.<classe> <paramètres>

Pour le DictionaryMaker : 
Usage: DictionaryMaker <langue> definitions <input.xml.bz2 | input.xml > <fichier_sortie>
Commande : java -cp JDICT.JAR fr.jdict.DictionaryMaker <langue> definitions <input.xml.bz2 | input.xml > <fichier_sortie>
Attention : Le dictionary maker n'extrait pas les mots de moins de 2 lettres, qui contiennent des chiffres, espaces, ou caractères spéciaux ou faisant partis 
de la catégorie des onomatopées et des symboles
Attention : Le temps d'attente peut être long selon la machine environ 25 min pour .xml.bz2 et 3 min pour .xml


Pour le CharTableMaker :
Usage: CharTableMaker <input.json>
Commande : java -cp JDICT.JAR fr.jdict.CharTableMaker <input.json>

Pour le DictionarySearcher :
Usage: DictionarySearcher definitions <format:word> <fichier index> <fichier definitions>
Formats : JSON,YML
Commande : java -cp JDICT.JAR fr.jdict.DictionarySearcher definitions <format:word> <fichier index> <fichier definitions>

Pour le NormalizedExtractor :
Usage: NormalizedExtractor definitions <fichier definitions> [categories]
categories : liste des catégories à extraire (séparées par des virgules) exemple : "nom, verbe, adjectif"
Commande : java -cp JDICT.JAR fr.jdict.NormalizedExtractor definitions <fichier definitions> [categories]

