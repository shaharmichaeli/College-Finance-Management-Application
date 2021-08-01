sessionServerURLPrefix = sessionStorage.getItem("sessionServerURLPrefix");
sessionCurrentURLPrefix = sessionStorage.getItem("sessionCurrentURLPrefix");
sessionSpace = sessionStorage.getItem("sessionSpace");
sessionEmail = sessionStorage.getItem("sessionEmail");
sessionRole = sessionStorage.getItem("sessionRole");
sessionUsername = sessionStorage.getItem("sessionUsername");
sessionAvatar = sessionStorage.getItem("sessionAvatar");
sessionID = sessionStorage.getItem("sessionID");
const http = new XMLHttpRequest();


window.onload = function generateReport() {
    //Setup parameters for filling report
    let studentAmount = 0;
    let studentTotalBalance = 0;
    let workerAmount = 0;
    let workerTotalPayment = 0;
    let totalPeople = 0;
    let income = 0;
    let liabilities = 0;
    let revenue = 0;

    //Turn into player first.
    //Create body of create user request.
    let userBody = JSON.stringify({
        "userId": {
            "space": sessionSpace,
            "email": sessionEmail
        },
        "role": "PLAYER", //turn into a player for operations.
        "username": sessionUsername,
        "avatar": sessionAvatar
    });
    //First http request -> turn manager to player.
    url = `${sessionServerURLPrefix}/twins/users/${sessionSpace}/${sessionEmail}`;
    http.open("PUT", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.setRequestHeader('Content-Type', 'application/json');
    http.send(userBody);
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //Invoke operation for generate_student_report.
            //Create body of operation.
            let operationBody = JSON.stringify({
                "operationId": {
                    "space": sessionSpace,
                    "id": ""
                },
                "type": "generate_report",
                "item": {
                    "itemId": {
                        "space": sessionSpace,
                        "id": "ae1260fa-244a-4108-8660-e6e15b281f81" //Default player fix.
                    }
                },
                "createdTimestamp": "",
                "invokedBy": {
                    "userId": {
                        "space": sessionSpace,
                        "email": sessionEmail
                    }
                },
                "operationAttributes": {
                    "key1": ""
                }
            });
            //second http request -> get students report.
            url = `${sessionServerURLPrefix}/twins/operations`;
            http.open("POST", url, true);
            http.setRequestHeader('Accept', 'application/json');
            http.setRequestHeader('Content-Type', 'application/json');
            http.send(operationBody);
            http.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    let resp = this.response;
                    let data = JSON.parse(resp);
                    //Turn back to manager.
                    userBody = JSON.stringify({
                        "userId": {
                            "space": sessionSpace,
                            "email": sessionEmail
                        },
                        "role": "MANAGER", //turn back into manager.
                        "username": sessionUsername,
                        "avatar": sessionAvatar
                    });
                    //Third http request -> turn player to manager.
                    url = `${sessionServerURLPrefix}/twins/users/${sessionSpace}/${sessionEmail}`
                    http.open("PUT", url, true);
                    http.setRequestHeader('Accept', 'application/json');
                    http.setRequestHeader('Content-Type', 'application/json');
                    http.send(userBody);
                    http.onreadystatechange = function () {
                        if (this.readyState == 4 && this.status == 200) {
                            for (const datum of data) {
                                if (datum.type == "student") {
                                    totalPeople++;
                                    studentAmount++;
                                    studentTotalBalance += parseInt(datum.itemAttributes.balance);
                                    income += parseInt(datum.itemAttributes.balance);
                                } else {
                                    workerAmount++;
                                    totalPeople++;
                                    workerTotalPayment += parseInt(datum.itemAttributes.payment);
                                    liabilities += parseInt(datum.itemAttributes.payment);
                                }
                                revenue = income - liabilities;
                            }
                            fillReport(studentAmount, studentTotalBalance,
                                workerAmount,workerTotalPayment, totalPeople,
                                income, liabilities, revenue);
                        }
                    }
                }
            }
        }
    }
}

function fillReport(studentAmount, studentTotalBalance,
                    workerAmount,workerTotalPayment, totalPeople,
                    income, liabilities, revenue) {
    document.getElementById("studentAmount").innerHTML = `Total amount of students in college: ${studentAmount}`;
    document.getElementById("studentTotalBalance").innerHTML = `Raking in an amount of: ${studentTotalBalance} $`;
    document.getElementById("workerAmount").innerHTML = `Total amount of workers in college: ${workerAmount}`;
    document.getElementById("workerTotalPayment").innerHTML = `Costing: ${workerTotalPayment} $`;
    document.getElementById("totalPeople").innerHTML = `Total amount of people:  ${totalPeople}`;
    document.getElementById("income").innerHTML = `Income: ${income}`;
    document.getElementById("liabilities").innerHTML = `Liabilities: ${liabilities}`;
    document.getElementById("revenue").innerHTML = `Revenue: ${revenue}`;
}