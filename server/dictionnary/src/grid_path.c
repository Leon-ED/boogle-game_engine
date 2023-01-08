#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#define MAX_WORD_LENGTH 100

// Définition de la structure de données représentant la grille de caractères
typedef struct
{
    int rows;
    int cols;
    char *letters;
} Grid;




// Fonction de vérification récursive de la présence du mot dans la grille
bool findWordInGrid(Grid *grid, int row, int col, const char *word)
{
    // Si le mot est vide, c'est qu'il a été entièrement trouvé dans la grille
    if (*word == '\0')
        return true;
    // printf("%s ", word);
    // Si la position actuelle est en dehors de la grille, on retourne false
    if (row < 0 || row >= grid->rows || col < 0 || col >= grid->cols)
        return false;

    // Si le caractère de la grille ne correspond pas à celui du mot, on retourne false
    if(grid->letters[row*grid->cols + col] != *word)
        return false;


    // On affiche la position actuelle
    printf("(%d, %d) ", row, col);

    // On marque la position actuelle comme visitée
    char tmp = grid->letters[row*grid->cols + col];
    grid->letters[row*grid->cols + col] = '*';

    // On vérifie récursivement les positions adjacentes et diagonales
    bool found = findWordInGrid(grid, row + 1, col, word + 1) ||
                 findWordInGrid(grid, row - 1, col, word + 1) ||
                 findWordInGrid(grid, row, col + 1, word + 1) ||
                 findWordInGrid(grid, row, col - 1, word + 1) ||
                 findWordInGrid(grid, row + 1, col + 1, word + 1) ||
                 findWordInGrid(grid, row - 1, col - 1, word + 1) ||
                 findWordInGrid(grid, row + 1, col - 1, word + 1) ||
                 findWordInGrid(grid, row - 1, col + 1, word + 1);

    // On remet le caractère de la grille à sa valeur initiale
    grid->letters[row*grid->cols + col] = tmp;

    return found;
}

int main(int argc, char **argv)
{
    // Vérification du nombre d'arguments
    if (argc < 5)
    {
        printf("Usage: %s <mot> <nombre de lignes> <nombre de colonnes> <grille>\n", argv[0]);
        return 1;
    }

    // Récupération des arguments
    char *word = argv[1];
    // replace in word all QU by &
    for (int i = 0; i < strlen(word); i++)
    {
        if (word[i] == 'Q' && word[i + 1] == 'U')
        {
            word[i] = '&';
            // shift the rest of the string
            for (int j = i + 1; j < strlen(word); j++)
            {
                word[j] = word[j + 1];
            }
        }
    }

    int rows = atoi(argv[2]);
    int cols = atoi(argv[3]);
    Grid grid;
    grid.rows = rows;
    grid.cols = cols;
    grid.letters = malloc(rows*cols*sizeof(char));
    char letters[argc - 4];
    for (int i = 4; i < argc; i++)
    {
        letters[i - 4] = argv[i][0];
        printf("%c ", letters[i - 4]);
         if (strcmp(argv[i], "QU") == 0){
            letters[i - 4] = '&';
            continue;
        }
        letters[i - 4] = argv[i][0];
    }
    strcpy(grid.letters, letters);
    printf("\n");
    // Affichage de la grille
    printf("Grille de %d lignes et %d colonnes: ", rows, cols);
    for (size_t i = 0; i < rows*cols; i++)
    {
        printf("%c ", grid.letters[i]);
    }
    printf("\n");

    // strcpy(grid.grid, argv[4]);
        // Recherche du mot dans la grille
    bool found = false;
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < cols; j++)
        {
            if (findWordInGrid(&grid, i, j, word))
            {
                printf("Mot trouvé en commençant par la position (%d, %d)\n", i, j);
                found = true;
                break;
            }
        }
    }
    if (!found)
    {
        printf("Mot non trouvé dans la grille\n");
    }

    return 0;
}
