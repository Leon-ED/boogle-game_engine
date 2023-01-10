#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/grid_path.h"
#include "include/libs.h"

/**
 * @brief Vérifie si une position est dans la grille
 * @param Grid *grid - La structure grille à vérifier
 * @param int i - La position verticale
 * @param int j - La position horizontale
 * @return 1 si la position est dans la grille, 0 sinon
 *
 */
int checkBounds(Grid *grid, int i, int j)
{
    return 1;
}

/**
 * @brief Cherche récursivement si un mot est dans la grille
 * @details Regarde les voisins diagonaux et adjacents de la position donnée jusqu'à avoir trouvé le mot ou si toutes les cases sont épuisée
 * @param Grid *grid - Structure de la grille à chercher
 * @param int row - Ligne de la position initiale à chercher
 * @param int col - Colonne de la position initiale à chercher
 * @param char *word - Mot à chercher
 * @param int *found_positions - Liste des positions déjà trouvées de taille = à strlen(word)
 * @return int 0 si le mot a été trouvé, 1 sinon
 *
 *
 * **/
int grid_path_continue(Grid *grid, int row, int col, const char *word, int *index_founded)
{
    // Si le mot est vide, c'est qu'il a été entièrement trouvé dans la grille
    if (*word == '\0')
        return 1;
    // printf("%s ", word);
    // Si la position actuelle est en dehors de la grille, on retourne 0
    if (row < 0 || row >= grid->rows || col < 0 || col >= grid->cols)
        return 0;

    // Si le caractère de la grille ne correspond pas à celui du mot, on retourne 0
    if (grid->letters[row * grid->cols + col] != *word)
        return 0;

    // On marque la position actuelle comme visitée
    char tmp = grid->letters[row * grid->cols + col];
    grid->letters[row * grid->cols + col] = '*';

    // On vérifie récursivement les positions adjacentes et diagonales
    int found = grid_path_continue(grid, row + 1, col, word + 1, index_founded) ||
                grid_path_continue(grid, row - 1, col, word + 1, index_founded) ||
                grid_path_continue(grid, row, col + 1, word + 1, index_founded) ||
                grid_path_continue(grid, row, col - 1, word + 1, index_founded) ||
                grid_path_continue(grid, row + 1, col + 1, word + 1, index_founded) ||
                grid_path_continue(grid, row - 1, col - 1, word + 1, index_founded) ||
                grid_path_continue(grid, row + 1, col - 1, word + 1, index_founded) ||
                grid_path_continue(grid, row - 1, col + 1, word + 1, index_founded);

    // On remet le caractère de la grille à sa valeur initiale
    grid->letters[row * grid->cols + col] = tmp;

    if (found)
        index_founded[strlen(word) - 1] = grid->cols * row + col;

    return found;
}

/**
 *@brief Change tous les 'QU' par un caractère spécial et décale les caractères suivants
 *@param char *word - Mot à nettoyer
 **/
void cleanWord(char *word)
{
    for (int i = 0; i < strlen(word); i++)
    {
        if (word[i] == 'Q' && word[i + 1] == 'U')
        {
            word[i] = QU;
            for (int j = i + 1; j < strlen(word); j++)
                word[j] = word[j + 1];
        }
    }
}

/**
 *@brief Change tous les 'QU' par un caractère spécial dans la grille
 *@param int argc - Nombre d'arguments
 *@param char **argv - Arguments
 *@param char *letters - Tableau de caractères à remplir
 **/
void cleanGrid(int argc, char **argv, char *letters)
{
    for (int i = 4; i < argc; i++)
    {
        letters[i - 4] = argv[i][0];
        // printf("%c ", letters[i - 4]);
        if (strcmp(argv[i], "QU") == 0)
        {
            letters[i - 4] = '&';
            continue;
        }
        letters[i - 4] = argv[i][0];
    }
}

/**
 * @brief Cherche un mot dans la grille et affiche les positions dans l'ordre
 * @param char *word - Mot à chercher
 * @param Grid *grid - Structure de la grille à chercher
 * @return int 0 si le mot a été trouvé, 1 sinon
 **/
int grid_path(char *word, Grid *grid)
{
    int found_positions[strlen(word)];

    // Recherche du mot dans la grille
    for (int i = 0; i < grid->rows; i++)
    {
        for (int j = 0; j < grid->cols; j++)
        {
            if (grid_path_continue(grid, i, j, word, found_positions))
            {
                // printf("Mot trouvé en commençant par la position (%d, %d)\n", i, j);
                // on a trouvé alors on affiche les positions dans le l'ordre (donc ordre inverse de la liste)
                for (int i = strlen(word) - 1; i >= 0; i--)
                    printf("%d ", found_positions[i]);
                printf("\n");
                return FOUND;
            }
        }
    }

    return NOT_FOUND;
}

/**
 * @brief Affiche la grille
 * @param Grid *grid - Structure de la grille à afficher
 **/
void printGrid(Grid *grid)
{
    printf("Grille de %d lignes et %d colonnes:\n", grid->rows, grid->cols);
    for (size_t i = 0; i < grid->cols; i++)
    {
        for (size_t j = 0; j < grid->rows; j++)
        {
            if (grid->letters[i * grid->cols + j] == QU)
                printf("QU ");
            else
                printf("%c ", grid->letters[i * grid->cols + j]);
        }
        printf("\n");
    }
}

int main(int argc, char **argv)
{
    // Vérification du nombre d'arguments
    if (argc < 5)
    {
        printf("Usage: %s <mot> <nombre de lignes> <nombre de colonnes> <grille>\n", argv[0]);
        return MISSING_PARAMS;
    }

    Grid grid;
    // Récupération des arguments
    grid.rows = atoi(argv[2]);
    grid.cols = atoi(argv[3]);
    grid.size = grid.rows * grid.cols;
    grid.letters = malloc(grid.size * sizeof(char));
    char *word = argv[1];
    // On remplace les QU par un caractère spécial
    cleanWord(word);
    // Vérification de la longueur du mot
    if (strlen(word) > grid.size)
    {
        printf("Le mot ne doit pas dépasser %d caractères\n", grid.size);
        return WRONG_PARAMS;
    }

    // printf("Mot à chercher: %s\n", word);
    char letters[argc - 4];
    // On fait pareil pour la grille

    cleanGrid(argc, argv, letters);
    strcpy(grid.letters, letters); // On met le résultat dans la struct
    // printGrid(&grid);
    int result = grid_path(word, &grid);
    free(grid.letters);
    printf("%d", result);

    
    return result;
}
