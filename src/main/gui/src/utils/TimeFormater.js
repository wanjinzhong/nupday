export function formatDate(value, type) {
  var dataTime = "";
  var data = new Date();
  data.setTime(value);
  var year = data.getFullYear();
  var month = addZero(data.getMonth() + 1);
  var day = addZero(data.getDate());
  var hour = addZero(data.getHours());
  var minute = addZero(data.getMinutes());
  var second = addZero(data.getSeconds());
  if (type == "DATE") {
    dataTime = year + "-" + month + "-" + day;
  } else if (type == "DATETIME") {
    dataTime = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
  } else if (type == "TIME") {
    dataTime = hour + ":" + minute + ":" + second;
  }
  return dataTime;
};

function addZero(val){
  if(val < 10){
    return "0" +val;
  }else{
    return val;
  }
};
