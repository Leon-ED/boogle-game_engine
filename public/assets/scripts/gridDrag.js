var selectedCells = [];
var inSelection = false;
var clearBox = false;


function onCellMouseDown(event) {
    //console.log("mousedown");
    var cell = event.target;
    //console.log(cell.tagName);
    if(cell.tagName == "SPAN"){

        cell = cell.parentElement;
    }


    if(clearBox){
        document.getElementById('word').value = "";
        clearBox = false;
    }
   
    if (cell.classList.contains('selected')) {

        selectedCells = selectedCells.filter(function (selectedCell) {
            return selectedCell !== cell;
        });
        return;
    } else {
        selectedCells.push(cell);
    }
    cell.classList.toggle('selected');
    inSelection = true;

    addWordToInput(cell);
}

function leaveGrid(){
    liste = document.querySelectorAll('.case');
    liste.forEach(function (cell) {
        cell.classList.remove('selected');
    });
    selectedCells = [];
    inSelection = false;
}

function addWordToInput(cell){

    var word = cell.querySelectorAll('span')[0].innerHTML;
    var currentWord = document.getElementById('word').value;
    document.getElementById('word').value = currentWord + word;
}


function onCellMouseUp(event) {
    selectedCells.forEach(function (cell) {
        cell.classList.remove('selected');
    });
    selectedCells = [];
    inSelection = false;
    clearBox = true;
    }

function dragEnter(event) {
    if(!inSelection) return;
    onCellMouseDown(event);
    event.preventDefault();
}




function initGridDrag() {

    var cells = document.querySelectorAll('.case');
    document.getElementById("grille").addEventListener("mouseleave", leaveGrid);
    cells.forEach(function (cell) {
        //console.log(cell);
        cell.addEventListener('mouseenter', dragEnter);
        cell.addEventListener('mousedown', onCellMouseDown);
        cell.addEventListener('mouseup', onCellMouseUp);

    });  
    document.getElementById("grille-section").addEventListener("mouseleave", function(){
        selectedCells.forEach(function (cell) {
            cell.classList.remove('selected');
        });
        selectedCells = [];
        inSelection = false;
    }
    );

}
