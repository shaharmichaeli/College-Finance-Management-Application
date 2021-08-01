sessionServerURLPrefix = sessionStorage.getItem("sessionServerURLPrefix");
sessionCurrentURLPrefix = sessionStorage.getItem("sessionCurrentURLPrefix");
sessionSpace = sessionStorage.getItem("sessionSpace");
sessionEmail = sessionStorage.getItem("sessionEmail");
sessionID = sessionStorage.getItem("sessionID");
const http = new XMLHttpRequest();

//Displays the list in the student.html page inside of ul element with id "studentList".
function displayStudentData() {
    const url = `${sessionServerURLPrefix}/twins/items/${sessionSpace}/${sessionEmail}/${sessionSpace}/${sessionID}`;
    http.open("GET", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.send();
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            let resp = this.response;
            let data = JSON.parse(resp);
            displayStudentDetails(data);
        }
    }
}

function displayStudentDetails(userData) {
    //Get the list element.
    let studentName = document.getElementById("studentName");
    // let studentCourseList = document.getElementById("studentList");
    let studentBalance = document.getElementById("studentBalance");
    let tempName = userData.name;
    let tempBalance = userData.itemAttributes.balance;
    studentName.innerText = `Welcome! ${tempName}`;
    studentBalance.innerText = `Balance: ${tempBalance} ₪`
}


