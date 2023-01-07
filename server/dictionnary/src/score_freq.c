#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/score_freq.h"
#include "include/libs.h"



int getScore(FILE* freq_FILE, char *wordList[]){
    



}


int main (int argc, char *argv[])
{
    if(argc <= 2){
        return MISSING_PARAMS;
    }
    FILE *freq_FILE = fopen(argv[1], "r");
    if(freq_FILE == NULL){
        return FILE_NOT_FOUND;
    }

return getScore(freq_FILE, argv+2);




}