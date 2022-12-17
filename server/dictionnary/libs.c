#include "include/libs.h"
#include <string.h>

/**
 * @brief      Compte le nombre de lignes dans un fichier
 * @param      file  Le fichier
 * @return     Le nombre de lignes
 */
int number_of_lines(FILE *file)
{
    int nb_lines = 0;
    char c;
    fseek(file, 0, SEEK_SET);
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
 * @brief      Renvoie le header du fichier
 * @param      export_FILE  Le fichier
 * @return     Le header
 */
FileHeader getFileHeader(FILE *export_FILE)
{
    FileHeader header;
    fseek(export_FILE, 0, SEEK_SET);
    fread(&header, sizeof(FileHeader), 1, export_FILE);
    return header;
}

/**
 * @brief Créé une cellule
 * @param letter : lettre de la cellule
 * @param first_child : index du premier enfant
 * @param nSibling : nombre de cellules frères
 **/
Cell newCell(char letter, int first_child, int nSibling)
{
    Cell cell;
    cell.letter = letter;
    cell.first_child = first_child;
    cell.nSibling = nSibling;
    return cell;
}