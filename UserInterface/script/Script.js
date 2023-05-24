// read config file and set the data to field
let config = {};
try {
	let data = ((require("./script/io.js")).readTextFile("config.txt"));
	data = data.split("\n");
	for (let e of data){
		e = e.split("=");
		config[e[0]] = e[1];
		try{
			document.querySelector(`#${e[0]}`).value = e[1];
		} catch(ignore){
			continue;
		}
	}
} catch (err) {
	console.log(err);
}

// write config input to file
document.querySelectorAll(".setting-input").forEach(e => {
	e.onchange = () => {
		config[`${e.id}`] = e.value;
		let writeData = "";
		for (let key in config) {
			if (writeData.legnth !== 0) writeData += "\n";
			writeData += `${key}=${config[key]}`;
		}
		(require("./script/io.js")).writeTextFile("config.txt", writeData);
	}
});

// add notification
function addNotification(style, msg){
	let notification = document.createElement("div");
	notification.classList.add(style);
	notification.innerText = msg;
	document.querySelector("#notification > .trigger-element").classList.add("alert");
	document.querySelector("#notification > .react-element").appendChild(notification);
}
function addValidNotification(msg){
	addNotification("green-notification", msg);
}
function addInvalidNotification(msg){
	addNotification("red-notification", msg);
}

// remove notification on bell click
document.querySelector("#notification > .trigger-element").addEventListener("click", () => document.querySelector("#notification > .trigger-element").classList.remove("alert"))

// file input behaviour
document.querySelectorAll(".file-input").forEach(e => {
	e.readOnly = true;
	e.addEventListener("click", () => {
		let input = document.createElement("input");
		input.type = "file";
		input.onchange = () => e.value = input.files[0].path;
		input.click();
	});
});

// folder input behaviour
document.querySelectorAll(".folder-input").forEach(e => {
	e.readOnly = true;
	e.addEventListener("click", () => {
		let input = document.createElement("input");
		input.type = "directory";
		input.click();
	});
});

// dropdowns behaviours
document.querySelectorAll(".dropdown").forEach(e => {
	const toggleBtn = e.querySelector(".trigger-element");
	const toggleEle = e.querySelector(".react-element");
	toggleBtn.addEventListener("click", () => {
		toggleEle.classList.toggle("float-div-inactive");
		toggleBtn.classList.toggle("button-active");
	});
});

// expand behaviours
document.querySelectorAll(".expand").forEach(e => {
	const triggerElement = e.querySelector(".trigger-element");
	const reactElement = e.querySelector(".react-element");
});

// selection input behaviour
document.querySelectorAll(".selection-input").forEach(e => {
	let triggerElement = e.querySelector(".trigger-element");
	e.querySelector(".react-element").querySelectorAll("div").forEach(e1 => {
		// console.log(e1);
		e1.addEventListener("click", () => triggerElement.innerText = e1.innerText);
	});
});

// selecting pane behaviours
let currentDisplayPane;
document.querySelectorAll(".toggle-pane-button").forEach(e => {
	e.addEventListener("click", () => {
		let togglePane = document.querySelector(`#${e.id}-pane`);
		togglePane.classList.toggle("float-div-inactive");
		if (currentDisplayPane) currentDisplayPane.classList.toggle("float-div-inactive");
		currentDisplayPane = togglePane;
	});
});

// hide on click pane behaviours
document.querySelectorAll(".hide-on-click").forEach(e => {
	const toggleBtn = e.querySelector(".trigger-element");
	const toggleEle = e.querySelector(".react-element");
	toggleEle.addEventListener("click", () => {
		toggleBtn.classList.toggle("button-active");
		toggleEle.classList.toggle("float-div-inactive");
	});
});

// SMTP Mail pane behaviour class
new (require("./script/SMTPMail.js"))();

// E-Invoive pane behaviour class
new (require("./script/EInvoice.js"))();
