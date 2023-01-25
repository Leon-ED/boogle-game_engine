function logout() {
    var oReq = new XMLHttpRequest();
    oReq.onload = function () {

        return this.responseText;
    };
    oReq.open("GET", "../server/api/logout.php?action=logout", false);

    oReq.send();

    window.location.href = "../index.php";

}