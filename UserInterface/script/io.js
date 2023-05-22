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

	constructor (certificatePath, keyPath, address, port){
		process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
		const fileSystem = require("fs");
		const tls = require("tls");
		const option = {
			key: fileSystem.readFileSync(keyPath),
			cert: fileSystem.readFileSync(certificatePath)
		};
		this.#socket = tls.connect(port, address, option, () => {
			this.#socket.setEncoding("utf8");
		});
		const writeData = {
			macAddress: this.#getMacAddress()
		};
		this.#socket.write(`${JSON.stringify(writeData)}\n`);;
	}

	onData(fcn){
		this.#socket.on("data", (data) => fcn(data));
	}

	write(data){
		this.#socket.write(`${data}\n`);
	}

	close() {
		this.#socket.destroy();
	}

	#getMacAddress(){
		let netInterfaces = require("os").networkInterfaces();
		let macAddr;
		for (let key in netInterfaces) {
			let netInterface = netInterfaces[key];
			for (let i of netInterface) {
				if (i.mac!=="00:00:00:00:00:00") {
					macAddr = i.mac;
					break;
				}
			}
			if (macAddr) break;
		}
		return macAddr;
	}
}

module.exports = {
	readExcelFile: readExcelFile,
	readTextFile: readTextFile,
	readFileBase64: readBase64,
	readBlob: readBuffer,
	writeTextFile: writeTextFile,
	socket: SocketOperation
};
