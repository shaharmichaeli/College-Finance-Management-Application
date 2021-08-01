Information on how to run the client.

0. In order to config the urls for the client to work it is a few steps are needed:
	- Start the server and check the port number which should be 8042.
	- in the index.js file, there are two global variable which control the url prefixes for interacting with the localhost of the server.
	  sessionServerURLPrefix contains the url for the server.
	  sessionCurrentURLPrefix contains the url for the current client website running in the browser.
	  Insert into the proper variable your url address and the rest of the client follows automatically.
	  
1. index.html is the starting point for the website. It is the login page for any of the users in the system (no admin though).

2. Depending on the email entered we are redirected to student.html (player@example.com), worker.html(worker@example.com) or office.html (manager@example.com).

3. student.html:
	In this page we load the information for the specific student that logged in.
	The course content is stubbed and not from the server.
	Name and balance is loaded from server.
	
4. worker.html:
	In this page we load the information for the specific worker that logged in.
	The course content is stubbed and not from the server.
	Name and balance is loaded from server.
	
4. office.html:
	The control panel for the office worker. 
	There are three sections to this page, student controls, ,worker controls and report generation.
	- clicking on "add student"/"add worker" opens a modal to be filled with information to create a new student\worker and add them to the system.
	- clicking on "show students"/"show workers" displays the current items in the systems according to type.
	- clicking on "generate report" redirects to report.html in which we show financial data calculated form the system.
	
IMPORTANT:
	There is a user and an item in the database which have a unique ID that is required for certain methods to work.
	ERASING IT WILL BREAK ITEM GETTING FROM SERVER.
	
	The user is:
		2021b.Shahar.Hilel.Michael:player@example.com | player.jpg | player@example.com | PLAYER | 2021b.Shahar.Hilel.Michael | player
	The item is:
		2021b.Shahar.Hilel.Michael:ae1260fa-244a-4108-8660-e6e15b281f81 | true | manager@example.com | 2021-05-30 09:35:24.682000 |
		{"userId":{"space":"2021b.Shahar.Hilel.Michael","email":"player@example.com"},"role":"PLAYER","username":"player","avatar":"player.jpg","balance":"0"} |
		ae1260fa-244a-4108-8660-e6e15b281f81 | 2021b.Shahar.Hilel.Michael | {"lat":0,"lng":0} | player player | student.
	
	If by accident the item has been deleted it is possible to use and ID of a different PLAYER item. 