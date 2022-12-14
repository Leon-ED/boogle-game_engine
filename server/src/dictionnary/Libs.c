#include "include/Libs.h"

/**
 * @brief      Compte le nombre de lignes dans un fichier
 * @param      file  Le fichier
 * @return     Le nombre de lignes
 */
int number_of_lines(FILE *file)
{
    int nb_lines = 0;
    char c;
    fseek(file , 0, SEEK_SET);
    while ((c = fgetc(file)) != EOF)
    {
        if (c == '\n')
        {
            nb_lines++;
        }
    }
    return nb_lines;
}

/**
 * @brief Teste la réussite de l'ouverture d'un fichier
 * @param file Le fichier
 * @param filename Le nom du fichier
 * @return 0 si le fichier s'est ouvert correctement, 1 sinon
 * 
 **/
int fopen_test(FILE *file, char *filename)
{
    if (file == NULL)
    {
        printf("ERREUR : Erreur lors de l'ouverture du fichier %s\n", filename);
        return 1;
    }
    return 0;
}