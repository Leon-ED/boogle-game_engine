
#include "include/dictionnary.h"
#include "include/trees.h"
#include "string.h"



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


    // On récupère le nombre de lignes du fichier d'entrée
    int nb_lines = number_of_lines(input_FILE);

}










// int main(int argc, char *argv[]){
//     return 0;
// }
