#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include "libs.h"
typedef char Letter;



// Cellule du dictionnaire, contient la lettre, la position du premier fils et le nombre de frères
typedef struct {
    Letter letter;
    int first_child;
    int nSibling;

} Cell;



Cell newCell(char letter, int first_child, int nSibling);
int dictionnary_lookup(FILE *dico_lex, char word[]);
void dictionnary_build(char input_name[], char output_name[]);