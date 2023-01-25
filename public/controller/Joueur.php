<?php

class Joueur
{
    /*
        Objet PHP représentant un joueur de Boogle
    */


    private $idUser;
    private $noStats = false;
    private $noDatas = "<i class='erreur'>Non disponible</i>";


    public function __construct($idUser)
    {
        $this->idUser = $idUser;
    }


    public function nombrePartiesJouees()
    {
        global $conn;
        $sql = "SELECT COUNT(*) FROM jouer WHERE idUser = :idUser";
        $stmt = $conn->prepare($sql);
        $stmt->execute(["idUser" => $this->idUser]);
        $result = $stmt->fetch();
        if ($result[0] == 0)
            $this->noStats = true;
        return $result[0];
    }


    public function partiePlusLongue()
    {
        if ($this->noStats)
            return $this->noDatas;
        global $conn;
        $sql = "SELECT DateFinPartie - DateDebutPartie FROM partie WHERE idPartie IN (SELECT idPartie FROM jouer WHERE idUser = :idUser) ORDER BY DateFinPartie - DateDebutPartie DESC LIMIT 1";
        $stmt = $conn->prepare($sql);
        $stmt->execute(["idUser" => $this->idUser]);
        $result = $stmt->fetch();
        $temps = $result[0];
        $msg = $temps / 60 . " minutes ($temps secondes)";
        return $msg;
    }

    public function partiePlusRapide()
    {
        if ($this->noStats)
            return $this->noDatas;
        global $conn;
        $sql = "SELECT DateFinPartie - DateDebutPartie FROM partie WHERE idPartie IN (SELECT idPartie FROM jouer WHERE idUser = :idUser) ORDER BY DateFinPartie - DateDebutPartie ASC LIMIT 1";
        $stmt = $conn->prepare($sql);
        $stmt->execute(["idUser" => $this->idUser]);
        $result = $stmt->fetch();
        $temps = $result[0];
        $msg = $temps / 60 . " minutes ($temps secondes)";
        // convert temps to minutes
        return $msg;
    }

    public function getParties($nombre)
    {
        global $conn;
        $sql = "SELECT * FROM partie WHERE idPartie IN (SELECT idPartie FROM jouer WHERE idUser = ?) LIMIT ?";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(1, $this->idUser);
        $stmt->bindParam(2, $nombre, PDO::PARAM_INT);
        try {
            $stmt->execute();
        } catch (PDOException $e) {
            echo $e->getMessage();
        }
        while ($result = $stmt->fetch()) {
            $date = date("d/m/Y", strtotime($result["DateDebutPartie"]));
            $heure = date("H:i", strtotime($result["DateDebutPartie"]));
            $temps = date("U", strtotime($result["DateFinPartie"])) - date("U", strtotime($result["DateDebutPartie"]));
            $temps = $temps / 60;
            $temps = round($temps, 2);
            $temps = $temps . " minutes";
            //strip specials chars from grille
            $grille = $result["Grille"];
            $grille = str_replace('"', "", $grille);
            $grille = str_replace("[", "", $grille);
            $grille = str_replace("]", "", $grille);
            $grille = str_replace(",", " ", $grille);
            $dimensions = $result["dimensionsGrille"];


            // $score = $result["ScorePartie"];
            $idPartie = $result["idPartie"];
            echo "<div class='partie'>
                    <p>Date : $date</p>
                    <p>Heure : $heure</p>
                    <p>Temps : $temps</p>
                    <p>Grille : {$grille} | Dimensions : {$dimensions}</p>
                    <a href='partie.php?idPartie=$idPartie'>Voir la partie</a>
                </div>";
        }
    }
}
