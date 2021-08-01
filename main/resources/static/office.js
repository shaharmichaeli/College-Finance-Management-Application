sessionServerURLPrefix = sessionStorage.getItem("sessionServerURLPrefix");
sessionCurrentURLPrefix = sessionStorage.getItem("sessionCurrentURLPrefix");
sessionSpace = sessionStorage.getItem("sessionSpace");
sessionEmail = sessionStorage.getItem("sessionEmail");
sessionRole = sessionStorage.getItem("sessionRole");
sessionUsername = sessionStorage.getItem("sessionUsername");
sessionAvatar = sessionStorage.getItem("sessionAvatar");
sessionID = sessionStorage.getItem("sessionID");

const http = new XMLHttpRequest();

//----------------------- Add Item Modal ---------------------//
function openStudentModal() {
    let studentModal = document.getElementById("addStudentModal");
    studentModal.style.display = "block";
}

function closeStudentModal() {
    let studentModal = document.getElementById("addStudentModal");
    let studentModalClose = document.getElementById("modalExitButtonStudent")
    if (event.target == studentModal || event.target == studentModalClose) {
        studentModal.style.display = "none";
    }
}

function openWorkerModal() {
    let studentModal = document.getElementById("addWorkerModal");
    studentModal.style.display = "block";
}

function closeWorkerModal() {
    let workerModal = document.getElementById("addWorkerModal");
    let workerModalClose = document.getElementById("modalExitButtonWorker")
    if (event.target == workerModal || event.target == workerModalClose) {
        workerModal.style.display = "none";
    }
}

//----------------------- Add Item Functions ---------------------//
function addNewStudent() {
    //Get values form input fields.
    let studentEmail = document.getElementById("inputEmailStudent").value;
    let studentRole = "PLAYER" //It's always a player.
    let studentUsername = document.getElementById("inputUsernameStudent").value;
    let studentAvatar = document.getElementById("inputAvatarStudent").value;
    let studentName = document.getElementById("inputNameStudent").value;
    let studentBalance = document.getElementById("inputBalanceStudent").value;

    //Create body of create user request.
    let userBody = JSON.stringify({
        "email": studentEmail,
        "role": studentRole,
        "username": studentUsername,
        "avatar": studentAvatar
    });

    //create new user in server.
    let url = `${sessionServerURLPrefix}/twins/users`;
    http.open("POST", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.setRequestHeader('Content-Type', 'application/json');
    http.send(userBody);
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //create body of create item request.
            let itemBody = JSON.stringify({
                "itemId": {
                    "space": sessionSpace,
                    "id": ""
                },
                "type": "student",
                "name": studentName,
                "active": true,
                "createdTimestamp": "",
                "createdBy": {
                    "userId": {
                        "space": sessionSpace,
                        "email": sessionEmail
                    }
                },
                "location": {
                    "lat": 0,
                    "lng": 0
                },
                "itemAttributes": {
                    "userId": {
                        "space": sessionSpace,
                        "email": studentEmail
                    },
                    "role": studentRole,
                    "username": studentUsername,
                    "avatar": studentAvatar,
                    "balance": studentBalance
                }
            });

            //create new user in database.
            url = `${sessionServerURLPrefix}/twins/items/${sessionSpace}/${sessionEmail}`;
            http.open("POST", url, true);
            http.setRequestHeader('Accept', 'application/json');
            http.setRequestHeader('Content-Type', 'application/json');
            http.send(itemBody);
            http.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {;
                    location.reload();
                }
            }
        }
    }
}

function addNewWorker() {
    //Get values form input fields.
    let workerEmail = document.getElementById("inputEmailWorker").value;
    let workerRole = "PLAYER" //It's always a player.
    let workerUsername = document.getElementById("inputUsernameWorker").value;
    let workerAvatar = document.getElementById("inputAvatarWorker").value;
    let workerName = document.getElementById("inputNameWorker").value;
    let workerPayment = document.getElementById("inputPaymentWorker").value;

    //Create body of create user request.
    let userBody = JSON.stringify({
        "email": workerEmail,
        "role": workerRole,
        "username": workerUsername,
        "avatar": workerAvatar
    });

    //create new user in server.
    let url = `${sessionServerURLPrefix}/twins/users`;
    http.open("POST", url, true);
    http.setRequestHeader('Accept', 'application/json');
    http.setRequestHeader('Content-Type', 'application/json');
    http.send(userBody);
    http.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            //create body of create item request.
            let itemBody = JSON.stringify({
                "itemId": {
                    "space": sessionSpace,
                    "id": ""
                },
                "type": "worker",
                "name": workerName,
                "active": true,
                "createdTimestamp": "",
                "createdBy": {
                    "userId": {
                        "space": sessionSpace,
                        "email": sessionEmail
                    }
                },
                "location": {
                    "lat": 0,
                    "lng": 0
                },
                "itemAttributes": {
                    "userId": {
                        "space": sessionSpace,
                        "email": workerEmail
                    },
                    "role": workerRole,
                    "username": workerUsername,
                    "avatar": workerAvatar,
                    "payment": workerPayment
                }
            });

            //create new user in server.
            url = `${sessionServerURLPrefix}/twins/items/${sessionSpace}/${sessionEmail}`;
            http.open("POST", url, true);
            http.setRequestHeader('Accept', 'application/json');
            http.setRequestHeader('Content-Type', 'application/json');
            http.send(itemBody);
            http.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    location.reload();
                }
            }
        }
    }
}

function generateStudentList() {
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
            //Create body of operation.
            let operationBody = JSON.stringify({
                "operationId": {
                    "space": sessionSpace,
                    "id": ""
                },
                "type": "get_student_information", //Invoke operation for get_student_information
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
                                createStudentListItem(datum);
                            }
                        }
                    }
                }
            }
        }
    }
}

function createStudentListItem(data) {
    //create html elements.
    let studentList = document.getElementById("studentList");
    let studentInnerList = document.createElement("ul");
    let studentAvatarItem = document.createElement("li");
    let studentEmailItem = document.createElement("li");
    let studentUsernameItem = document.createElement("li");
    let studentBalanceItem = document.createElement("li");
    let studentIDItem = document.createElement("li");
    //add class name to elements for styling.
    studentInnerList.classList.add("innerList");
    studentInnerList.onclick = openSpecificStudentModal;
    studentAvatarItem.classList.add("listItem");
    studentEmailItem.classList.add("listItem");
    studentUsernameItem.classList.add("listItem");
    studentBalanceItem.classList.add("listItem");
    studentIDItem.hidden = true;
    //inject data into elements.
    studentAvatarItem.innerText = data.itemAttributes.avatar;
    studentEmailItem.innerText = data.itemAttributes.userId.email;
    studentUsernameItem.innerText = data.name;
    studentBalanceItem.innerText = data.itemAttributes.balance
    studentIDItem.innerText = data.id;

    studentInnerList.append(studentIDItem, studentAvatarItem, studentUsernameItem, studentEmailItem, studentBalanceItem);
    studentList.append(studentInnerList);
}

function generateWorkerList() {
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
                "type": "get_worker_information",
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
                                createWorkerListItem(datum);
                            }
                        }
                    }
                }
            }
        }
    }
}

function createWorkerListItem(data) {
    //create html elements.
    let workerList = document.getElementById("workerList");
    let workerInnerList = document.createElement("ul");
    let workerAvatarItem = document.createElement("li");
    let workerEmailItem = document.createElement("li");
    let workerNameItem = document.createElement("li");
    let workerPaymentItem = document.createElement("li");
    let workerIDItem = document.createElement("li");
    //add class name to elements for styling.
    workerInnerList.classList.add("innerList");
    workerInnerList.onclick = openSpecificWorkerModal;
    workerAvatarItem.classList.add("listItem");
    workerEmailItem.classList.add("listItem");
    workerNameItem.classList.add("listItem");
    workerPaymentItem.classList.add("listItem");
    workerIDItem.hidden = true;
    //inject data into elements.
    workerAvatarItem.innerText = data.itemAttributes.avatar;
    workerEmailItem.innerText = data.itemAttributes.userId.email;
    workerNameItem.innerText = data.name;
    workerPaymentItem.innerText = data.itemAttributes.payment;
    workerIDItem.innerText = data.id;
    //append elements to list.
    workerInnerList.append(workerIDItem, workerAvatarItem, workerNameItem, workerEmailItem, workerPaymentItem);
    workerList.append(workerInnerList);
}

function openSpecificStudentModal() {
    //get ID of specific student.
    const parentElement = event.target.parentElement;
    const specificID = parentElement.firstElementChild;
    const specificAvatar = specificID.nextElementSibling;
    const specificName = specificAvatar.nextElementSibling;
    const specificEmail = specificName.nextElementSibling;
    const specificBalance = specificEmail.nextElementSibling;

    let specificNamePara = document.getElementById("specificStudentName");
    specificNamePara.innerText = specificName.innerHTML;
    let specificEmailPara = document.getElementById("specificStudentEmail");
    specificEmailPara.innerText = specificEmail.innerHTML;
    let specificBalancePara = document.getElementById("specificStudentBalance");
    specificBalancePara.innerText = specificBalance.innerHTML;

    let specificCanvas = document.getElementById("myCanvas");
    let ctx = specificCanvas.getContext("2d");
    ctx.font="30px Arial";
    ctx.fillText(specificAvatar.innerHTML,10,50);

    let studentModal = document.getElementById("viewDetailsModalStudent");
    studentModal.style.display = "block"
}

function closeSpecificStudentModal() {
    let studentModal = document.getElementById("viewDetailsModalStudent");
    studentModal.style.display = "none"
}

function openSpecificWorkerModal() {
    //get ID of specific student.
    const parentElement = event.target.parentElement;
    const specificID = parentElement.firstElementChild;
    const specificAvatar = specificID.nextElementSibling;
    const specificName = specificAvatar.nextElementSibling;
    const specificEmail = specificName.nextElementSibling;
    const specificBalance = specificEmail.nextElementSibling;

    let specificNamePara = document.getElementById("specificWorkerName");
    specificNamePara.innerText = specificName.innerHTML;
    let specificEmailPara = document.getElementById("specificWorkerEmail");
    specificEmailPara.innerText = specificEmail.innerHTML;
    let specificBalancePara = document.getElementById("specificWorkerBalance");
    specificBalancePara.innerText = specificBalance.innerHTML;

    let specificCanvas = document.getElementById("myCanvas2");
    let ctx = specificCanvas.getContext("2d");
    ctx.font="30px Arial";
    ctx.fillText(specificAvatar.innerHTML,10,50);

    let studentModal = document.getElementById("viewDetailsModalWorker");
    studentModal.style.display = "block"
}

function closeSpecificWorkerModal() {
    let workerModal = document.getElementById("viewDetailsModalWorker");
    workerModal.style.display = "none"
}

function generateReport() {
    //Forward to report page.
    window.location.href = `${sessionCurrentURLPrefix}/Client/report.html`;
}
