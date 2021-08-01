sessionServerURLPrefix = sessionStorage.getItem("sessionServerURLPrefix");
sessionCurrentURLPrefix = sessionStorage.getItem("sessionCurrentURLPrefix");
sessionSpace = sessionStorage.getItem("sessionSpace");
sessionEmail = sessionStorage.getItem("sessionEmail");
sessionID = sessionStorage.getItem("sessionID");
const http = new XMLHttpRequest();

//Displays the list in the student.html page inside of ul element with id "studentList".
function displayWorkerData() {
    console.log(sessionID);
    const url = `${sessionServerURLPrefix}/twins/items/${sessionSpace}/${sessionEmail}/${sessionSpace}/${sessionID}`;
    http.open("GET", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.send();
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let resp = this.response;
            let data = JSON.parse(resp);
            displayWorkerDetails(data);
        }
    }

}

function displayWorkerDetails(userData) {
    //Get the list element.
    let workerName = document.getElementById("workerName");
    // let studentCourseList = document.getElementById("studentList");
    let workerPayment = document.getElementById("workerPayment");
    let tempName = userData.name;
    let tempBalance = userData.itemAttributes.payment;
    workerName.innerText = `Welcome! ${tempName}`;
    workerPayment.innerText = `Payment: ${tempBalance} ₪`;
}


