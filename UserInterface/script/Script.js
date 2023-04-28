// SMTP Mail pane behaviour class
const SmtpMailController = new (require("./script/SMTPMail.js"))();

// Public Finance pane behaviour class
const PublicFiananceController = new (require("./script/PublicFinance.js"))();

// read setting file
let config;
try {
	config = JSON.parse((require("./script/io.js")).readTextFile("config.txt"));
} catch (e) {
	config = {};
	console.log(e);
}

// dropdowns behaviours
document.querySelectorAll(".dropdown").forEach(e => {
	const toggleBtn = e.querySelector(".button");
	const toggleEle = e.querySelector(".float-div");
	toggleBtn.addEventListener("click", () => {
		toggleEle.classList.toggle("float-div-inactive");
		toggleBtn.classList.toggle("button-active");
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
	const toggleBtn = e.querySelector(".button");
	const toggleEle = e.querySelector(".float-div");
	toggleEle.addEventListener("click", () => {
		toggleBtn.classList.toggle("button-active");
		toggleEle.classList.toggle("float-div-inactive");
	});
});
