const form = document.getElementById('form-submit');
const nbCol = document.getElementById('nbCol');
const nbRows = document.getElementById('nbRows');
const langue = document.getElementById('langue');

const PROBA_RENVERSEE = 20;
const LETTRES_AMBIGUES = ['M','W']
const TIMER_PH = "%MIN%:%SEC%";
const TIMER_ORIGIN = "03:00";
var temps_fini = false;



function updateTimer(reset = false){
    if(reset) {
        document.getElementById("word").removeAttribute("disabled");
        document.getElementById("word-submit").removeAttribute("disabled");
        document.getElementById('timer-ph').innerHTML = TIMER_ORIGIN;
        return;
    }
    if(temps_fini) return;
    const timer = document.getElementById('timer-ph').innerHTML;
    const min = parseInt(timer.split(':')[0]);
    const sec = parseInt(timer.split(':')[1]);

    const date = new Date();
    date.setMinutes(min);
    date.setSeconds(sec);

    date.setSeconds(date.getSeconds() - 1);
    const newMin = date.getMinutes();
    const newSec = date.getSeconds();
    document.getElementById('timer-ph').innerHTML = TIMER_PH.replace('%MIN%', newMin).replace('%SEC%', newSec);
    if(date.getMinutes() === 0 && date.getSeconds() === 0) {
        document.getElementById("word").setAttribute("disabled", "disabled");
        document.getElementById("word-submit").setAttribute("disabled", "disabled");
        temps_fini = true;
        document.getElementById('timer-ph').innerHTML = "Le temps est expiré !";
        return;
    }

}

// set interval only on first populateGrid call
var interval = null;
function initTimer() {
    if(interval === null) {
        interval = setInterval(updateTimer, 1000);
    }
}

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

    initGridDrag();

}

// Fais un appel au serveur pour récupérer la grille selon les données
form.addEventListener('click', (e) => {
    initTimer();
    e.preventDefault();
    document.getElementById('word-list').innerHTML = "<h1>Mots trouvés</h1>";
    const nbColValue = nbCol.value;
    const nbRowsValue = nbRows.value;
    const langueValue = langue.value;
    //console.log(nbColValue, nbRowsValue, langueValue);
    const url = "../server/api/grille.php";
    temps_fini = false;
    updateTimer(true);
    const params = `?nbCol=${nbColValue}&nbRows=${nbRowsValue}&langue=${langueValue}`;
    document.getElementById('grille-section').classList.remove('hidden');
    fetch(url + params)
        .then((response) => response.json())
        .then((data) => {
            //console.log(data);
            data = data.grille
            populateGrid(data, nbRowsValue, nbColValue);

        })
        .catch((error) => {
            //console.error('Error:', error);
        });
});