const inputOutputModule = require("./InputOutputOperation.js");
const textFileOperation = new inputOutputModule.TextFileOperation();

// startup actions
const setting = JSON.parse(textFileOperation.read("./config.txt"));
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

// action for changing setting inputs
const settingInputs = document.querySelectorAll(".setting-input");
settingInputs.forEach(input => {
	input.addEventListener("blur", () => {
		setting[input.id] = input.value.trim();
		textFileOperation.write("config.txt", JSON.stringify(setting));
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
	const emailList = document.querySelector("#email-list");
	const subject = document.querySelector("#smtp-mail-subject").value;
	const body = document.querySelector("#smtp-mail-body").value;
	// if (subject.trim() == "") {
	// 	createNotification("red-notification", "Tiêu đề của thư bị bỏ trống");
	// 	return;
	// }
	// if (body.trim() == "") {
	// 	createNotification("red-notification", "Nội dung thư bị bỏ trống");
	// 	return;
	// }
	// if (setting["smtp-username"] == ""){
	// 	createNotification("red-notification", "Tên hộp thư bị bỏ trống");
	// 	return;
	// }
	// if (setting["smtp-password"] == ""){
	// 	createNotification("red-notification", "Mật khẩu hộp thư bị bỏ trống");
	// 	return;
	// }
	// if (setting["smtp-server"] == ""){
	// 	createNotification("red-notification", "Địa chỉ SMTP server bị bỏ trống");
	// 	return;
	// }
	// if (setting["smtp-port"] == ""){
	// 	createNotification("red-notification", "Cổng SMTP server bị bỏ trống");
	// 	return;
	// }
	if (emailList.children.length == 0){
		createNotification("red-notification", "Danh sách hộp thư nhận không có ai");
		return;
	}
	let socketOperation;
	try {
		socketOperation = new inputOutputModule.SocketOperation("certificate.pem", "key.pem", "127.0.0.1", 9998, "abcdef");
	} catch (err) {
		createNotification("red-notification", "Không kết nối được đến máy chủ");
		return;
	}
	let sendData = {
		job: "SMTPMail",
		host: setting["smtp-server"],
		port: setting["smtp-port"],
		username: setting["smtp-username"],
		password: setting["smtp-password"],
		recipient: "billslim0996@gmail.com",
		subject: subject,
		body: body
	};
	setTimeout(function () {
		socketOperation.write(JSON.stringify(sendData));
		setTimeout(function() {
			 let returnData = JSON.parse(socketOperation.read());
			 if (returnData.error){
			 	createNotification("red-notification", returnData.error);
			 }
		},1000);
	},1000);
});