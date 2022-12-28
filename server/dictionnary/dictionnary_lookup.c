/**
 * @brief Cherche la lettre dans le dictionnaire et renvoie la cellule correspondante
 * @authors Léon EDMEE, Denis DELMAS
 **/

#include "include/dictionnary_lookup.h"
#include <string.h>

/**
 * @brief Cherche la lettre dans le dictionnaire et renvoie la cellule correspondante
 *
 *
 * Si la lettre n'est pas présente, renvoie une cellule avec un elem à \0 et un firstChild à -1
 * Si la lettre à chercher est \0, renvoie la cellule avec un elem à \0 et un firstChild à -2
 * */
Cell cell_from_letter(FILE *dico_lex, char letter, int taille_cellule)
{
    Cell cell;
    Cell NOT_FOUND = newCell('\0', -1, -1);
    fread(&cell, taille_cellule, 1, dico_lex);
    // while cell.letter != letter we read the cell's next sibling, if cell is the last sibling or if cell is not found or cell has no child we return NOT_FOUND
    //printf("\t\t L : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);

    while (cell.letter != letter)
    {
        if (cell.nSibling <= 0)
        {
            break;
        }
        //printf("\t\t L : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);
        fread(&cell, taille_cellule, 1, dico_lex);
    }
    // printf("\t\t L : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);
    if (letter == '\0' && cell.letter == '\0')
    {
        return newCell('\0', -2, -2);
    }
    if (cell.letter == letter)
    {
        return cell;
    }
    else
    {
        return NOT_FOUND;
    }
}
/**
 * @brief Cherche le mot dans le dictionnaire
 * @param dico_name : nom du fichier dictionnaire
 * @param word : mot à chercher
 *
 *  @return 0 si le mot est trouvé, 1 si le mot est un préfixe d'un mot du dictionnaire, 2 si le mot n'est pas trouvé
 **/
int dictionnary_lookup(FILE *dico_lex, char word[])
{
#define FOUND 0
#define NOT_FOUND 2
#define FOUND_PREFIX 1

    // Ouvre le fichier, récupère ses infos et se place au début du fichier
    FileHeader header = getFileHeader(dico_lex);
    Cell cell;

    int size_cell = header.cell_size;
    int size_header = header.size;
    //printf("size_cell : %d, size_header : %d\n", size_cell, size_header);
    //printf("nb_cell : %d, nbword : %d\n", header.nb_Cells, header.nb_words);
    fseek(dico_lex, size_header, SEEK_SET);
    // On se place au début du fichier, après le header et on boucle sur chaque lettre du mot à chercher
    // printf("=== MOT A CHERCHER === %s ===\n", word);
    for (size_t i = 0; i < strnlen(word, 99); i++)
    {
        char letter = word[i];
        // printf("%d : On cherche : %c \n", i, letter);
        //  On réupère la cellule correspondant à la lettre dans les frères
        cell = cell_from_letter(dico_lex, letter, size_cell);
        //printf("On a trouve  : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);
        // On a pas trouvé la lettre donc on renvoie NOT_FOUND
        if (cell.nSibling == -1 || cell.first_child == -1 || cell.letter != letter)
        {
            //printf("NOT FOUND\n");
            return NOT_FOUND;
        }
        // On a trouvé la lettre on se place au premier enfant de la cellule pour ensuite chercher la lettre suivante dans ses frères
        fseek(dico_lex, size_header + size_cell * cell.first_child, SEEK_SET);
    }

    // On a parcouru tout le mot, on regarde si la dernière lettre à comme frère \0
    cell = cell_from_letter(dico_lex, '\0', size_cell);
    // Si elle a un frère \0 on renvoie FOUND car le mot est dans le dictionnaire
    if (cell.nSibling == -2 || cell.first_child == -2)
    {
        return FOUND;
        // Sinon ce n'est pas un mot valide mais un prefixe d'un mot du dictionnaire si la lettre a des enfants
    }
    return FOUND_PREFIX;
}

int main(int argc, char **argv)
{
    if (argc < 3)
    {
        printf("Erreur : 2 arguments attendus (%d passés)\n", argc);
        printf("Syntaxe : $ dictionnary_lookup <fichier.lex> <MOT>\n");
        return MISSING_PARAMS;
    }
    FILE *dico_LEX = fopen(argv[1], "rb");
    if (dico_LEX == NULL)
    {
        printf("Erreur : Erreur lors de l'ouverture du fichier, le chemin spécifié (%s) est-il correct ? \n", argv[1]);
        return 0;
    }
    printf("%d", dictionnary_lookup(dico_LEX, argv[2]));

    return EXIT_SUCCESS;
}