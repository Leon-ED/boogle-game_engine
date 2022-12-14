#include <stdlib.h>
#include <stdio.h>
#include <limits.h>

// En tête du fichier dictionnaire .lex
// Contient sa taille, le nombre de mots, le nombre de cellules et la taille d'une cellule
typedef struct {
    int size;
    int nb_words;
    int nb_Cells;
    int cell_size;

} FileHeader;


// Cellule du dictionnaire, contient la lettre, la position du premier fils et le nombre de frères
typedef struct {
    char letter;
    int first_child;
    int nSibling;

} Cell;


Cell newCell(char letter, int first_child, int nSibling);
FileHeader getFileHeader(FILE *export_FILE);
int dictionnary_lookup(char dico_name[], char word[]);
void dictionnary_build(char input_name[], char output_name[]);