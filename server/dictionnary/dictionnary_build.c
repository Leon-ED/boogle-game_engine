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
  if (*t == NULL)
  {
    *t = newTree(word[0], NULL, NULL);
    if (word[1] != '\0')
      insertWord(&(*t)->firstChild, word + 1);
  }
  else if ((*t)->elem == word[0])
  {
    if (word[1] != '\0')
      insertWord(&(*t)->firstChild, word + 1);
  }
  else if ((*t)->elem > word[0])
  {
    // printf("AU DESSUS");
    // printf("word: %c, char %c ", word[0], (*t)->elem);
    CSTree new = newTree(word[0], NULL, *t);
    *t = new;
    if (word[1] != '\0')
      insertWord(&(*t)->firstChild, word + 1);
  }
  else
  {
    insertWord(&(*t)->nextSibling, word);
  }
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
  int i = 0;
  CSTree tree = NULL;
  while (fgets(line, sizeof(line), dico))
  {
    insertWord(&tree, line);
    i++;
  }
  printPrefix(tree);
  fclose(dico);
}





int main(int argc, char *argv[])
{
  dictionnary_build("../files/dico.txt", "../files/dumps/dico_lexe.lex");
  return 0;
}