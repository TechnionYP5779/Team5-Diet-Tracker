var DB = require("../database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/production_creds.json', 'utf8'));
var db = new DB(credInfo.host, credInfo.user, credInfo.password, credInfo.db);

var deleteUsers = {
    success: function() {
      db.pool.end();
    },
    failure: function() {
      assert.fail("failed at deleting users table");
    }
  };
  
  var deleteAte = {
    success: function() {
      db.deleteUsers(deleteUsers);
    },
    failure: function() {
      assert.fail("failed at deleting food table");
    }
  };
  
  

db.deleteAte(deleteAte);