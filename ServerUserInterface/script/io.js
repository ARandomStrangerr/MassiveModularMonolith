class Socket{
  #socket;
  constructor(serverAddress, port){
    let socket = require("net");
    this.#socket = socket.connect(port, serverAddress,() => {
      this.#socket.setEncoding("utf-8");
    });
  }

  setOnDataReceive(callback){
    this.#socket.on("data",callback);
  }
}

class TextFile{
  #fileSystem;
  constructor(){
    this.#fileSystem = require("fs");
  }

  writeToFile(path, data){
    this.#fileSystem.writeFile(path, data, function (error) {
			if (error) console.log(error);
		});
  }

  readFromFile(path){
    return this.#fileSystem.readFileSync(path, "utf8");
  }
}

module.exports = {
  Socket: Socket,
  TextFile: TextFile
};
