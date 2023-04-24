module.exports = class {
  #logUnorderedList;
  constructor(logUnorderedList){
    this.#logUnorderedList = logUnorderedList;
  }

  startModule(javaClassPath, port, jksPath, jksPassword, timeout){
    let childProcess = require("child_process");
    let param = {
      port: port,
      timeout: timeout,
      keyStorePath: jksPath,
      keyStorePassword :jksPassword,
      moduleName: "DataStream",
      monitorToolPort: 9997
    }
    let temp = javaClassPath.split("/");
    temp = temp[temp.length-1];
    let cp = javaClassPath.slice(0, javaClassPath.length-temp.length-1);
    let moduleName = temp.split(".")[0];
    let cmd = `java --enable-preview -cp ${cp} ${moduleName} '${JSON.stringify(param)}'`;
    childProcess.exec(cmd);

    let socket;
    setTimeout(() => {
      socket = new (require('./io.js')).Socket("127.0.0.1", 9997);
      socket.setOnDataReceive((data)=>this.#onDataEvent(data)); // f*** this but where does this points to? i don't f***ing know :)
    }, 1000);
  }

  #onDataEvent(data){
    try{
      data = JSON.parse(data);
    } catch (e) {
      console.log(e);
      return;
    }
    let printoutData = `${data.time} ${data.status} ${data.notification}`;
    this.#addValidLog(printoutData);
  }

  #addValidLog(msg){
    let listItem = document.createElement("li");
    listItem.classList.add("dot");
    listItem.classList.add("valid-dot");
    listItem.innerText = msg;
    this.#logUnorderedList.appendChild(listItem);
  }

  #addInvalidLog(msg){
    let listItem = document.createElement("li");
    listItem.classList.add("dot");
    listItem.classList.add("invalid-dot");
    listItem.innerText = msg;
    this.#logUnorderedList.appendChild(listItem);
  }
}
