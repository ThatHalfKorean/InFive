var User = require('../models/user').User,
    Notification = require('../models/notification').Notification,
    tokens = require('./tokens'),
    auth = require('./authentication');

var notificationFuncs = {
  createNotification: function (request, reply) {
    var notification = request.payload;
    User.findOne({authToken: notification.Authorization}, function (err, existingUser) {
      if (auth.checkAuthToken(existingUser, reply)) {
        var username = existingUser.username;
        var newNotification = new Notification({
          content: notification.content,
          author: username,
          recipients: notification.recipients
        });
        newNotification.save();
        reply({
          message: "Success",
          id: notification._id,
          author: username,
          recipients: notification.recipients,
          response: "Created a new notification"
        });
      }
    });
  },

  getNotificationsCreatedByUser: function (request, reply) {
      User.findOne({authToken: request.headers.authorization}, function (err, existingUser) {
        if (auth.checkAuthToken(existingUser, reply)) {
          var username = existingUser.username;
          Notification.find({ author: username }, function (err, notifications) {
            var sortedNotifications = notifications.sort(function (a, b) {
              var dateA = a.creationDate.getTime(),
                  dateB = b.creationDate.getTime();
              if (dateA < dateB) {
                return -1;
              }
              if (dateA > dateB) {
                return 1;
              }
              return 0;
            });
            reply({
              message: "Success",
              response: sortedNotifications
            });
          });
        }
      });
    },


    getNotificationsFeed: function (request, reply) {
      User.findOne({authToken: request.headers.authorization}, function (err, user) {
        if (auth.checkAuthToken(user, reply)) {
          var username = user.username;
          Notification.find({ recipients: username }, function (err, notifications) {
            var viewableNotifications = [];
            var index = 0;
            notifications.forEach(function (notification) {
              viewableNotifications[index] = notification;
              index++;
            });
            var sortedNotifications = viewableNotifications.sort(function (a, b) {
              var dateA = a.creationDate.getTime(),
                  dateB = b.creationDate.getTime();
              if (dateA > dateB) {
                return -1;
              }
              if (dateA < dateB) {
                return 1;
              }
              return 0;
            });
            reply({
              message: "Success",
              response: sortedNotifications
            });
          });
        }
      });
    }

};

module.exports = notificationFuncs;