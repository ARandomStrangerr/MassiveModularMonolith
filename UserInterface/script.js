const inputOutputModule = require("./InputOutputOperation.js");
const textFileOperation = new inputOutputModule.TextFileOperation();

// startup actions
const setting = JSON.parse(textFileOperation.read("./config.txt"));
// set setting fields
for (let key in setting){
	document.querySelector(`#${key}`).value = setting[key];
}

// action for changing setting inputs
const settingInputs = document.querySelectorAll(".setting-input");
settingInputs.forEach(input => {
	input.addEventListener("blur", () => {
		setting[input.id] = input.value;
		textFileOperation.writeTextFile("config.txt", JSON.stringify(setting));
	});
});

// action for dropdown div
const dropdowns = document.querySelectorAll(".dropdown");
dropdowns.forEach(dropdown => {
	const button = dropdown.querySelector(".button");
	const floatDiv = dropdown.querySelector(".float-div");

	button.addEventListener("click", () => {
		floatDiv.classList.toggle("float-div-inactive");
		button.classList.toggle("button-active");
	});
});

// action for adding email smtp
const addEmailButton = document.querySelector("#add-email-button");
addEmailButton.addEventListener("click", () => {
	const emailList = document.querySelector("#email-list");
	const itemDiv = document.createElement("div");
	const buttonDiv = document.createElement("div");
	const emailLi = document.createElement("li");
	const email = document.createElement("span");
	const attachmentUl = document.createElement("ul");
	const deleteButton = document.createElement("a");
	const editButton = document.createElement("a");
	const addAttachmentButton = document.createElement("a");
	deleteButton.innerText = "-";
	deleteButton.classList.add("button");
	deleteButton.classList.add("red-button");
	deleteButton.addEventListener("click", () => {
		emailList.removeChild(emailLi);
	});
	editButton.classList.add("button");
	editButton.classList.add("edit-button");
	editButton.addEventListener("click", () => {
		const textField = document.createElement("input");
		textField.addEventListener("blur", () => {
			email.innerText = textField.value;
			itemDiv.removeChild(textField);
		})
		textField.value = email.innerText;
		email.innerText = "";
		itemDiv.prepend(textField);
	});
	addAttachmentButton.innerText="+";
	addAttachmentButton.classList.add("button");
	addAttachmentButton.classList.add("green-button");
	addAttachmentButton.addEventListener("click", () => {
		const attachmentLi = document.createElement("li");
		const editAttachmentButton = document.createElement("a");
		const deleteAttachmentButton = document.createElement("a");
		const fileDiv = document.createElement("div");
		const fileButtonDiv = document.createElement("div");
		const file = document.createElement("span");
		const size = document.createElement("span");
		editAttachmentButton.addEventListener("click", () => {
			const fileInput = document.createElement("input");
			fileInput.type = "file";
			fileInput.onchange = () => {
				file.innerText = fileInput.files[0].name;
				size.innerText = `${fileInput.files[0].size} KB`;
			}
			fileInput.click();
		});
		editAttachmentButton.classList.add("button");
		editAttachmentButton.classList.add("edit-button");
		deleteAttachmentButton.innerText="-";
		deleteAttachmentButton.classList.add("button");
		deleteAttachmentButton.classList.add("red-button");
		deleteAttachmentButton.addEventListener("click", ()=> {
			attachmentUl.removeChild(attachmentLi);
		});
		file.innerText = "Tệp tin";
		size.innerText = "0 KB";
		fileButtonDiv.appendChild(editAttachmentButton);
		fileButtonDiv.appendChild(deleteAttachmentButton);
		fileDiv.appendChild(file);
		fileDiv.appendChild(size);
		fileDiv.appendChild(fileButtonDiv);
		attachmentLi.appendChild(fileDiv);
		attachmentUl.appendChild(attachmentLi);
	});
	buttonDiv.appendChild(editButton);
	buttonDiv.appendChild(addAttachmentButton);
	buttonDiv.appendChild(deleteButton);
	email.innerText = "example@mail.com";
	itemDiv.appendChild(email);
	itemDiv.appendChild(buttonDiv);
	attachmentUl.classList.add("tree-view");
	emailLi.appendChild(itemDiv);
	emailLi.appendChild(attachmentUl);
	emailList.appendChild(emailLi);
});

// action for sending emails smtp
const sendSMTPMailButton = document.querySelector("#smtp-send-button");
sendSMTPMailButton.addEventListener("click", () => {
	
});