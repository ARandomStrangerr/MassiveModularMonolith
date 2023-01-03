class TextFileOperation {
	#fileSystem;

	constructor (){
		this.#fileSystem = require("fs");
	}

	read(path){
		return this.#fileSystem.readFileSync(path, "utf8");
	}

	readBase64(path){
		return this.#fileSystem.readFileSync(path, {encoding: "base64"});
	}

	write(path, data){
		this.#fileSystem.writeFile(path, data, function (error) {
			throw new Error(error);
		});
	}
}

class SocketOperation {
	#socket; // the socket itself
	#dataLine; // a list to store the line that read from the stream

	constructor (certificatePath, keyPath, address, port, macAddress){
		process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
		const fileSystem = require("fs");
		const tls = require("tls");
		const option = {
			key: fileSystem.readFileSync(keyPath),
			cert: fileSystem.readFileSync(certificatePath)
		};
		// declare another variable due to JS prevent to use the private element of the class
		let socket = tls.connect(port, option, function() {
			const writeData = {
				macAddress: macAddress
			};
			socket.setEncoding("utf8");
			socket.write(`${JSON.stringify(writeData)}\n`);
		});
		let list = [];
		// when data arrive , just push it into a list
		socket.on("data", (data) => {
			list.push(data);
		});
		// set the local variables to this class private variables
		this.#socket = socket;
		this.#dataLine = list;
		// while loop prevent exit the constructor when either socket or list is empty
		while (!this.#socket || !this.#dataLine);
	}

	write(data){
		this.#socket.write(`${data}\n`);
	}

	read(){
		// the while loop help to block the return until there is an input
		while (this.#dataLine.length == 0);
		return this.#dataLine.pop();
	}
}

module.exports = {
	TextFileOperation: TextFileOperation,
	SocketOperation: SocketOperation
};
