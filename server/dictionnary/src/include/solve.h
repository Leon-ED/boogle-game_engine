#include "grid_path.h"
#include "dictionnary_lookup.h"

// Définition de la structure de données représentant la grille de caractères
// Structure for the grid of characters
// Structure for the grid of characters
typedef struct
{
    int rows;
    int cols;
    char *letters;
} Grid;

// Linked list node structure
typedef struct list List;
struct list
{
    List *next;
    void *data;
};
