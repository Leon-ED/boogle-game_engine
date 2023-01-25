foundWords = [];



// Lorsque l'on clique sur le bouton "Vérifier" 
const word = document.getElementById('word-submit');
word.addEventListener('click', (e) => {
    e.preventDefault();
    const wordValue = document.getElementById('word').value;
    // console.log(wordValue);
    // on vérifie que le mot n'a pas déjà été trouvé
    if (foundWords.includes(wordValue)) {
        alert('Le mot a déjà été trouvé');
        return;
    }
    // Sinon on prépare l'appel à l'API
    const url = "../server/api/verify.php";
    const grille = document.getElementById('grille');
    const lettres = grille.querySelectorAll('.case');
    const nbCol = document.getElementById('nbCol');
    const nbRows = document.getElementById('nbRows');
    // On prépare l'envoi de la grille au serveur
    let grilleArray = [];
    lettres.forEach((lettre) => {
        grilleArray.push(lettre.innerHTML);
    });

    // On envoie l'appel à l'API
    const params = `?word=${wordValue}&grille=${grilleArray}&nbCol=${nbCol.value}&nbRows=${nbRows.value}`;
    fetch(url + params)
        .then((response) => response.json())
        .then((data) => {
            // Code 0 = mot trouvé, code 1 = mot non trouvé mais existe dans le dictionnaire, code 2 = mot non existant
            if (data.code === 0) {
                document.getElementById('word-list').classList.remove('hidden');
                foundWords.push(wordValue);
                addWord(wordValue);
            } else if (data.code === 1) {
                alert('Le mot n\'est pas dans la grille');
            } else {

                alert('Le mot n\'existe pas dans le dictionnaire');
            }

        })
        .catch((error) => {
            console.error('Error:', error);
        });
});

// Ajoute une mot dans la liste des mots trouvés
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

// Récupère la définition du mot grâce à l'API de dictionnaire et l'affiche dans la liste des mots trouvés sous la forme d'un arbre de définitions
function getDefinition(word, wordDiv) {
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
                    if (key !== "title") {
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