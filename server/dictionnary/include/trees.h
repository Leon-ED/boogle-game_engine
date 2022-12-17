
#include <stdlib.h>
#include <stdio.h>
#include <limits.h>

#define NONE -1
typedef unsigned int index_;
// Définition du type child Sibling Tree (CSTree)

typedef int Element;

typedef struct node
{
    Element elem;
    struct node *firstChild;
    struct node *nextSibling;
} Node;
typedef Node *CSTree;

typedef struct
{
    Element elem;
    unsigned int firstChild;
    unsigned int nSiblings;
} ArrayCell;

typedef struct
{
    ArrayCell *nodeArray;
    unsigned int nNodes;
} StaticTree;
