#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int size;
    int nb_words;
    int nb_Cells;
    int cell_size;

} FileHeader;

int number_of_lines(FILE *file);
FileHeader getFileHeader(FILE *export_FILE);
