#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/grid_path.h"
#include "include/dictionnary_lookup.h"

/**
 * @brief Renvoi les mots valides de la grilel présents dans le dico.lex
 **/


char **create_matrix(int num_lines, int num_columns, char **lettres) {
    char **matrix = malloc(num_lines * sizeof(char *));
    for (int i = 0; i < num_lines; i++) {
        matrix[i] = malloc(num_columns * sizeof(char));
    }
    int k = 0;
    for (int i = 0; i < num_lines; i++) {
        for (int j = 0; j < num_columns; j++) {
            matrix[i][j] = lettres[0][k];
            k++;
        }
    }
    return matrix;
}


void solve(int taille_mot, int num_lines, int num_columns, char *lettres, FILE *dico_lex) {

printf("taille_mot : %d \n", taille_mot);
printf("num_lines : %d \n", num_lines);
printf("num_columns : %d \n", num_columns);
printf("lettres : %s \n", lettres);
printf("dico_lex : %s \n", dico_lex);

    char **matrix = create_matrix(num_lines, num_columns, &lettres);
    //Print matrix
    for (int i = 0; i < num_lines; i++) {
        for (int j = 0; j < num_columns; j++) {
            printf("%c ", matrix[i][j]);
        }
        printf("\n");
    }



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

    int taille_mot = atoi(argv[2]);
    int num_lines = atoi(argv[3]);
    int num_columns = atoi(argv[4]);
    char lettres[argc-4];
    for (int i =5; i<argc; i++) {
        if (strcmp(argv[i], "QU") == 0)
            lettres[i-5] = *"~";
        else
        lettres[i-5] = argv[i][0];
    }


    solve(taille_mot, num_lines, num_columns, lettres, dico_LEX);

}