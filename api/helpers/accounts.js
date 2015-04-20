var User = require('../models/user').User,
    Bcrypt = require('bcrypt'),
    Uuid = require('uuid'),
    tokens = require('./tokens');

var accountFuncs = {
    signup: function(request, reply) {
      var user = request.payload;
      if (user.password !== user.confirmPassword) {
        reply({
          reason: "Passwords don't match",
          message: "Passwords must match"
        }).code(400);
      }
      User.findOne({username: user.username}, function (err, found) {
        if (found) {
            reply({
              reason: "Username taken",
              message: "Username " + user.username + " is already taken"
            }).code(409);
        } else {
          Bcrypt.genSalt(10, function(err, salt) {
            Bcrypt.hash(user.password, salt, function (err, hash) {
              var newUser = new User({
                username: user.username,
                password: hash,
                email: user.email
              });
              newUser.save(function (err) {console.log(err);});
              reply({
                id: user._id,
                username: user.username,
                response: "Created a new user"
              });
            });
          });
        }
      });
    },
    login: function (request, reply) {
      var user = request.payload;
      User.findOne({username: user.username}, function (err, existingUser) {
        if (!existingUser) {
          reply({
            reason: "Invalid username.",
            message: "Please enter a valid username."
          }).code(401);
        } else {
          Bcrypt.compare(user.password, existingUser.password, function (err, isValid) {
            if (isValid) {
                if (!existingUser.authToken) {
                  var token = Uuid.v1();
                  existingUser.authToken = token;
                  existingUser.tokenExpiration = tokens.setExpiration();
                  existingUser.save(function(err) {
                    if (err) {
                      reply({
                        reason: "error", 
                        message: err
                      }).code(401);
                    } else {
                      reply({
                        message: "Success",
                        response: "Account authorized; note token",
                        token: token
                      });
                    }
                  });
                } else {
                  reply({
                    reason: "Invalid login",
                    message: "User is already logged in."
                  }).code(403);
                }
            } else {
              reply({
                reason: "Invalid password.",
                message: "Invalid password."
              }).code(401);
            }
          });
        }
      });
    },

    logout: function (request, reply) {
      var token = request.headers.authorization;
      User.findOne({authToken: token}, function (err, existingUser) {
        if (!existingUser) {
          reply({
            reason: "Already logged out",
            message: "User already logged out."
          }).code(401);
        } else {
          tokens.setTokenToNull(existingUser);
          existingUser.save(function (err) {
            if (err) {
              reply({
                reason: "error", 
                message: err
              });
            } else {
              reply({
                message: "Success",
                response: "User's session has ended."
              });
            }
          });
        }
      });
    }
};

module.exports = accountFuncs;