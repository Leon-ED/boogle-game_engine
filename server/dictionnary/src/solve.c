#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "include/solve.h"


/**
 * @brief Renvoi les mots valides de la grilel présents dans le dico.lex
 **/


// Définition de la structure de données représentant la grille de caractères
typedef struct
{
    int rows;
    int cols;
    char *letters;
} Grid;


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


void solve(int taille_mot, int rows, int cols, char *lettres, FILE *dico_lex) {

printf("taille_mot : %d \n", taille_mot);
printf("num_lines : %d \n", cols);
printf("num_columns : %d \n", rows);
printf("lettres : %s \n", lettres);
printf("dico_lex : %s \n", dico_lex);

    char **matrix = create_matrix(rows, cols, &lettres);
    //Print matrix
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            printf("%c ", matrix[i][j]);
        }
        printf("\n");
    }

}


void findAllPossibleWordsInGrid(Grid *grid, int row, int col, char *word, int len, FILE *dico_lex) {

    int index = 0;

    // Creer la liste des mots trouvés
    int n = 1000;
    char** mots_trouves = malloc(n * sizeof(char*)); 

    for (int i = 0; i < n; i++) {
    mots_trouves[i] = NULL;
    }
    


    // On ajoute la lettre actuelle au mot
    word[len] = grid->letters[row*grid->cols + col];
    len++;

    if (dictionnary_lookup(dico_lex, word) == 0) {
        printf("Mot trouvé : %s \n", word);
        // get the size of word
        int size = strlen(word);
        // allocate memory for the word
        mots_trouves[index] = malloc(size * sizeof(char));
        strcpy(mots_trouves[index], word);
        index++;
    }

    // On affiche le mot actuel
    printf("%s \n", word);


    // On marque la case actuelle comme visitée
    grid->letters[row*grid->cols + col] = '*';

    // On parcourt les 8 cases adjacentes
    for (int i = row-1; i <= row+1; i++) {
        for (int j = col-1; j <= col+1; j++) {
            // On vérifie que la case est dans la grille
            if (i >= 0 && i < grid->rows && j >= 0 && j < grid->cols) {
                // On vérifie que la case n'a pas déjà été visitée
                if (grid->letters[i*grid->cols + j] != '*') {
                    // On appelle la fonction récursive
                    findAllPossibleWordsInGrid(grid, i, j, word, len, dico_lex);
                }
            }
        }
    }

    // On démarque la case actuelle
    grid->letters[row*grid->cols + col] = word[len-1];


    for (int i = 0; i < n; i++) {
        if (mots_trouves[i] != NULL) {
            printf("Mot trouvé : %s \n", mots_trouves[i]);
        }
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
    int rows = atoi(argv[3]);
    int cols = atoi(argv[4]);
    char lettres[argc-4];

    Grid grid;
    grid.rows = rows;
    grid.cols = cols;
    grid.letters = malloc(rows*cols*sizeof(char));


    for (int i =5; i<argc; i++) {
        if (strcmp(argv[i], "QU") == 0)
            lettres[i-5] = *"~";
        else
        lettres[i-5] = argv[i][0];
    }

    strcpy(grid.letters, lettres);
    printf("\n");
    // Affichage de la grille
    printf("Grille de %d lignes et %d colonnes: ", rows, cols);
    for (size_t i = 0; i < rows*cols; i++)
    {
        printf("%c ", grid.letters[i]);
    }
    printf("\n");

    findAllPossibleWordsInGrid(&grid, rows, cols, lettres, taille_mot, dico_LEX);

    solve(taille_mot, rows, cols, lettres, dico_LEX);

}