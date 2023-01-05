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
			if (error) console.log(error);
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
			socket.setEncoding("utf8");
		});
		const writeData = {
			macAddress: macAddress
		};
		socket.write(`${JSON.stringify(writeData)}\n`);
		let list = [];
		// when data arrive , just push it into a list
		socket.on("data", (data) => {
			console.log(data);
			list.push(data);
		});
		// set the local variables to this class private variables
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

class ExcelFileOperation {
	read(path){
		const xlsx = require("xlsx");
		const data = [];
		const file = xlsx .readFile(path);
		const sheet = xlsx.utils.sheet_to_json(file.Sheets[file.SheetNames[0]]);
		temp.forEach((res) => {
			data.push(res);
		});
		return data;
	}
}

module.exports = {
	TextFileOperation: TextFileOperation,
	SocketOperation: SocketOperation,
	ExcelFileOperation: ExcelFileOperation
};
