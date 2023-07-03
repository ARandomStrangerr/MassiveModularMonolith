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

function writeBase64(path, data){
	fileSystem.writeFileSync(path, data, 'base64');
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
	#cache; // incoming data
	#isAlive = true; // the socket is alive or not

	constructor (certificatePath, keyPath, address, port){
		process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
		const fileSystem = require("fs");
		const tls = require("tls");
		const option = {
			key: fileSystem.readFileSync(keyPath),
			cert: fileSystem.readFileSync(certificatePath)
		};
		this.#cache = [];
		let lock = false;
		this.#socket = tls.connect(port, address, option, () => {
			this.#socket.setEncoding("utf8");
		});
		this.#socket.on("error",(e) => {
			console.log(e);
			addInvalidNotification("Không kết nối được đến máy chủ")
		});
		this.#socket.on("close", () => {
			this.#isAlive = false;
		});
		let aux = "";
		this.#socket.on("data", (data) => {
			aux += data;
			if (aux[aux.length -1] === "\n"){
				this.#cache.push(aux);
				aux = "";
			}
		});
		const writeData = {
			macAddress: this.#getMacAddress()
		};
		this.#socket.write(`${JSON.stringify(writeData)}\n`);
	}

	write(data){
		this.#socket.write(`${data}\n`);
	}

	async read(){
		/**
		what a mess of a language.
		this method block the return until an output is reached.
		lesson learned:
		- looping async and setTimeout does not work
		- 'return' keyword within setTimeout does work with outter async
		- the resove / reject function of Promise can be called within setTimeout
		- cannot simply await for the timeout within async
		- async should only be used to await Promise
		=> Promise is more powerful than the sugar coated async syntax
		**/
		let readLine = () => {
			return new Promise((resolve, reject) => {
				setTimeout(() => {
					resolve (this.#cache.shift());
				}, 1000);
			});
		};
		while (this.#isAlive && this.#cache.length != 0){
			let returnData = await readLine();
			if (returnData) return returnData;
		}
		throw "The socket is closed";
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
	writeBase64: writeBase64,
	writeTextFile: writeTextFile,
	socket: SocketOperation
};
