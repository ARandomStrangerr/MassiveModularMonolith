let fileSystem = require("fs");

function readTextFile(path){
	return fileSystem.readFileSync(path, "utf8");
}

function readBuffer(path){
	return fileSystem.readFileSync(path);
}

function readBase64(path){
	return fileSystem.readFileSync(path, {encoding: "base64"});
}

function writeTextFile(path, data){
	fileSystem.writeFile(path, data, function (error) {
		if (error) console.log(error);
	});
}

async function readExcelFile(path){
	const excelReader = require("read-excel-file");
	const data = await excelReader(path);
	return data;
}
class SocketOperation {
	#socket; // the socket itself

	constructor (certificatePath, keyPath, address, port, macAddress, errHandler){
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
		socket.on("error", errHandler)
		socket.write(`${JSON.stringify(writeData)}\n`);
		// set the local variables to this class private variables
		this.#socket = socket;
	}

	read(callback) {
		this.#socket.on("data", callback);
	}
	write(data){
		this.#socket.write(`${data}\n`);
	}
	close() {
		this.#socket.destroy();
	}
}

module.exports = {
	readExcelFile: readExcelFile,
	readTextFile: readTextFile,
	readFileBase64: readBase64,
	readBlob: readBuffer,
	writeTextFile: writeTextFile
};

