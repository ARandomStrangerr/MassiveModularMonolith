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
      moduleName: "DataStream"
    }
    let temp = javaClassPath.split("/");
    temp = temp[temp.length-1];
    let cp = javaClassPath.slice(0, javaClassPath.length-temp.length-1);
    let moduleName = temp.split(".")[0];
    let cmd = `java --enable-preview -cp ${cp} ${moduleName} '${JSON.stringify(param)}' &`;
    childProcess.exec(cmd, (err, stdout, stderr) => {
      // if (err){
      //   console.log(err);
      //   return;
      // }
      // if (stderr){
      //   console.log(stderr);
      //   return;
      // }
      this.#addValidLog("Thành công kết nối đến Data Stream Moddule");
    });
  }

  #addValidLog(msg){
    let listItem = document.createElement("li");
    listItem.classList.add("dot");
    listItem.classList.add("valid-dot");
    listItem.innerText = msg;
    this.#logUnorderedList.appendChild(listItem);
  }
}
