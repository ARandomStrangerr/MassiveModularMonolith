module.exports = class {
  #logUnorderedList;
  constructor(logUnorderedList){
    this.#logUnorderedList = logUnorderedList;
		document.querySelector("#start-data-stream-module-button").addEventListener("click", () => this.#startModule());
  }

	#startModule(){ // action starting datastream module
		let javaClassPath = document.querySelector("#data-stream-module-classpath-input").value;
		if (javaClassPath === "") {
			this.#addInvalidLog("Địa điểm tệp tin khởi động chưa được điền");
			return;
		}
		let port = document.querySelector("#data-stream-module-port-input").value;
		if (port === "") {
			this.#addInvalidLog("Cổng hoạt động của chưa được điền");
			return;
		}
		let monitorToolPort = document.querySelector("#data-stream-module-monitor-port-input").value;
		if (monitorToolPort === ""){
			this.#addInvalidLog("Cổng triết xuất thông tin chưa được điền");
			return;
		}
		let jksPath = document.querySelector("#data-stream-module-jks-input").value;
		if (jksPath === ""){
			this.#addInvalidLog("Địa điểm tệp tin bảo mật JKS chưa được điền");
			return;
		}
		let jksPassword = document.querySelector("#data-stream-module-jks-password-input").value;
		if (jksPassword === ""){
			this.#addInvalidLog("Mật khẩu của tệp tin bảo mật JKS chưa được điền");
			return;
		}
		let timeout = document.querySelector("#data-stream-module-timeout-input").value;
		if (timeout === ""){
			this.#addInvalidLog("Thời gian ngắt cho kết nối chưa được uỷ quyền chưa được điền");
			return;
		}
    let childProcess = require("child_process");
    let param = {
      port: port,
      timeout: timeout,
      keyStorePath: jksPath,
      keyStorePassword :jksPassword,
      moduleName: "DataStream",
      monitorToolPort: monitorToolPort
    }
    let temp = javaClassPath.split("/");
    temp = temp[temp.length-1];
    let cp = javaClassPath.slice(0, javaClassPath.length-temp.length-1);
    let moduleName = temp.split(".")[0];
    let cmd = `java --enable-preview -cp ${cp} ${moduleName} '${JSON.stringify(param)}'`;
    childProcess.exec(cmd);

    let socket;
    setTimeout(() => {
      socket = new (require('./io.js')).Socket("127.0.0.1", monitorToolPort);
      socket.setOnDataReceive((data)=>this.#onDataEvent(data)); // f*** this but where does this points to? i don't f***ing know :)
    }, 1000);
  }

  #onDataEvent(data){ // things to do when information arrive
		console.log(data);
    try{
      data = JSON.parse(data);
    } catch (e) {
      console.log(e);
      return;
    }
    let printoutData = `${data.time} ${data.status? "Thành công" : "Lỗi" } ${data.notification}`;
    this.#addValidLog(printoutData);
  }

  #addValidLog(msg){ // add a message to log section with a green dot
    let listItem = document.createElement("li");
    listItem.classList.add("dot");
    listItem.classList.add("valid-dot");
    listItem.innerText = msg;
    this.#logUnorderedList.appendChild(listItem);
  }

  #addInvalidLog(msg){ // add a message to log section with a red dot
    let listItem = document.createElement("li");
    listItem.classList.add("dot");
    listItem.classList.add("invalid-dot");
    listItem.innerText = msg;
    this.#logUnorderedList.appendChild(listItem);
  }

	#addActiveModule(moduleName){ // add an module into active module section with a green dot
		let moduleNameSpan = document.createElement("span");
		moduleNameSpan.innerText = moduleName;
		let disconnectButton = document.createElement("i");
		disconnectButton.classList.add("button");
		disconnectButton.classList.add("button-red");
		let listItem = document.createElement("li");
		listItem.appendChild(moduleNameSpan, disconnectButton);
	}
}
