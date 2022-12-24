#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/grid_build.h"
#include "include/libs.h"
#include <math.h>
#include <time.h>



/**
 * @brief Créé une grille de lettres aléatoires en fonction d'un fichier de lettres et de leur nombre d'occurence
 * @param file : fichier de lettres et de leur nombre d'occurence
 * @param num_lines : nombre de lignes à avoir dans la grille
 * @param num_columns : nombre de colonnes à avoir dans la grille
 * @return 0 si tout s'est bien passé, sinon une erreur
 * 
 * 
 * @todo : Faire en sorte d'avoir un peu plus de a,e 
 * **/
int grid_build(FILE *file, int num_lines, int num_columns)
{
    // Allocation mémoire
    const int letter_count = number_of_lines(file);
    Letter_occurence *counts = malloc(letter_count * sizeof(letter_count));
    if (counts == NULL)
    {
        return MEMORY_ERROR; 
    }
    int total_count = 0;

    // On lit le fichier ligne par ligne et on récupère les 2 premières caractères et le nombre d'occurence
    // On mets tout ça dans le tableau
    char line[16];
    int i = 0;
    while (fgets(line, sizeof(line), file) != NULL)
    {
        sscanf(line, "%2s %d", counts[i].letter, &counts[i].count);
        i++;
        total_count += counts[i].count;
    }

    fclose(file);

    // Seed pour avoir des nombres aléatoires différents à chaque fois
    srand(time(NULL));

    // Pour chaque ligne
    for (int i = 0; i < num_lines; i++)
    {
        // Pour chaque colonne
        for (int j = 0; j < num_columns; j++)
        {
            // On prends une lettre au pif
            int index = rand() % letter_count;
            Letter_occurence *elem = &counts[index];
            // Ensuite on prends un nombre au hasard entre 0 et le nombre total d'occurence
            int random = rand() % total_count;
            char letter[2];
            while (1)
            {
            // Si le nombre est inférieur ou égal au nombre d'occurence de la lettre, on prends cette lettre
            if(random <= (*elem).count)
            {
                strcpy(letter, (*elem).letter);
                break;
            }
            // Sinon on continue avec la lettre suivante
            else
            {
               index = rand() % letter_count;
                elem = &counts[index];
                random = rand() % total_count;
            }
            }
        

            //printf("%c ", letter[0] + 32); Minuscule : pour tester sur le solveur de grille du prof (marche pas avec qu)
            printf("%s ", letter);
        }
        printf("\n");
    }

    // Free the memory allocated for the array
    free(counts);
    return 0;
}

int main(int argc, char *argv[])
{

    FILE *freq_FILE = NULL;
    int nbLines = 0;
    int nbCol = 0;

    if (argc > 4)
    {
        printf("GRID_BUILD : Trop d'arguments\nSyntaxe : grid_build <fichier> <nbLignes> <nbColonnes> \n");
        return TOO_MANY_PARAMS;
    }
    if (argc < 4)
    {
        printf("GRID_BUILD : Pas assez d'arguments\nSyntaxe : grid_build <fichier> <nbLignes> <nbColonnes> \n");
        return MISSING_PARAMS;
    }

    freq_FILE = fopen(argv[1], "r");
    if (freq_FILE == NULL)
    {
        printf("GRID_BUILD: Erreur lors de l'ouverture du fichier, le chemin spécifié (%s) est-il correct ? \n", argv[1]);
        return FILE_NOT_FOUND;
    }
    grid_build(freq_FILE, atoi(argv[2]), atoi(argv[3]));

    return 0;
}
