var User = require('../models/user').User,
    tokens = require('./tokens'),
    auth = require('./authentication');

var userFuncs = {
  searchForUsers: function (request, reply) {
    User.findOne({authToken: request.headers.authorization}, function (err, existingUser) {
      if (auth.checkAuthToken(existingUser, reply)) {
        var searchString = request.headers.search;
        User.find({username: new RegExp('^' + searchString + '.*$', "i")}, function (err, users) {
          if (!users.length) {
            reply({
              message: "Unsuccessful",
              response: "No users found."
            });
          } else {
            var usersArr = [];
            var index = 0;
            users.forEach(function (user) {
              usersArr[index] = user.username;
              index++;
            });
            reply({
              message: "Success",
              response: usersArr
            });
          }
        });
      }
    });
  },

  addFriend: function (request, reply) {
    var body = request.payload;
    User.findOne({authToken: body.Authorization}, function (err, user) {
      if (auth.checkAuthToken(user, reply)) {
        User.findOne({username: body.newFriend}, function (err, newFriend) {
          if (!newFriend) {
            reply({
              error: "No user",
              reason: "The user you are trying to add as a friend does not exist."
            }).code(404);
          } else {
            var notFriends = true;
            for (var i = 0; i < user.friends.length; i++) {
              if (user.friends[i] === body.newFriend) {
                notFriends = false;
                break;
              }
            }
            if (notFriends) {
            user.friends.push(body.newFriend);
            user.save();
            reply({
              message: "Success",
              id: user._id,
              name: user.name,
              username: user.username,
              newFriend: body.newFriend,
              response: "Added a new friend"
            });
            } else {
              reply({
                error: "Already friends",
                reason: body.newFriend + " is already your friend!"
              }).code(409);
            }
          }
        });
      }
    });
  },

  getFriends: function (request, reply) {
    User.findOne({authToken: request.headers.authorization}, function (err, user) {
      if (auth.checkAuthToken(user, reply)) {
        var sortedFriendsArray = user.friends.sort(function (a, b) {
          var nameA = a.toLowerCase(),
              nameB = b.toLowerCase();
          if (nameA < nameB) {
            return -1;
          }
          if (nameA > nameB) {
            return 1;
          }
          return 0;
        });
        reply({
          message: "Success",
          response: sortedFriendsArray
        });
      }
    });
  }

};

module.exports = userFuncs;