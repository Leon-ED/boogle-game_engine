#include <stdio.h>
#include <stdlib.h>
#include "include/dictionnary_build.h"

/**
 * @brief Insère un mot dans l'arbre dans l'ordre alphabétique
 * @param t Arbre dans lequel insérer le mot
 * @param word Mot à insérer
 * @return void
 **/
void insertWord(CSTree *t, char *word)
{
  printf("word: %s ", word);
  // Aucun élément trouvé alors on l'insère nous même
  if (*t == NULL)
  {
    *t = newTree(word[0], NULL, NULL);
    if (word[1] != '\0')
      insertWord(&(*t)->firstChild, word + 1);
  }
  // Si c'est la même lettre, on l'insère pas et on continue dans les fils avec la lettre suivante
  else if ((*t)->elem == word[0])
  {
    if (word[1] != '\0')
      insertWord(&(*t)->firstChild, word + 1);
  }
  // Pour insérer dans l'ordre alphabétique
  else if ((*t)->elem > word[0])
  {
    CSTree new = newTree(word[0], NULL, *t);
    *t = new;
    if (word[1] != '\0')
      insertWord(&(*t)->firstChild, word + 1);
  }
  else // On a rien trouvé, on continue dans les frères
  {
    insertWord(&(*t)->nextSibling, word);
  }
}

/**
 * @brief Remplace les \n par des \0 dans l'arbre
 * @param t Arbre dans lequel remplacer les \n par des \0
 * @return void
 */
void replaceEOL(CSTree *t)
{
  if (*t == NULL)
    return;
  if ((*t)->elem == '\n')
    (*t)->elem = '\0';
  replaceEOL(&(*t)->firstChild);
  replaceEOL(&(*t)->nextSibling);
}


/**
 * @brief Construit le dictionnaire à partir du fichier dico.txt
 * @param dico_path Chemin vers le fichier dico.txt où se trouve le dictionnaire
 * @param dico_lex_path Chemin vers le fichier dico_lexe.lex où sera stocké le dictionnaire lexé
 * @return void
 */
void dictionnary_build(char *dico_path, char *dico_lex_path)
{
  FILE *dico = fopen(dico_path, "r");
  FILE *dico_lex = fopen(dico_lex_path, "wb");
  FileHeader header;
  header.nb_words = number_of_lines(dico);
  printf("nb_words: %d \n", header.nb_words);
  if (dico == NULL || dico_lex == NULL)
  {
    printf("Erreur : Impossible de trouver l'un des deux fichiers, etes-vous sur de leur emplacement ? !\n");
    exit(1);
  }
  printf("nb_words: %d \n", header.nb_words);

  char line[256];
  CSTree tree = NULL;
  // On parcourt le fichier dico.txt et on insère les mots dans l'arbre
  while (fgets(line, sizeof(line), dico))
  {
    insertWord(&tree, line);
  }
  replaceEOL(&tree);
  printPrefix(tree);
  fclose(dico);
}

int main(int argc, char *argv[])
{
  dictionnary_build("../files/dico.txt", "../files/dumps/dico_lexe.lex");
  return 0;
}