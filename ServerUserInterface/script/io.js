const exec = require("child_process");

exec.exec("ls", (err, stdout, stderr) => {
  if (err){
    console.log(err);
    return;
  }
  if (stderr){
    console.log(stderr);
  }
  console.log(stdout);
})
