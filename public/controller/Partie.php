<?php

class Partie
{

    private $idPartie;
    private $listeIdJoueurs = array();
    private $listeMotsTrouves = array();
    private $grille;
    private $nbCol;
    private $nbRows;
    private $langue;
    private $nomDico;


    function setDimensions($nbRows, $nbCol)
    {
        $this->nbRows = $nbRows;
        $this->nbCol = $nbCol;
    }

    function setGrille($grille)
    {
        $this->grille = $grille;
    }

    function addJoueur($idJoueur)
    {
        if (!isset($this->listeIdJoueurs))
            $this->listeIdJoueurs = array();

        if (!in_array($idJoueur, $this->listeIdJoueurs))
            $this->listeIdJoueurs[] = $idJoueur;
    }
    function addMot($mot)
    {
        if (!isset($this->listeMotsTrouves))
            $this->listeMotsTrouves = array();

        if (!in_array($mot, $this->listeMotsTrouves))
            $this->listeMotsTrouves[] = $mot;
    }

    function savePartie($conn)
    {
        $idPartie = md5(uniqid(rand(), true));
        $sql = "INSERT INTO partie(idPartie, NbMotsTrouves, idVainqueur, Grille, NomDicoUtilise, DateDebutPartie, DateFinPartie) VALUES(?, ?, ?, ?, ?, ?, ?)";
        $stmt = $conn->prepare($sql);

        $nbMotsTrouves = count($this->listeMotsTrouves);
        $idVainqueur = $this->listeIdJoueurs[0];
        $grille = json_encode($this->grille);
        $nomDicoUtilise = $this->nomDico;
        $dateDebutPartie = date("Y-m-d H:i:s");
        $dateFinPartie = date("Y-m-d H:i:s");
        $stmt->execute(
            array(
                $idPartie,
                $nbMotsTrouves,
                $idVainqueur,
                $grille,
                $nomDicoUtilise,
                $dateDebutPartie,
                $dateFinPartie
            )
        );


        $sql = "INSERT INTO jouer (idUser,idPartie) VALUES (?, ?)";
        $stmt = $conn->prepare($sql);
        foreach ($this->listeIdJoueurs as $idJoueur) {
          
            $stmt->execute(array($idJoueur, $idPartie));
        }
    }
}
