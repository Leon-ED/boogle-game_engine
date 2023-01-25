const form = document.getElementById('form-submit');
const nbCol = document.getElementById('nbCol');
const nbRows = document.getElementById('nbRows');
const langue = document.getElementById('langue');

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
            const grille = document.getElementById('grille');
            grille.innerHTML = '';

            for (var i = 0; i < nbRowsValue; i++) {
                const line = document.createElement('div');
                console.log(line);
                line.classList.add('ligne');
                for (var j = 0; j < nbColValue; j++) {
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