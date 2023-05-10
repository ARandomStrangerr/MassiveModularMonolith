module.exports = class{
	#spawn;

	constructor(){
		document.querySelector("#start-connection-manager-module-button").addEventListener("click", () => this.#startModule());
		document.querySelector("#stop-connection-manager-module-button").addEventListener("click", () => this.#stopModule());
	}

	#startModule(){
		let params = {
			moduleName: "ConnectionManager",
			hostAddress: document.querySelector("#connection-manager-module-host-address-input").value.trim(),
			hostPort: document.querySelector("#connection-manager-module-host-port-input").value.trim(),
			listenerPort: document.querySelector("#connection-manager-module-port-input").value.trim(),
			keyStorePath: document.querySelector("#connection-manager-module-jks-path-input").value.trim(),
			keyStorePassword: document.querySelector("#connection-manager-module-jks-password-input").value.trim(),
			databaseUrl: "jdbc:postgresql://localhost:5432/authentication?password=hungcom23",
			poolSize: 2, 
			timeout: 5000
		};
		if(params.hostAddress === "") {
			this.#addInvalidLog("Địa chỉ DataStream chưa được điền");
			return;
		}
		if(params.hostPort === ""){
			this.#addInvalidLog("Cổng DataStream chưa được điền");
			return;
		}
		if(params.listernerPort === ""){
			this.#addInvalidLog("Cổng tiếp nhận máy con chưa được điền");
			return;
		}
		if(params.keyStorePath === ""){
			this.#addInvalidLog("Địa chỉ tệp tin TLS cert chưa được điền");
			return;
		}
		if(params.keyStorePassword === ""){ 
			this.#addInvalidLog("Mật khẩu TLS cert chưa được điền");
			return;
		}
		let cp = document.querySelector("#connection-manager-module-classpath-input").value;
		let className = cp.split("/");
		className = className[className.length-1];
		cp = cp.slice(0, cp.length - className.length - 1);
		className = className.split(".")[0];
		let args = ["--enable-preview", "-cp", cp, className, JSON.stringify(params)];
		this.#spawn = require("child_process").spawn("java", args);
		let li = document.querySelector("#connection-manager-page");
		li.classList.add("valid-dot");
		li.classList.remove("invlaid-dot");
		this.#spawn.on("close", code => {
			li.classList.add("invalid-dot");
			li.classList.remove("valid-dot");
		});
		this.#spawn.stdout.setEncoding("utf8");
		this.#spawn.stdout.on("data", data => this.#addValidLog(data));
		this.#spawn.stderr.setEncoding("utf8");
		this.#spawn.stderr.on("data", data => this.#addInvalidLog(data));
	}

	#stopModule(){
		this.#spawn.kill();
	}

	#addValidLog(msg){
		const li = document.createElement("li");
		li.classList.add("dot");
		li.classList.add("valid-dot");
		li.innerText = msg;
		document.querySelector("#connection-manager-log").appendChild(li);
	}

	#addInvalidLog(msg){
		const li = document.createElement("li");
		li.classList.add("dot");
		li.classList.add("invalid-dot");
		li.innerText = msg;
		document.querySelector("#connection-manager-log").appendChild(li);
	}
}
