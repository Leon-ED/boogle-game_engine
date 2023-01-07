#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/grid_build.h"
#include "include/libs.h"
#include <math.h>
#include <time.h>
#include <stdarg.h>

//Create matrix of letters with list of letters in argument
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


/**
 * @brief Test si un mot donné est présent dans la grille
 * @param word : mot à chercher dans la grille
 * @param num_lines : nombre de lignes à avoir dans la grille
 * @param num_columns : nombre de colonnes à avoir dans la grille
 * @param lettres : liste de lettres sous forme de lettre par lettre
 * @return 0 si le mot est présent dans la grille, sinon 1
 * @return Si le mot est présent, on affiche le chemin emprunté (liste des indices des cases utilisées, séparés par des espaces). Aucune sortie si le mot est absent
 *
 *
 * @todo : TOUT
 * **/
int grid_path(char word[], int num_lines, int num_columns, char lettres[]) {
    printf("word : %s\n", word);
    printf("num_lines : %d\n", num_lines);
    printf("num_columns : %d\n", num_columns);
    printf("list_letters : %s\n", lettres);

    //Create matrix of letters
    char **matrix = create_matrix(num_lines, num_columns, &lettres);
    //Print matrix
    for (int i = 0; i < num_lines; i++) {
        for (int j = 0; j < num_columns; j++) {
            printf("%c ", matrix[i][j]);
        }
        printf("\n");
    }


    // List of index already used
    int *index_used = malloc(strlen(word) * sizeof(int));
    // tab of letter correct
    int letter_correct_index[strlen(word)];

    // loop on each letter of the word
    for (int i = 0; i < strlen(word); i++) {
        int index;
        int found = 0;
        // loop on each letter of the matrix
        for (int j = 0; j < num_lines; j++) {
            for (int k = 0; k < num_columns; k++) {
                // if the letter is not the same
                if (word[i] != matrix[j][k]) { continue;}

                // if the letter is already used
                index = num_columns*j+k;
                if (index_used[index] == word[i]) {
                    continue;
                }
                // if there is not a next letter
                if (i == strlen(word)-1) {
                    //printf("no more letter, %d", index);
                    letter_correct_index[i] = index;
                    found += 1;
                    break;
                }
                // search in the 8 directions
                int found_neighbour = 0;
                for (int l = -1; l < 2; l++) {
                    for (int m = -1; m < 2; m++) {
                        if (l == 0 && m == 0) { continue; }
                        if (j+l < 0 || j+l >= num_lines) { continue; }
                        if (k+m < 0 || k+m >= num_columns) { continue; }
                        if (word[i+1] == matrix[j+l][k+m]) {
                            //printf("letter %c found at %d %d\n", word[i+1], j+l, k+m);
                            found += 1;
                            index_used[index] = word[i];
                            found_neighbour = 1;
                            break;
                        }
                    }
                }
                if (found_neighbour == 0) {
                    //printf("letter %c not found\n", word[i+1]);
                    return 1;
                }


                index_used[index] = word[i];

                }

        }
        if (found == 0) {
            printf("letter %c not found\n", word[i]);
            return 1;
        }
        letter_correct_index[i] = index;


    }
    for (int i = 0; i < strlen(word); i++) {
        printf("%d ", letter_correct_index[i]);
    }

    //print letter correct index
    return 0;
}



int main(int argc, char *argv[]) {
    // Si le nombre d'arguments est incorrect
    if (argc-2 < 3) {
        printf("Usage: %s <word> <num_lines> <num_columns> <grille>\n", argv[0]);
        return 1;
    }

    char *word = argv[1];
    int num_lines = atoi(argv[2]);
    int num_columns = atoi(argv[3]);
    char lettres[argc-4];
    for (int i =4; i<argc; i++) {
        if (strcmp(argv[i], "QU") == 0)
            lettres[i-4] = *"~";
        else
        lettres[i-4] = argv[i][0];
    }

    printf("\n[valeur de sortie = %d]\n", grid_path(word, num_lines, num_columns, lettres));

    return 0;
}

