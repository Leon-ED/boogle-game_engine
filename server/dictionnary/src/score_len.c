#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include "include/libs.h"

#define MAX_WORD_LENGTH 100



int main(int argc, char *argv[])
{
    // Vérification du nombre d'arguments
    if (argc < 1)
    {
        printf("Usage: %s <MOTS>\n", argv[0]);
        return MISSING_PARAMS;
    }
    int score = 0;

    for (size_t i = 1; i < argc ; i++)
    {
        char *word = argv[i];
        int len = strlen(word);
        score += len;
    }
    printf("%d\n", score);


}