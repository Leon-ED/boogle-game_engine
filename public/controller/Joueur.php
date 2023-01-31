<?php

class Joueur
{
    /*
        Objet PHP représentant un joueur de Boogle
    */


    private $idUser;
    private $email;
    private $login;
    private $pseudo;
    private $noStats = false;
    private $noDatas = "<i class='erreur'>Non disponible</i>";


    public function __construct($idUser)
    {
        $this->idUser = $idUser;
        global $conn;
        $sql = "SELECT * FROM utilisateur WHERE idUser = :idUser";
        $stmt = $conn->prepare($sql);
        $stmt->execute(["idUser" => $this->idUser]);
        $result = $stmt->fetch();
        $this->email = $result["email"];
        $this->login = $result["login"];
        $this->pseudo = $result["pseudoUser"];
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
        $msg = round($temps / 60, 1) . " minutes ($temps secondes)";
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
        $msg = round($temps / 60, 1)  . " minutes ($temps secondes)";
        // convert temps to minutes
        return $msg;
    }

    public function echoInFriendList()
    {
        $hash = md5(strtolower(trim($this->email)));
        echo "<div class='friend'>
        <div class='friend-header'>
        <img src='https://www.gravatar.com/avatar/{$hash}?s=200&d=identicon' alt='Profile picture' height='50' />
            <a href='compte.php?{$this->idUser}' ><h2 class='friendName'>{$this->pseudo}</h2></a>
            <i class='fas fa-comment-alt-lines'></i>
        </div>
        </div>";
    }

    public function getParties($nombre)
    {
        global $conn;
        $sql = "SELECT * FROM partie WHERE idPartie IN (SELECT idPartie FROM jouer WHERE idUser = ?) ORDER BY DateFinPartie DESC LIMIT ? ";
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

    public function getStatPartie($id_partie, $userId)
    {
        global $conn;
        $sql = "SELECT * FROM partie p, jouer j WHERE p.idPartie = j.idPartie AND j.idUser = ? AND p.idPartie = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(1, $userId);
        $stmt->bindParam(2, $id_partie);
        try {
            $stmt->execute();
        } catch (PDOException $e) {
            echo $e->getMessage();
        }
        $result = $stmt->fetch();
        return $result;
    }

    public function getListMotsTrouves($id_partie, $idUser)
    {
        global $conn;
        $sql = "SELECT * FROM proposemots WHERE idPartie = ? AND idUser = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(1, $id_partie);
        $stmt->bindParam(2, $idUser);
        try {
            $stmt->execute();
        } catch (PDOException $e) {
            echo $e->getMessage();
        }
        $result = $stmt->fetchAll();
        return $result;
    }

    public function echoFriendsList()
    {
        global $conn;
        $sql = "SELECT * FROM amis WHERE idUser1 = ? OR idUser2 = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(1, $this->idUser);
        $stmt->bindParam(2, $this->idUser);
        try {
            $stmt->execute();
        } catch (PDOException $e) {
            echo $e->getMessage();
        }
        $result = $stmt->fetchAll();
        if (count($result) == 0) {
            echo "<p>Vous n'avez pas encore d'amis</p>";
        } else {
            foreach ($result as $friend) {
                if ($friend["idUser1"] == $this->idUser) {
                    $id = $friend["idUser2"];
                } else {
                    $id = $friend["idUser1"];
                }
                $user = new Joueur($id);
                $user->echoInFriendList();
            }
        }
    }


    public function getProfilePicture($taille = 150)
    {
        $hash = md5(strtolower(trim($this->email)));
        echo "<img src='https://www.gravatar.com/avatar/{$hash}?s=200&d=identicon' alt='Profile picture' height='{$taille}' />";
    }
}
