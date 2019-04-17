var DB = require("../database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/tests_creds.json', 'utf8'));
var db = new DB(credInfo.host, credInfo.user, credInfo.password, credInfo.db);

var createAte = {
    success: function() {
     db.pool.end();
    },
    failure: function() {
      assert.fail("failed at creating ate table");
    }
  };
  
  var createFood = {
    success: function() {
      db.createAte(createAte);
    },
    failure: function() {
      assert.fail("failed at creating food table");
    }
  };
  
  var createUsers = {
    success: function() {
      db.createFood(createFood);
    },
    failure: function() {
      assert.fail("failed at creating users table");
    }
  };
  
  db.createUsers(createUsers);
  