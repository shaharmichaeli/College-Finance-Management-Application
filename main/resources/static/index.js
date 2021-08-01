//global session params
let sessionServerURLPrefix = "http://localhost:8042";
sessionStorage.setItem("sessionServerURLPrefix", sessionServerURLPrefix);
let sessionCurrentURLPrefix = "http://localhost:63342"
sessionStorage.setItem("sessionCurrentURLPrefix", sessionCurrentURLPrefix);
let sessionSpace = "2021b.Shahar.Hilel.Michael";
sessionStorage.setItem("sessionSpace", sessionSpace);
let sessionEmail = "";
let sessionID = "";
let sessionRole = "";
let sessionUsername = "";
let sessionAvatar = "";
const http = new XMLHttpRequest();
//----------------------------------------- LOGIN SCRIPT -----------------------------------------//

//Check what kind of email was entered - PLAYER or MANAGER
function login() {
    let tempEmail = document.getElementById("email").value;
    const url = `${sessionServerURLPrefix}/twins/users/login/${sessionSpace}/${tempEmail}`;
    http.open("GET", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.send();
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let resp = this.response;
            let userData = JSON.parse(resp);
            console.log("Log from login function");
            console.log(userData);
            switch (userData.role) {
                case "PLAYER":
                    const userEmail = userData.userId.email;
                    console.log("userEmail " + userEmail);
                    userSetUp(tempEmail, userEmail);
                    break;
                case "MANAGER":
                    sessionEmail = tempEmail;
                    sessionStorage.setItem("sessionEmail", sessionEmail);
                    sessionRole = userData.role;
                    sessionStorage.setItem("sessionRole", sessionRole);
                    sessionUsername = userData.username;
                    sessionStorage.setItem("sessionUsername", sessionUsername);
                    sessionAvatar = userData.avatar;
                    sessionStorage.setItem("sessionAvatar", sessionAvatar);
                    window.location.replace(`${sessionCurrentURLPrefix}/Client/office.html`); //Change url here as shown in your port number.
                    break;
                default:
                    break;
            }
        }
    }
}

function userSetUp(tempEmail, userEmail) {
    console.log("userSetUp fired! ");
    const url = `${sessionServerURLPrefix}/twins/items/${sessionSpace}/${tempEmail}`;
    http.open("GET", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.send();
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let resp = this.response;
            let itemData = JSON.parse(resp);
            console.log("Log from userSetUp function");
            console.log(itemData);
            for (const datum of itemData) {
                let checkedEmail = datum.itemAttributes.userId.email;
                console.log(checkedEmail);
                if (userEmail.localeCompare(checkedEmail) == 0) {
                    console.log(datum);
                    sessionEmail = tempEmail;
                    sessionStorage.setItem("sessionEmail", sessionEmail);
                    sessionID = datum.itemId.id;
                    sessionStorage.setItem("sessionID", sessionID);
                    const userType = datum.type;
                    console.log("userType " + userType);
                    userRedirection(userType);
                }
            }
        }
    }
}

//Login and decide user
function userRedirection(userType) {
    console.log("userRedirection Fired!");
    console.log(userType);
    switch (userType) {
        case "student":
            console.log(userType);
            window.location.replace(`${sessionCurrentURLPrefix}/Client/student.html`); //Change url here as shown in your port number.
            break;
        case "worker":
            console.log(userType);
            window.location.replace(`${sessionCurrentURLPrefix}/Client/worker.html`); //Change url here as shown in your port number.
            break;
        default:
            console.log(userType);
            console.log("No such email in system.");
            alert("No such user! Try Again...");
            document.getElementById("email").value = "";
            break;
    }
    ;
}