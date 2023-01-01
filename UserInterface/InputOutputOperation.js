class TextFileOperation {
	#fileSystem;
	
	constructor (){
		this.#fileSystem = require("fs");
	}

	read(path){
		return this.#fileSystem.readFileSync(path, "utf8");
	}

	write(path, data){
		this.#fileSystem.writeFile(path, data, function (error) {
			throw new Error(error);
		});
	}
}

class SocketOperation {
	#socket;
	#dataLine;

	constructor (certificatePath, keyPath, address, port, macAddress){
		process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
		const fileSystem = require("fs");
		const tls = require("tls");
		const option = {
			key: fileSystem.readFileSync(keyPath),
			cert: fileSystem.readFileSync(certificatePath)
		};
		let socket = tls.connect(port, option, function() {
			const writeData = {
				macAddress: macAddress
			};
			socket.setEncoding("utf8");
			socket.write(`${JSON.stringify(writeData)}\n`);
		});
		let list = [];
		socket.on("data", (data) => {
			list.push(data);
		});
		this.#socket = socket;
		this.#dataLine = list;
	}

	write(data){
		this.#socket.write(`${data}\n`);
	}

	read(){
		return this.#dataLine.pop();
	}
}

module.exports = {
	TextFileOperation: TextFileOperation,
	SocketOperation: SocketOperation
};