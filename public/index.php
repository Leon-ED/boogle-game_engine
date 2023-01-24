<?php
require_once("config/config.php");
if(!isset($_SESSION["user"])){
    header("Location: login.php");
    exit();
}

?>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Boogle</title>
</head>
<body>
    <style>
        .tree{
  --spacing : 1.5rem;
  --radius  : 10px;
}

.tree li{
  display      : block;
  position     : relative;
  padding-left : calc(2 * var(--spacing) - var(--radius) - 2px);
}

.tree ul{
  margin-left  : calc(var(--radius) - var(--spacing));
  padding-left : 0;
}

.tree ul li{
  border-left : 2px solid #ddd;
}

.tree ul li:last-child{
  border-color : transparent;
}

.tree ul li::before{
  content      : '';
  display      : block;
  position     : absolute;
  top          : calc(var(--spacing) / -2);
  left         : -2px;
  width        : calc(var(--spacing) + 2px);
  height       : calc(var(--spacing) + 1px);
  border       : solid #ddd;
  border-width : 0 0 2px 2px;
}

.tree summary{
  display : block;
  cursor  : pointer;
}

.tree summary::marker,
.tree summary::-webkit-details-marker{
  display : none;
}

.tree summary:focus{
  outline : none;
}

.tree summary:focus-visible{
  outline : 1px dotted #000;
}

.tree li::after,
.tree summary::before{
  content       : '';
  display       : block;
  position      : absolute;
  top           : calc(var(--spacing) / 2 - var(--radius));
  left          : calc(var(--spacing) - var(--radius) - 1px);
  width         : calc(2 * var(--radius));
  height        : calc(2 * var(--radius));
  border-radius : 50%;
  background    : #ddd;
}

.tree summary::before{
  content     : '+';
  z-index     : 1;
  background  : #696;
  color       : #fff;
  line-height : calc(2 * var(--radius) - 2px);
  text-align  : center;
}

.tree details[open] > summary::before{
  content : '−';
}
    </style>
    <h1>Choisir données:</h1>
    <div id="form-Choose">
        <div>
            <div>
                <label for="langue">Français</label>
                <input type="radio" name="langue" id="langue" value="fr" checked>
        </div>
        <div>
            <label for="nbCol">Nombre de colonnes</label>
            <input type="number" name="nbCol" id="nbCol" value="4" min="2" max="10">
            <label for="nbRows">Nombre de lignes</label>
            <input type="number" name="nbRows" id="nbRows" value="4" min="2" max="10">           
        </div>
        <input type="submit" value="Valider" id="form-submit">
    </div>
    <h1>Grille :</h1>
    <section>
        <div id="grille">

        </div>
        <label for="word">Mot à chercher</label>
        <input type="text" name="word" id="word">
        <input type="submit" value="Chercher" id="word-submit">

    </section>
    <section id="word-list">
        <h1> Mots trouvés : </h1>
        
    </section>    
    <script defer>
        const form = document.getElementById('form-submit');
        const nbCol = document.getElementById('nbCol');
        const nbRows = document.getElementById('nbRows');
        const langue = document.getElementById('langue');

        form.addEventListener('click', (e) => {
            e.preventDefault();
            const nbColValue = nbCol.value;
            const nbRowsValue = nbRows.value;
            const langueValue = langue.value;
            console.log(nbColValue, nbRowsValue, langueValue);
            const url = "../server/api/grille.php";
            const params = `?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;
            fetch(url + params)
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                data = data.grille
                const grille = document.getElementById('grille');
                grille.innerHTML = '';

                for(var i = 0; i < nbRowsValue ; i++){
                    const line = document.createElement('div');
                    console.log(line);
                    line.classList.add('ligne');
                    for(var j = 0; j < nbColValue ; j++){
                        var lettre = data[i][j];
                        console.log(lettre);
                        const div = document.createElement('div');
                        div.classList.add('case');
                        div.innerHTML = lettre;
                        line.appendChild(div);
                    }
                    grille.appendChild(line);
                }

            })
            .catch((error) => {
                console.error('Error:', error);
            });
        });
    </script>
    <script defer>
        foundWords = [];
        const word = document.getElementById('word-submit');
        word.addEventListener('click', (e) => {
            e.preventDefault();
            const wordValue = document.getElementById('word').value;
            // console.log(wordValue);
            if(foundWords.includes(wordValue)){
                alert('Le mot a déjà été trouvé');
                return;
            }
            const url = "../server/api/verify.php";
            const grille = document.getElementById('grille');
            const lettres = grille.querySelectorAll('.case');
            const nbCol = document.getElementById('nbCol');
            const nbRows = document.getElementById('nbRows');
            let grilleArray = [];   
            lettres.forEach((lettre) => {
                grilleArray.push(lettre.innerHTML);
            });
            const params = `?word=${wordValue}&grille=${grilleArray}&nbCol=${nbCol.value}&nbRows=${nbRows.value}`;
            fetch(url + params)
            .then((response) => response.json())
            .then((data) => {
                if(data.code === 0){
                    alert('Le mot est dans la grille et dans le dictionnaire');
                    foundWords.push(wordValue);
                    addWord(wordValue);
                }
                else if(data.code === 1){
                    alert('Le mot n\'est pas dans la grille');
                }else{

                    alert('Le mot n\'existe pas dans le dictionnaire');
                }

            })
            .catch((error) => {
                console.error('Error:', error);
            });
        });

        function addWord(word) {
            const wordList = document.getElementById('word-list');
            const ul = document.createElement('ul');
            ul.classList.add('tree')
            const li = document.createElement('li');
            const ul2 = document.createElement('ul');
            const wordDiv = document.createElement('details');
            const summary = document.createElement('summary');
            summary.innerHTML = word;
            wordList.appendChild(ul);
            ul.appendChild(li);
            li.appendChild(wordDiv);
            wordDiv.appendChild(summary);
            
            getDefinition(word, wordDiv);
        }
        function getDefinition(word, wordDiv){
            const url = "../server/api/definition.php";            
            const params = `?word=${word}`;
            fetch(url + params)
            .then((response) => response.json())
            .then((data) => {
                //const detailDiv = wordDiv.querySelector('details');
                //const definition = document.createElement('div');
                //detailDiv.appendChild(definition);
                console.log(data);
                console.log(wordDiv);
                const ul = document.createElement('ul');
                wordDiv.appendChild(ul);
                data.forEach(element => {

                    const li = document.createElement('li');
                    const details = document.createElement('details');
                    const summary = document.createElement('summary');
                    summary.innerHTML = element["title"];
                    ul.appendChild(li);
                    li.appendChild(details);
                    details.appendChild(summary);

                    // Pour chaque définition on affiche les exemples (chaque clé de l'objet)
                    const ul2 = document.createElement('ul');
                    details.appendChild(ul2);
                    for (const [key, value] of Object.entries(element)) {
                        console.log(value);
                        if(key !== "title"){
                            const li2 = document.createElement('li');
                            li2.innerHTML = key;
                            ul2.appendChild(li2);
                            const details2 = document.createElement('details');
                            const summary2 = document.createElement('summary');
                            li2.appendChild(details2);
                            details2.appendChild(summary2);
                            
                            const ul3 = document.createElement('ul');
                            details2.appendChild(ul3);
                            for (const [key2, value2] of Object.entries(value)) {
                                const li3 = document.createElement('li');
                                li3.innerHTML = key2;
                                ul3.appendChild(li3);
                                const details3 = document.createElement('details');
                                const summary3 = document.createElement('summary');
                                li3.appendChild(details3);
                                details3.appendChild(summary3);
                                const ul4 = document.createElement('ul');
                                details3.appendChild(ul4);
                                for (const [key3, value3] of Object.entries(value2)) {
                                    const li4 = document.createElement('li');
                                    li4.innerHTML = value3;
                                    ul4.appendChild(li4);
                                }
                            }

                        }
                    }
                    
                });
                //definition.innerHTML = JSON.stringify(data, null, 2);
                //wordDiv.appendChild(definition);


            

            })
            .catch((error) => {
                console.error('Error:', error);
            });
        }

    </script>
    <style>
        .case{
            width: 50px;
            height: 50px;
            border: 1px solid black;
            display: inline-block;
            text-align: center;
            line-height: 50px;
        }
        .ligne{
            margin-bottom: 10px;
        }


    </style>

</body>
</html>
