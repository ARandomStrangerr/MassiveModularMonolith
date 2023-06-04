module.exports = class {

	constructor(){
		document.querySelector("#upload-invoice-button").addEventListener("click", () => this.#sendInvoice());
		document.querySelector("#download-invoice-button").addEventListener("click", () => this.#downloadInvoice());
	}

	async #sendInvoice(){
		let excelFilePath = document.querySelector("#excel-invoice-input").value;
		if (excelFilePath === "") {
			addInvalidNotification("Chưa có tệp tin excel chứa hóa đơn nào được chọn");
			return;
		}
		let username = document.querySelector("#invoice-username").value.trim();
		if (username === "") {
			addInvalidNotification("Tên người dùng dịch vụ hóa đơn chưa được điền");
			return;
		}
		let password = document.querySelector("#invoice-password").value.trim();
		if (password === "") {
			addInvalidNotification("Mật khẩu dịch vụ hóa đơn chưa được điền");
			return;
		}
		let serverAddress = document.querySelector("#home-server").value.trim();
		if (serverAddress === ""){
			addInvalidNotification("Địa chỉ máy chủ chưa được điền");
			return;
		}
		let serverPort = document.querySelector("#home-port").value.trim();
		if (serverPort === ""){
			addInvalidNotification("Cổng của máy chủ chưa được điền");
			return;
		}
		let sendData = {
			job: "ViettelEInvoice",
			subJob: document.querySelector("#create-draft-invoice").check ? "uploadDraftInvoice" : "uploadInvoice",
			file: (require("./io.js")).readFileBase64(excelFilePath),
			username: username,
			password: password
		};
		let socket = new (require("./io.js")).socket("./certificate.pem", "./key.pem", serverAddress, serverPort);
		socket.write(JSON.stringify(sendData));
		while (true) {
			let data;
			try{
				data = await socket.read();
			} catch (e){
				break;
			}
			data = JSON.parse(data);
			if (data.hasOwnProperty("error")) addInvalidNotification(data.error);
			else if (data.hasOwnProperty("success")) addValidNotification(data.success);
			else if (data.hasOwnProperty("update")) addValidNotification(data.update);
		}
	}

	async #downloadInvoice(){
		let startNum = document.querySelector("#start-invoice-number-input").value.trim();
		if (startNum === ""){
			addInvalidNotification("Số hóa đơn bắt đầu chưa được điền");
			return;
		}
		let endNum = document.querySelector("#end-invoice-number-input").value.trim();
		if (endNum === ""){
			addInvalidNotification("Số hóa đơn kết thúc chưa được điền");
			return;
		}
		let templateCode = document.querySelector("#template-code-input > a").value.trim();
		if (templateCode === ""){
			addInvalidNotification("Ký hiệu hóa đơn chưa được điền");
			return;
		}
		let username = document.querySelector("#invoice-username").value.trim();
		if (username === "") {
			addInvalidNotification("Tên người dùng dịch vụ hóa đơn chưa được điền");
			return;
		}
		let password = document.querySelector("#invoice-password").value.trim();
		if (password === "") {
			addInvalidNotification("Mật khẩu dịch vụ hóa đơn chưa được điền");
			return;
		}
		let serverAddress = document.querySelector("#home-server").value.trim();
		if (serverAddress === ""){
			addInvalidNotification("Địa chỉ máy chủ chưa được điền");
			return;
		}
		let serverPort = document.querySelector("#home-port").value.trim();
		if (serverPort === ""){
			addInvalidNotification("Cổng của máy chủ chưa được điền");
			return;
		}
		let sendData = {
			job: "ViettelEInvoice",
			subJob: "downloadInvoice",
			start: Number(startNum),
			end: Number(endNum),
			invoiceSeries: invoiceSeries,
			templateCode: templateCode,
			username: username,
			password: password
		};
		let ioFile = require("./io.js");
		let socket = new ioFile.socket("./certificate.pem", "./key.pem", serverAddress, serverPort);
		socket.write(JSON.stringify(sendData));
		let separator = process.platform === "win32" ? '\\' : '/';
		while (true) {
			let data;
			try{
				data = await socket.read();
			} catch (e){
				break;
			}
			data = JSON.parse(data);
			if (data.hasOwnProperty("error")) addInvalidNotification(data.error);
			else if (data.hasOwnProperty("success")) addValidNotification(data.success);
			else{
				addValidNotification(`Tải về hóa đơn ${data.fileName}`);
				ioFile.writeBase64(`.${separator}Downloads${separator}${data.fileName}`,data.fileToBytes);
			}
		}
	}
}
