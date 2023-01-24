const inputOutputModule = require("./InputOutputOperation.js");
const textFileOperation = new inputOutputModule.TextFileOperation();
const netInterfaces = require("os").networkInterfaces();
const isWindows = require("os").platform() === "win32";

let macAddr;
for (let key in netInterfaces) {
	let netInterface = netInterfaces[key];
	for (let i of netInterface) {
		if (i.mac!=="00:00:00:00:00:00") {
			macAddr = i.mac;
			break;
		}
	}
	if (macAddr) break;
}

// startup actions
let setting;
try{
	setting = JSON.parse(textFileOperation.read("./config.txt"));
} catch (err) {
	setting = {};
}
// set setting fields
for (let key in setting){
	document.querySelector(`#${key}`).value = setting[key];
}

// function to create a notification
function createNotification(cssClass, message){
	const notificationContainer = document.querySelector("#notification-container");
	const notificationPane = document.createElement("div");
	notificationPane.addEventListener("click", () => {
		notificationContainer.removeChild(notificationPane);
	});
	notificationPane.classList.add("notification");
	notificationPane.classList.add(cssClass);
	notificationPane.innerText = message;
	notificationContainer.appendChild(notificationPane);
}

// add attachment to an email unordered list item
function addAttachmentSMTP(mailDiv, fileObj){
	const attachmentLi = document.createElement("li");
	const editAttachmentButton = document.createElement("a");
	const deleteAttachmentButton = document.createElement("a");
	const fileDiv = document.createElement("div");
	const fileButtonDiv = document.createElement("div");
	const file = document.createElement("span");
	const size = document.createElement("span");
	const attachmentUl = mailDiv.querySelector("ul");
	editAttachmentButton.addEventListener("click", () => {
		const fileInput = document.createElement("input");
		fileInput.type = "file";
		fileInput.onchange = () => {
			file.innerText = fileInput.files[0].name;
			size.innerText = `${fileInput.files[0].size} KB`;
			attachmentLi.file = fileInput.files[0];
		}
		fileInput.click();
	});
	editAttachmentButton.classList.add("button");
	editAttachmentButton.classList.add("edit-button");
	deleteAttachmentButton.innerText = "-";
	deleteAttachmentButton.classList.add("button");
	deleteAttachmentButton.classList.add("red-button");
	deleteAttachmentButton.addEventListener("click", () => {
		attachmentUl.removeChild(attachmentLi);
	});
	if (fileObj){
		file.innerText = fileObj.name;
		size.innerText = `${fileObj.size} KB`
		attachmentLi.file = fileObj;
	} else {
		file.innerText = "Tệp tin";
		size.innerText = "0 KB";
	}
	fileButtonDiv.appendChild(editAttachmentButton);
	fileButtonDiv.appendChild(deleteAttachmentButton);
	fileDiv.appendChild(file);
	fileDiv.appendChild(size);
	fileDiv.appendChild(fileButtonDiv);
	attachmentLi.appendChild(fileDiv);
	attachmentUl.appendChild(attachmentLi);
}

// add email function
function addEmailSMTP(mailName){
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
	addAttachmentButton.innerText = "+";
	addAttachmentButton.classList.add("button");
	addAttachmentButton.classList.add("green-button");
	addAttachmentButton.addEventListener("click", () => {
		addAttachmentSMTP(emailLi);
	});
	buttonDiv.appendChild(editButton);
	buttonDiv.appendChild(addAttachmentButton);
	buttonDiv.appendChild(deleteButton);
	email.innerText = mailName;
	email.classList.add("email");
	itemDiv.appendChild(email);
	itemDiv.appendChild(buttonDiv);
	attachmentUl.classList.add("tree-view");
	emailLi.appendChild(itemDiv);
	emailLi.appendChild(attachmentUl);
	emailList.appendChild(emailLi);
	return emailLi;
}

// action for changing setting inputs
const settingInputs = document.querySelectorAll(".setting-input");
settingInputs.forEach(input => {
	input.addEventListener("blur", () => {
		setting[input.id] = input.value.trim();
		textFileOperation.write("config.txt", JSON.stringify(setting));
	});
});

// action selecting pane
const paneSelectors = document.querySelectorAll(".toggle-pane-button");
let currentDisplayPane;
paneSelectors.forEach(toggleButton => {
	toggleButton.addEventListener("click", () => {
		let togglePane = document.querySelector(`#${toggleButton.id}-pane`);
		if (currentDisplayPane) currentDisplayPane.classList.toggle("float-div-inactive");
		togglePane.classList.toggle("float-div-inactive");
		currentDisplayPane = togglePane;
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

// action for dropdown div click then hide
const hideOnClick = document.querySelectorAll(".hide-on-click");
hideOnClick.forEach(dropdown => {
	const button = dropdown.querySelector(".button");
	const floatDiv = dropdown.querySelector(".float-div");
	floatDiv.addEventListener("click", () => {
		floatDiv.classList.toggle("float-div-inactive");
		button.classList.toggle("button-active");
	});
});

// action for adding single email smtp
const addEmailButton = document.querySelector("#add-email-button");
addEmailButton.addEventListener("click", () => {
	addEmailSMTP("example@mail.com");
});

// action for adding multiple emails from excel
const addEmailExcel = document.querySelector("#add-email-excel");
addEmailExcel.addEventListener("click", () => {
	const fileInput = document.createElement("input");
	const excelFileOperation = new inputOutputModule.ExcelFileOperation();
	fileInput.type = "file";
	fileInput.onchange = () => {
		excelFileOperation.read(fileInput.files[0])
		.then(function(rows){
			let count = 0;
			rows.forEach(row => {
				let emailLi = addEmailSMTP(row[0]);
				for (let i = 1; i < row.length; i++){
					let name;
					if (isWindows) name = row[0].split("\\");
					else name = row[i].split("/");
					name = name[name.length-1];
					let fileObj;
					try {
						fileObj= new File([textFileOperation.readBuffer(row[i])], name);
					} catch (err) {
						createNotification("red-notification", err);
						continue;
					}
					addAttachmentSMTP(emailLi, fileObj);
					count = count + 1;
				}
			});
			createNotification("green-notification", `Thành công nhập vào ${rows.length} email cùng với ${count} tệp tin đính kèm`)
		})
	}
	fileInput.click();
});

// action for sending emails smtp
const sendSMTPMailButton = document.querySelector("#smtp-send-button");
sendSMTPMailButton.addEventListener("click", () => {
	const emailList = document.querySelector("#email-list");
	const subject = document.querySelector("#smtp-mail-subject").value;
	const body = document.querySelector("#smtp-mail-body").value;
	if (subject.trim() == "") {
		createNotification("red-notification", "Tiêu đề của thư bị bỏ trống");
		return;
	}
	if (body.trim() == "") {
		createNotification("red-notification", "Nội dung thư bị bỏ trống");
		return;
	}
	if (setting["smtp-username"] == ""){
		createNotification("red-notification", "Tên hộp thư bị bỏ trống");
		return;
	}
	if (setting["smtp-password"] == ""){
		createNotification("red-notification", "Mật khẩu hộp thư bị bỏ trống");
		return;
	}
	if (setting["smtp-server"] == ""){
		createNotification("red-notification", "Địa chỉ SMTP server bị bỏ trống");
		return;
	}
	if (setting["smtp-port"] == ""){
		createNotification("red-notification", "Cổng SMTP server bị bỏ trống");
		return;
	}
	if (emailList.children.length == 0){
		createNotification("red-notification", "Danh sách hộp thư nhận không có ai");
		return;
	}
	let socketOperation;
	try {
		socketOperation = new inputOutputModule.SocketOperation("certificate.pem", "key.pem", setting["home-server"], setting["home-port"], macAddr);
	} catch (err) {
		console.log(err);
		createNotification("red-notification", "Không kết nối được đến máy chủ");
		return;
	}
	let sendData = {
		job: "SMTPMail",
		host: setting["smtp-server"],
		port: setting["smtp-port"],
		username: setting["smtp-username"],
		password: setting["smtp-password"],
		subject: subject,
		message: body
	};
	setTimeout(() => {
		for (let i = 0; i < emailList.children.length; i++) {
			sendData["attachment"] = [];
			const attachmentList = emailList.children[i].querySelector(".tree-view");
			for (let j = 0; j < attachmentList.children.length; j++) {
				sendData["attachment"].push(
					{
						name: attachmentList.children[j].file.name,
						blob: textFileOperation.readBase64(attachmentList.children[j].file.path)
					}
				);
			}
			sendData["recipient"] = emailList.children[i].querySelector(".email").innerText;
			socketOperation.write(JSON.stringify(sendData));
		}
	},0);
	setTimeout(() => {
		for (let i = 0; i < emailList.children.length; i++) {
			let returnData = JSON.parse(socketOperation.read());
			if (returnData.error) {
				createNotification("red-notification", returnData.error);
			} else if (returnData.update) {
				createNotification("green-notification", returnData.update);
			}
		}
	},8000);
});
