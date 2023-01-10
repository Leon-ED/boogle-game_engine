#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/grid_path.h"
#include "include/dictionnary_lookup.h"

/**
 * @brief Renvoi les mots valides de la grilel présents dans le dico.lex
 **/

// Définition de la structure de données représentant la grille de caractères
typedef struct {
    int rows;
    int cols;
    char *letters;
} Grid;

void solve(int taille_mot, int num_lines, int num_columns, char *lettres, FILE *dico_lex) {

printf("taille_mot : %d \n", taille_mot);
printf("num_lines : %d \n", num_lines);
printf("num_columns : %d \n", num_columns);
printf("lettres : %s \n", lettres);
printf("dico_lex : %s \n", dico_lex);





}



int main(int argc, char const *argv[])
{
    if (argc < 6) {
        printf("Usage: %s <dico.lex> <taille_mot> <num_lignes> <num_columns>\n", argv[0]);
        return 1;
    }


    FILE *dico_LEX = fopen(argv[1], "rb");
    if (dico_LEX == NULL)
    {
        printf("Erreur : Erreur lors de l'ouverture du fichier, le chemin spécifié (%s) est-il correct ? \n", argv[1]);
        return 0;
    }

    int rows = atoi(argv[2]);
    int cols = atoi(argv[3]);
    Grid grid;
    grid.rows = rows;
    grid.cols = cols;
    grid.letters = malloc(rows*cols*sizeof(char));
    char letters[argc - 4];
    for (int i = 5; i < argc; i++){
        if ((i-1) %4  == 0 && i != 5)
            printf("\n");

        letters[i - 5] = argv[i][0];
        if (strcmp(argv[i], "QU") == 0){
            letters[i - 5] = '&';
            printf("%c ", letters[i - 5]);
            continue;
        }
        printf("%c ", letters[i - 5]);
        letters[i - 5] = argv[i][0];
    }
    strcpy(grid.letters, letters);



    


    //solve(taille_mot, num_lines, num_columns, lettres, dico_LEX);

}