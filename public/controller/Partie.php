<?php

class Partie
{

    private $idPartie;
    private $listeIdJoueurs = array();
    private $listeMotsTrouves = array();
    private $grille;
    private $dateDebutPartie;
    private $dateFinPartie;
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
    function addMot($mot, $idUser)
    {
        if (!isset($this->listeMotsTrouves))
            $this->listeMotsTrouves = array();
        if (!isset($this->listeMotsTrouves[$idUser]))
            $this->listeMotsTrouves[$idUser] = array();

        if (!in_array($mot, $this->listeMotsTrouves))
            $this->listeMotsTrouves[$idUser][] = $mot;
    }

    function setLangue($langue)
    {
        $this->langue = $langue;
    }

    function setTimeStampDebut($timeStamp)
    {
        $this->dateDebutPartie = $timeStamp;
    }

    function setTimeStampFin($timeStamp)
    {
        $this->dateFinPartie = $timeStamp;
    }

    function savePartie($conn)
    {
        // Aucun mots dans la partie, pas intéressant à sauvegarder
        if ($this->listeMotsTrouves == null || $this->listeMotsTrouves == array())
            return;

        // Car on a pas mit en autoincrement, dans la BDD
        $idPartie = md5(uniqid(rand(), true));

        // On sauvegarde la partie
        $sql = "INSERT INTO partie(idPartie, NbMotsTrouves, idVainqueur, Grille, NomDicoUtilise, DateDebutPartie, DateFinPartie) VALUES(?, ?, ?, ?, ?, ?, ?)";
        $stmt = $conn->prepare($sql);
        $this->idPartie = $idPartie;

        $nbMotsTrouves = count($this->listeMotsTrouves);
        $idVainqueur = $this->listeIdJoueurs[0];
        $grille = json_encode($this->grille);
        $nomDicoUtilise = $this->nomDico;
        $stmt->execute(
            array(
                $idPartie,
                $nbMotsTrouves,
                $idVainqueur,
                $grille,
                $nomDicoUtilise,
                $this->dateDebutPartie,
                $this->dateFinPartie
            )
        );

        // On sauvegarde l'association entre les joueurs et la partie
        $sql = "INSERT INTO jouer (idUser,idPartie) VALUES (?, ?)";
        $stmt = $conn->prepare($sql);
        foreach ($this->listeIdJoueurs as $idJoueur) {

            $stmt->execute(array($idJoueur, $idPartie));
        }

        // On sauvegarde les mots trouvés et leur score par chaque joueur dans la partie
        $sql = "INSERT INTO proposemots (idUser, idPartie, MotsPropose,ScoreMots) VALUES (?, ?, ?, ?)";
        foreach ($this->listeMotsTrouves as $idJoueur => $listeMots) {
            foreach ($listeMots as $mot) {
                $stmt = $conn->prepare($sql);
                $stmt->execute(array($idJoueur, $idPartie, $mot, 0));
            }
        }
    }
}
