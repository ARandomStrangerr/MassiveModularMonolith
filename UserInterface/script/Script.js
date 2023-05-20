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
	document.querySelector("#notification > .react-element").appendChild(notification);
}
function addValidNotification(msg){
	addNotification("green-notification", msg);
}
function addInvalidNotification(msg){
	addNotification("red-notification", msg);
}

document.querySelectorAll(".file-input").forEach(e => {
	e.readOnly = true;
	e.addEventListener("click", () => {
		let input = document.createElement("input");
		input.type = "file";
		input.onchange = () => e.value = input.files[0].path;
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
	const toggleBtn = e.querySelector(".button");
	const toggleEle = e.querySelector(".float-div");
	toggleEle.addEventListener("click", () => {
		toggleBtn.classList.toggle("button-active");
		toggleEle.classList.toggle("float-div-inactive");
	});
});

// SMTP Mail pane behaviour class
new (require("./script/SMTPMail.js"))(addValidNotification, addInvalidNotification);

// E-Invoive pane behaviour class
new (require("./script/EInvoice.js"))(addValidNotification, addInvalidNotification);
