class TextFileOperation {
	#fileSystem;

	constructor (){
		this.#fileSystem = require("fs");
	}

	read(path){
		return this.#fileSystem.readFileSync(path, "utf8");
	}

	readBuffer(path){
		return this.#fileSystem.readFileSync(path);
	}

	readBase64(path){
		return this.#fileSystem.readFileSync(path, {encoding: "base64"});
	}

	write(path, data){
		this.#fileSystem.writeFile(path, data, function (error) {
			if (error) console.log(error);
		});
	}
}

class SocketOperation {
	#socket; // the socket itself

	constructor (certificatePath, keyPath, address, port, macAddress, onData){
		process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
		const fileSystem = require("fs");
		const tls = require("tls");
		const option = {
			key: fileSystem.readFileSync(keyPath),
			cert: fileSystem.readFileSync(certificatePath)
		};
		// declare another variable due to JS prevent to use the private element of the class
		let socket = tls.connect(port, address, option, function() {
			socket.setEncoding("utf8");
		});
		const writeData = {
			macAddress: macAddress
		};
		socket.write(`${JSON.stringify(writeData)}\n`);
		// when data arrive , just push it into a list
		socket.on("data", onData);
		// set the local variables to this class private variables
		this.#socket = socket;
	}

	write(data){
		this.#socket.write(`${data}\n`);
	}

	close() {
		this.#socket.destroy();
	}
}

class ExcelFileOperation {
	async read(path){
		const excelReader = require("read-excel-file");
		const data = await excelReader(path);
		return data;
	}
}

module.exports = {
	TextFileOperation: TextFileOperation,
	SocketOperation: SocketOperation,
	ExcelFileOperation: ExcelFileOperation
};
