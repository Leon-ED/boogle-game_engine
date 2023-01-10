#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/solve.h"
/**
 * @brief Renvoi les mots valides de la grilel présents dans le dico.lex
 * @bug marche pas (too bad)
 **/


// Create a new node for the linked list
List *list_create(void *data)
{
    List *list = malloc(sizeof(list));
    if (list)
    {
        list->data = data;
        list->next = NULL;
    }
    return list;
}

// Add a new node to the front of the linked list
List *add(List *old, void *data)
{
    List *list = list_create(data);
    if (list)
        list->next = old;
    return list;
}

#define MAX_WORD_SIZE 256

void searchWordInGrid(Grid *grid, int row, int col, char *path, int current_word_len, int MIN_WORD_LEN, List **list_all_words, FILE *dico_lex)
{
    if (current_word_len + 1 >= MIN_WORD_LEN)
        return;

    if (row < 0 || row >= grid->rows || col < 0 || col >= grid->cols)
        return;

    path[current_word_len] = grid->letters[row * grid->cols + col];

    if (current_word_len + 1 >= 2 && dictionnary_lookup(dico_lex, path) == 0)
    {
        char *word = strdup(path);
        if (!word)
            return;
        printf("%s", word);
        printf(" curr word len : %d \n", current_word_len + 1);
        *list_all_words = add(*list_all_words, word);
    }

    for (int i = -1; i <= 1; i++)
    {
        for (int j = -1; j <= 1; j++)
        {
            int x = row + i;
            int y = col + j;
            if (x >= 0 && x < grid->rows && y >= 0 && y < grid->cols)
            {
                searchWordInGrid(grid, x, y, path, current_word_len + 1, MIN_WORD_LEN, list_all_words, dico_lex);
            }
        }
    }
    path[current_word_len] = 0;
}

void findAllWordInGrid(Grid *grid, int start_row, int start_col, List **list_all_words, FILE *dico_lex, int taille_mot)
{
    if (!grid || !dico_lex)
        return;
    char path[MAX_WORD_SIZE] = {0};
    // Recursively check all possible paths starting from (start_row, start_col)
    searchWordInGrid(grid, start_row, start_col, path, 0, taille_mot, list_all_words, dico_lex);
}

/**
 * @bug : MARCHE PAS
 ***/
int main(int argc, char const *argv[])
{
    if (argc < 6)
    {
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
    printf("rows : %d \n", rows);
    int cols = atoi(argv[4]);
    printf("cols : %d \n", cols);
    Grid grid;
    grid.rows = rows;
    grid.cols = cols;
    grid.letters = malloc(rows * cols * sizeof(char));
    char letters[argc - 4];
    for (int i = 5; i < argc; i++)
    {
        if ((i - 1) % 4 == 0 && i != 5)
            printf("\n");

        letters[i - 5] = argv[i][0];
        if (strcmp(argv[i], "QU") == 0)
        {
            letters[i - 5] = '&';
            printf("%c ", letters[i - 5]);
            continue;
        }
        printf("%c ", letters[i - 5]);
        letters[i - 5] = argv[i][0];
    }
    strcpy(grid.letters, letters);

    List *list_all_words = NULL;

    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            findAllWordInGrid(&grid, i, j, &list_all_words, dico_LEX, taille_mot);
        }
    }


    // solve(taille_mot, num_lines, num_columns, lettres, dico_LEX);
}