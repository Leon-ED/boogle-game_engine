#include "include/trees.h"

CSTree newTree(Element elem, CSTree firstChild, CSTree nextSibling)
{
    CSTree t = malloc(sizeof(Node));
    if (t == NULL)
        return NULL;
    t->elem = elem;
    t->firstChild = firstChild;
    t->nextSibling = nextSibling;
    return t;
}

CSTree example()
{
    CSTree a = newTree('A',
                       newTree('B', NULL, NULL),
                       newTree('C', NULL, NULL));
    CSTree b = newTree('D', NULL, a);
    return b;
}

void printPrefix(CSTree t)
{
    if (t == NULL)
        return;
    printf("%c ", t->elem);
    printPrefix(t->firstChild);
    printPrefix(t->nextSibling);
}

// Imprime le contenu d'un Static Tree (dans l'ordre du tableau)
void printStatic(StaticTree t, unsigned int i) {
    i = 0;
    printf("\nindex\t\tElement\t\tFirst Child\t\tsiblings\n");
    while (i < t.nNodes) {
        // printf("%c \n",t.nodeArray[i].elem);
        // printf("i: %d \n",i);
        printf("%d\t\t%c\t\t%d\t\t%d\n", i, t.nodeArray[i].elem, t.nodeArray[i].firstChild, t.nodeArray[i].nSiblings);
        i ++;
    }
}


int size(CSTree t)
{
    if (t == NULL)
        return 0;
    return 1 + size(t->firstChild) + size(t->nextSibling);
}

int nSibling(CSTree t)
{
    if (t == NULL)
        return 0;
    return 1 + nSibling(t->nextSibling);
}

int nChildren(CSTree t)
{
    if (t == NULL)
        return 0;
    return 1 + nChildren(t->firstChild);
}
void exportStaticTreeRec(CSTree t, int *i, ArrayCell *nodeArray)
{
    if (t != NULL)
    {
        nodeArray[*i].elem = t->elem;
        // le 1er fils est après les frères et leurs fils
        if(t->firstChild == NULL)
            nodeArray[*i].firstChild = NONE;
        else
            nodeArray[*i].firstChild = *i + size(t->nextSibling) + 1 ;
        nodeArray[*i].nSiblings = nSibling(t) - 1;
        (*i)++;
        exportStaticTreeRec(t->nextSibling, i, nodeArray);
        exportStaticTreeRec(t->firstChild, i, nodeArray);
    }
}

StaticTree exportStaticTree(CSTree t)
{
    StaticTree st;
    st.nNodes = size(t);
    st.nodeArray = malloc(st.nNodes * sizeof(ArrayCell));

    int i = 0;
    exportStaticTreeRec(t, &i, st.nodeArray);

    return st;
}

ArrayCell cons(Element e, int firstChild, int nSiblings)
{
    ArrayCell c;
    c.elem = e;
    c.firstChild = firstChild;
    c.nSiblings = nSiblings;
    return c;

}

// Renvoie le premier frere de t contenant l’element e (ou t lui-mˆeme), NULL si aucun n’existe.
CSTree siblingLookup(CSTree t, Element e)
{
    if (t == NULL)
        return NULL;
    if (t->elem == e)
        return t;
    return siblingLookup(t->nextSibling, e);
}

CSTree sortInsertSibling(CSTree *t, Element e)
{
    if (*t == NULL)
    {
        *t = newTree(e, NULL, NULL);
        return *t;
    }
    if ((*t)->nextSibling == NULL)
    {
        (*t)->nextSibling = newTree(e, NULL, NULL);
        return *t;
    }
    if ((*t)->nextSibling->elem > e)
    {
        CSTree tmp = (*t)->nextSibling;
        (*t)->nextSibling = newTree(e, NULL, tmp);
        return *t;
    }
    return sortInsertSibling(&(*t)->nextSibling, e);
}

CSTree sortContinue(CSTree *t, Element e)
{
    if (*t == NULL)
    {
        *t = newTree(e, NULL, NULL);
        return *t;
    }
    if ((*t)->elem > e)
    {
        CSTree tmp = *t;
        *t = newTree(e, NULL, tmp);
        return *t;
    }
    return sortContinue(&(*t)->nextSibling, e);
}

index_ siblingLookupStatic(StaticTree t, Element e, index_ from, index_ len)
{
    if (len == 0)
        return -1;
    if (t.nodeArray[from].elem == e)
        return from;
    return siblingLookupStatic(t, e, from + 1, len - 1);
}

index_ siblingDichotomyLookupStatic(StaticTree t, Element e, index_ from, index_ len)
{
    if (len == 0)
        return -1;
    if (t.nodeArray[from + len / 2].elem == e)
        return from + len / 2;
    if (t.nodeArray[from + len / 2].elem > e)
        return siblingDichotomyLookupStatic(t, e, from, len / 2);
    return siblingDichotomyLookupStatic(t, e, from + len / 2 + 1, len - len / 2 - 1);
}
