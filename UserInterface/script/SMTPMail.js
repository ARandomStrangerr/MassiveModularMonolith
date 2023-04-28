module.exports = class {
	#ioModule;
	constructor(){
		document.querySelector("#add-email-button").addEventListener("click", () => this.#addSingleEmail());
		document.querySelector("#add-email-excel").addEventListener("click", () => this.#addBuckFromExcel());
		this.#ioModule = require("./io.js");
	}

	#addSingleEmail(mailAddress){
		let emailSpan = document.createElement("span");
		if (mailAddress) emailSpan.innerText = mailAddress;
		else emailSpan.innerText = "example@mail.com";
		let container = document.createElement("li");
		let emailList = document.createElement("ul");
		let header = document.createElement("div");
		emailList.classList.add("tree-view");
		let removeInstanceButton = document.createElement("a");
		removeInstanceButton.classList.add("button");
		removeInstanceButton.classList.add("red-button");
		removeInstanceButton.innerText = "-";
		removeInstanceButton.addEventListener("click", () => document.querySelector("#email-list").removeChild(container));
		let addAttachmentButton = document.createElement("a");
		addAttachmentButton.classList.add("button");
		addAttachmentButton.classList.add("green-button");
		addAttachmentButton.innerText = "+";
		addAttachmentButton.addEventListener("click", () => this.#addAttachment(emailList));
		let editInstanceButton = document.createElement("a");
		editInstanceButton.classList.add("button");
		editInstanceButton.classList.add("edit-button");
		editInstanceButton.addEventListener("click", () => {
			const editInput = document.createElement("input");
			editInput.value = emailSpan.innerText;
			editInput.addEventListener("blur", () => {
				emailSpan.innerText = editInput.value;
				header.removeChild(editInput);
			});
			emailSpan.innerText = "";
			header.prepend(editInput);
		});
		let buttonWrapper = document.createElement("div");
		buttonWrapper.appendChild(editInstanceButton);
		buttonWrapper.appendChild(addAttachmentButton);
		buttonWrapper.appendChild(removeInstanceButton);
		header.classList.add("header");
		header.appendChild(emailSpan);
		header.appendChild(buttonWrapper);
		container.appendChild(header);
		container.appendChild(emailList);
		document.querySelector("#email-list").appendChild(container);
		return emailList;
	}

	#addAttachment(attachmentList, filePath){
		const container = document.createElement("li");
		const deleteInstanceButton = document.createElement("a");
		deleteInstanceButton.innerText = "-";
		deleteInstanceButton.classList.add("button");
		deleteInstanceButton.classList.add("red-button");
		deleteInstanceButton.addEventListener("click", () => attachmentList.removeChild(container));
		const fileNameSpan = document.createElement("span");
		if (filePath){
			container.path = filePath;
			fileNameSpan.innerText = filePath;
		} else fileNameSpan.innerText = "Trống";
		const editButton = document.createElement("a");
		editButton.classList.add("edit-button");
		editButton.classList.add("button");
		editButton.addEventListener("click", () => {
			const fileInput = document.createElement("input");
			fileInput.type = "file";
			fileInput.addEventListener("change", () => {
				container.path = fileInput.files[0].path;
				fileNameSpan.innerText = fileInput.files[0].name;
			});
			fileInput.click();
		});
		const buttonWrapper = document.createElement("div");
		buttonWrapper.appendChild(editButton);
		buttonWrapper.appendChild(deleteInstanceButton);
		const header = document.createElement("div");
		header.appendChild(fileNameSpan);
		header.appendChild(buttonWrapper); 
		container.appendChild(header);
		attachmentList.appendChild(container);
	}

	#addBuckFromExcel() {
		const fileInput = document.createElement("input");
		fileInput.type = "file";
		fileInput.addEventListener("change", () => {
			this.#ioModule.readExcelFile(this.#ioModule.readBlob(fileInput.files[0].path))
				.then((rows) => {
					console.log(rows);
					for (let row of rows){
						let attachmentList = this.#addSingleEmail(row[0]);
						for (let i = 1; i < row.length; i++) this.#addAttachment(attachmentList, row[i]);
					}
				});
		});
		fileInput.click();
	}
}
