#include <stdio.h>
#include <stdlib.h>
#include "include/dictionnary_build.h"
#include <time.h>

/**
 * @brief Insère un mot dans l'arbre dans l'ordre alphabétique
 * @param t Arbre dans lequel insérer le mot
 * @param word Mot à insérer
 * @return void
 **/
void insertWord(CSTree *t, char *word)
{
  //printf("word: %s ", word);
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
 * @brief Insère un \0 à la fin du mot
 * @param t Arbre dans lequel insérer le \0
 * @param word Mot à trouver
 * @return void
 **/
void insertEOW(char *word, CSTree *t)
{
  // Si l'arbre est nul on mets un \0
  if (*t == NULL)
  {
    (*t) = newTree('\0', NULL, NULL);
  }
  // Si on a trouvé une lettre, on continue dans les fils
  else if ((*t)->elem == word[0])
  {
    insertEOW(word + 1, &(*t)->firstChild);
  }
  // Si on a pas trouvé la lettre, on continue dans les frères
  else
  {
    insertEOW(word, &(*t)->nextSibling);
  }
}
void writeStaticTreeRec(StaticTree t, FILE *dico_LEX, unsigned int i)
{
  if (i < t.nNodes)
  {
    fwrite(&t.nodeArray[i], sizeof(ArrayCell), 1, dico_LEX);
    writeStaticTreeRec(t, dico_LEX, i + 1);
  }
}
void writeStaticTree(StaticTree t, FILE *dico_LEX,FileHeader header)
{
  fseek(dico_LEX, 0, SEEK_SET);
  fwrite(&header, sizeof(FileHeader), 1, dico_LEX);
  writeStaticTreeRec(t, dico_LEX, 0);
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
  //printf("nb_words: %d \n", header.nb_words);
  if (dico == NULL || dico_lex == NULL)
  {
    //printf("Erreur : Impossible de trouver l'un des deux fichiers, etes-vous sur de leur emplacement ? !\n");
    exit(1);
  }
  //printf("nb_words: %d \n", header.nb_words);

  char line[256];
  CSTree tree = NULL;
  // On parcourt le fichier dico.txt et on insère les mots dans l'arbre
  while (fgets(line, sizeof(line), dico))
  {
    if (line[strlen(line) - 1] == '\n')
    {
      line[strlen(line) - 1] = '\0';
      //printf("%c \n", line[strlen(line) - 1]);
    }
    insertWord(&tree, line);
    insertEOW(line, &tree);
  }
  // replaceEOL(&tree);

  //printf("\n");
  //printPrefix(tree);
  fclose(dico);
  StaticTree staticTree = exportStaticTree(tree);
  //printStatic(staticTree, 0);
  header.cell_size = sizeof(ArrayCell);
  header.nb_Cells = staticTree.nNodes;
  header.size = sizeof(FileHeader);
  printf("cell_size: %d \n", header.cell_size);
  printf("size: %d \n", header.size);
  printf("nb_Cells: %d \n", header.nb_Cells);
  printf("nb_words: %d \n", header.nb_words);
  writeStaticTree(staticTree, dico_lex, header);
}

int main(int argc, char *argv[])
{
  dictionnary_build("../files/dico.txt", "../files/dumps/dico_lexe.lex");
  return 0;
}