var exec = require('cordova/exec');

function intermecScanner() {
 console.log("intermecScanner.js: is created");
}

intermecScanner.prototype.activateScanner = function(cb){
  console.log("intermecScanner.js: activateScanner");
  exec(function(result){
    cb(result);
  },
  function(result){
    cb(result);
  },"intermecScanner","ACTIVATE",[]);
}
intermecScanner.prototype.deactivateScanner = function(cb){
  console.log("intermecScanner.js: deactivateScanner");
  exec(function(result){
    cb(result);
  },
  function(result){
    cb(result);
  },"intermecScanner","DEACTIVATE",[]);
}

 var intermecScanner = new intermecScanner();
 module.exports = intermecScanner;
