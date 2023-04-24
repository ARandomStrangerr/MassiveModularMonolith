// read file
const fileIO = new (require("./script/io.js")).TextFile();
let data;
try {
  data = JSON.parse(fileIO.readFromFile("setting.txt"));
  for (let key in data){
    document.querySelector(`#${key}`).value = data[key];
  }
} catch (e){
  console.log(e);
  data = {};
}
for (let i of document.querySelectorAll(".input")){
  i.addEventListener("blur", () => {
    data[i.id] = i.value.trim();
    fileIO.writeToFile("setting.txt", JSON.stringify(data));
  });
}

// input file picker
for (let i of document.querySelectorAll(".file-input")){
  i.readOnly=true;
  i.addEventListener("click", ()=>{
    let fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.onchange = () => {
      i.value = fileInput.files[0].path;
      data[i.id] = i.value.trim();
      fileIO.writeToFile("setting.txt", JSON.stringify(data));
    }
    fileInput.click();
  });
}

let dataStreamPageControl = new (require("./script/DataStreamPageControl.js"))(document.querySelector("#data-stream-module-log > div:last-child > ul"));

// collapsible elements behaviour
let expandable = document.querySelectorAll(".title-with-expandable");
for (let i of expandable){
  let expandButton = i.querySelector("div:first-child > .expand-button");
  expandButton.addEventListener('click', () => {
    i.querySelector("div:last-child").classList.toggle("collapse"); //i fu**ing hate js, whoever designs this progamming language should be reserved a special place in hell next to Satan.
  });
}


// start data stream module
document.querySelector("#start-data-stream-module-button").addEventListener("click", () => {
  let classPath = document.querySelector("#data-stream-module-classpath-input").value;
  let port = document.querySelector("#data-stream-module-port-input").value;
  let jksPath = document.querySelector("#data-stream-module-jks-input").value;
  let jksPassword = document.querySelector("#data-stream-module-jks-password-input").value;
  let timeout = document.querySelector("#data-stream-module-timeout-input").value;
  dataStreamPageControl.startModule(classPath, port, jksPath, jksPassword, timeout)
});
