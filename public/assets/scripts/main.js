const form = document.getElementById('form-submit');
const nbCol = document.getElementById('nbCol');
const nbRows = document.getElementById('nbRows');
const langue = document.getElementById('langue');

const PROBA_RENVERSEE = 20;
const LETTRES_AMBIGUES = ['M','W']

// Remplis la grille grâce au JSON renvoyé par le serveur
function populateGrid(json, nbRowsValue, nbColValue) {
    const grille = document.getElementById('grille');
    grille.innerHTML = '';

    for (var i = 0; i < nbRowsValue; i++) {
        const line = document.createElement('div');
        line.classList.add('ligne');
        for (var j = 0; j < nbColValue; j++) {
            var lettre = json[i][j];
            const div = document.createElement('div');
            div.classList.add('case');
            if(PROBA_RENVERSEE > Math.floor(Math.random() * 100) && !LETTRES_AMBIGUES.includes(lettre)) {
                const rotation = Math.floor(Math.random() * 4) * 90;
                div.style.transform = 'rotate(' + rotation + 'deg)';
                div.classList.add('renversee');
            }
            
            const span_lettre = document.createElement('span');
            span_lettre.innerHTML = lettre;
            div.appendChild(span_lettre);
            line.appendChild(div);
        }
        grille.appendChild(line);
    }



}

// Fais un appel au serveur pour récupérer la grille selon les données
form.addEventListener('click', (e) => {
    e.preventDefault();
    document.getElementById('word-list').innerHTML = "<h1>Mots trouvés</h1>";
    const nbColValue = nbCol.value;
    const nbRowsValue = nbRows.value;
    const langueValue = langue.value;
    console.log(nbColValue, nbRowsValue, langueValue);
    const url = "../server/api/grille.php";
    const params = `?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;
    document.getElementById('grille-section').classList.remove('hidden');
    fetch(url + params)
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            data = data.grille
            populateGrid(data, nbRowsValue, nbColValue);

        })
        .catch((error) => {
            console.error('Error:', error);
        });
});