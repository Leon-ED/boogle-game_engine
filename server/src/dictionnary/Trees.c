#include "include/Trees.h"


CSTree newTree(Element elem, CSTree firstChild, CSTree nextSibling){ 
    CSTree t = malloc(sizeof(Node));
    if (t == NULL) return NULL;
    t->elem = elem;
    t->firstChild = firstChild;
    t->nextSibling = nextSibling;
    return t;
}

CSTree example() {
    CSTree a = newTree(5, 
                newTree(2, NULL, NULL), 
                newTree(3, NULL, NULL)
            );
    CSTree b =  newTree(8, NULL,  a );
    return b;
}


void printPrefix(CSTree t){
    if (t == NULL) return;
    printf("%d ", t->elem);
    printPrefix(t->firstChild);
    printPrefix(t->nextSibling);
}




//Imprime le contenu d'un Static Tree (dans l'ordre du tableau) 
void printStatic(StaticTree t, unsigned int i) {
    if (i >= t.nNodes) return;
    printf("%d ", t.nodeArray[i].elem);
    printStatic(t, t.nodeArray[i].firstChild);
    printStatic(t, t.nodeArray[i].firstChild + 1);
}



int size(CSTree t) {
    if (t == NULL) return 0;
    return 1 + size(t->firstChild) + size(t->nextSibling);
}

int nSibling(CSTree t) {
    if (t == NULL) return 0;
    return 1 + nSibling(t->nextSibling);
}

int nChildren(CSTree t) {
    if (t == NULL) return 0;
    return 1 + nChildren(t->nextSibling);
}
void exportStaticTreeRec(CSTree t, int *i, ArrayCell *nodeArray)
{
    if (t != NULL)
    {
        nodeArray[*i].elem = t->elem;
        nodeArray[*i].firstChild = *i + 1;
        nodeArray[*i].nSiblings = nSibling(t->firstChild);
        (*i)++;
        exportStaticTreeRec(t->firstChild, i, nodeArray);
        exportStaticTreeRec(t->nextSibling, i, nodeArray);
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




// Renvoie le premier fr`ere de t contenant l’´el´ement e (ou t lui-mˆeme), NULL si aucun n’existe.
CSTree siblingLookup(CSTree t, Element e) {
    if (t == NULL) return NULL;
    if (t->elem == e) return t;
    return siblingLookup(t->nextSibling, e);
}

CSTree sortInsertSibling(CSTree *t, Element e) {
    if (*t == NULL) {
        *t = newTree(e, NULL, NULL);
        return *t;
    }
    if ((*t)->nextSibling == NULL) {
        (*t)->nextSibling = newTree(e, NULL, NULL);
        return *t;
    }
    if ((*t)->nextSibling->elem > e) {
        CSTree tmp = (*t)->nextSibling;
        (*t)->nextSibling = newTree(e, NULL, tmp);
        return *t;
    }
    return sortInsertSibling(&(*t)->nextSibling, e);
}


CSTree sortContinue(CSTree* t, Element e) {
    if (*t == NULL) {
        *t = newTree(e, NULL, NULL);
        return *t;
    }
    if ((*t)->elem > e) {
        CSTree tmp = *t;
        *t = newTree(e, NULL, tmp);
        return *t;
    }
    return sortContinue(&(*t)->nextSibling, e);

}

index siblingLookupStatic(StaticTree t, Element e, index from, index len){
    if (len == 0) return -1;
    if (t.nodeArray[from].elem == e) return from;
    return siblingLookupStatic(t, e, from + 1, len - 1);
}

index siblingDichotomyLookupStatic(StaticTree t, Element e, index from, index len){
    if (len == 0) return -1;
    if (t.nodeArray[from + len/2].elem == e) return from + len/2;
    if (t.nodeArray[from + len/2].elem > e) return siblingDichotomyLookupStatic(t, e, from, len/2);
    return siblingDichotomyLookupStatic(t, e, from + len/2 + 1, len - len/2 - 1);
}
