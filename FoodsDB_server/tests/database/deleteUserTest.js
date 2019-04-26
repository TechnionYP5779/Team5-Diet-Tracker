var dbMaker = require("../../src/database/DietTrackerDB");
var fs = require('fs');
var credInfo = JSON.parse(fs.readFileSync('../../creds/tests_creds.json', 'utf8'));
var db = new dbMaker(credInfo.host, credInfo.user, credInfo.password, credInfo.db);
var assert = require("assert");

// user for the test
var user = {
    email: "testt_748thw34uoyt4o74w3ef80gdj340j88734tj@gmail.com"
  };


  // test execution goes from bottom to top 

  var deleteUsersTest = {
    success: function() {
      db.pool.end();
      console.log("SUCCESS!");
    },
    failure: function() {
      assert.fail("failed at deleting users table");
    }
  };
  
  var deleteFoodTest = {
    success: function() {
      db.deleteUsers(deleteUsersTest);
    },
    failure: function() {
      assert.fail("failed at deleting food table");
    }
  };
  
  var deleteAteTest = {
      success: function() {
        db.deleteFood(deleteFoodTest);
      },
      failure: function() {
        assert.fail("failed at deleting ate table");
      }
    };

  var addUserTest2 = {
    success: function() {
      db.deleteAte(deleteAteTest);
    },
    failure: function() {
      assert.fail("failed at adding user once again");
    }
  };

  var deleteUserTest = {
    success: function() {
      db.addUser(user,addUserTest2);
    },
    failure: function(err) {
      assert.fail("failed at deleting user");
    }
  };

  var addUserTest = {
    success: function() {
      db.deleteUser(user,deleteUserTest);
    },
    failure: function() {
      assert.fail("failed at adding user ");
    }
  };

  var createAteTest = {
    success: function() {
      db.addUser(user,addUserTest);
    },
    failure: function() {
      assert.fail("failed at creating ate table");
    }
  };
  
  var createFoodTest = {
    success: function() {
      db.createAte(createAteTest);
    },
    failure: function() {
      assert.fail("failed at creating food table");
    }
  };
  
  var createUsersTest = {
    success: function() {
      db.createFood(createFoodTest);
    },
    failure: function() {
      assert.fail("failed at creating users table");
    }
  };

db.createUsers(createUsersTest);
