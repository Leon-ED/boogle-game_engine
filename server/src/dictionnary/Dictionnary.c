
#include "include/Libs.h"
#include "include/Dictionnary.h"
#include "include/Trees.h"
#include "string.h"


/** ===== FONCTIONS GENERALES ===== **/

/**
 * @brief Créé une cellule 
 * @param letter : lettre de la cellule
 * @param first_child : index du premier enfant
 * @param nSibling : nombre de cellules frères
 **/
Cell newCell(char letter, int first_child, int nSibling){
    Cell cell;
    cell.letter = letter;
    cell.first_child = first_child;
    cell.nSibling = nSibling;
    return cell;
}


/**
 * @brief      Renvoie le header du fichier
 * @param      export_FILE  Le fichier
 * @return     Le header
 */
FileHeader getFileHeader(FILE *export_FILE){
    FileHeader header;
    fseek(export_FILE, 0, SEEK_SET);
    fread(&header, sizeof(FileHeader), 1, export_FILE);
    return header;
}




/** ===== DICTIONNARY BUILD ===== **/

/**
 * @brief      Construit le dictionnaire
 * @param      input_name   Le nom du fichier d'entrée
 * @param      output_name  Le nom du fichier de sortie
 */
void dictionnary_build(char input_name[], char output_name[])
{
    // On ouvre les fichiers et on teste leur ouverture
    FILE *input_FILE = fopen(input_name, "r");
    FILE *output_FILE = fopen(output_name, "w");
    if (!fopen_test(input_FILE, input_name) || !fopen_test(output_FILE, output_name))
        exit(EXIT_FAILURE);

    // On récupère le nombre de lignes du fichier d'entrée
    int nb_lines = number_of_lines(input_FILE);

}





/** ===== DICTIONNARY LOOKUP ===== **/


/**
 * @brief Cherche la lettre dans le dictionnaire et renvoie la cellule correspondante
 * 
 * 
 * Si la lettre n'est pas présente, renvoie une cellule avec un elem à \0 et un firstChild à -1
 * Si la lettre à chercher est \0, renvoie la cellule avec un elem à \0 et un firstChild à -2
 * */
Cell cell_from_letter(FILE *dico_lex, char letter, int taille_cellule){
    Cell cell;
    Cell NOT_FOUND = newCell('\0', -1, -1);
    fread(&cell, taille_cellule, 1, dico_lex);
    // while cell.letter != letter we read the cell's next sibling, if cell is the last sibling or if cell is not found or cell has no child we return NOT_FOUND
    while(cell.letter != letter ){
        if(cell.nSibling <= 0){
            break;
        }
        printf("\t\t L : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);
        fread(&cell, taille_cellule, 1, dico_lex);
    }
    printf("\t\t L : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);
    if(letter == '\0' && cell.letter == '\0'){
        return newCell('\0', -2, -2);
    }
    if(cell.letter == letter){
        return cell;
    }
    else{
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
int dictionnary_lookup(char dico_name[], char word[])
{
    #define FOUND 0 
    #define NOT_FOUND 2
    #define FOUND_PREFIX 1

    // Ouvre le fichier, récupère ses infos et se place au début du fichier
    FILE *dico_lex = fopen(dico_name, "rb");
    FileHeader header = getFileHeader(dico_lex);
    Cell cell;
    
    int size_cell = header.cell_size;
    int size_header = header.size;

    fseek(dico_lex, size_header, SEEK_SET);
    // On se place au début du fichier, après le header et on boucle sur chaque lettre du mot à chercher
    printf("=== MOT A CHERCHER === %s ===\n", word);
    for (int i = 0; i < strnlen(word, 99); i++)
    {
        char letter = word[i];
        printf("%d : On cherche : %c \n", i, letter);
        // On réupère la cellule correspondant à la lettre dans les frères
        cell = cell_from_letter(dico_lex, letter, size_cell);
        printf("On a trouve  : %c , nbSib : %d, 1stChild : %d\n", cell.letter, cell.nSibling, cell.first_child);

        // On a pas trouvé la lettre donc on renvoie NOT_FOUND
        if(cell.nSibling == -1 || cell.first_child == -1 || cell.letter != letter ){
            return NOT_FOUND;
        }
        // On a trouvé la lettre on se place au premier enfant de la cellule pour ensuite chercher la lettre suivante dans ses frères
        fseek(dico_lex,size_header+size_cell*cell.first_child, SEEK_SET);   
    }


    // On a parcouru tout le mot, on regarde si la dernière lettre à comme frère \0
    cell = cell_from_letter(dico_lex, '\0', size_cell);
    // Si elle a un frère \0 on renvoie FOUND car le mot est dans le dictionnaire 
    if(cell.nSibling == -2 || cell.first_child == -2){
        return FOUND;
    // Sinon ce n'est pas un mot valide mais un prefixe d'un mot du dictionnaire si la lettre a des enfants
    }
    return FOUND_PREFIX;


}




int main(int argc, char *argv[]){
    printf("=== Dictionnaire ===\n");
    int code = dictionnary_lookup("../../files/32.lex", "AALENIENNES");
    printf("Code %d\n", code);
    return 0;
}
