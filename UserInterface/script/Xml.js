function fromJsonToXml(jsonObj){
  let output = "";
  for (let key in jsonObj){
    if (typeof(jsonObj[key]) === "object"){
      output += `<${key}>`;
      output += fromJsonToXml(jsonObj[key]);
      output += `</${key}>`;
    }
    else output += `<${key}>${jsonObj[key]}</${key}>`;
  }
  return output;
}

module.exports = {
  fromJsonToXml: fromJsonToXml
};
