var mongoose = require('mongoose'),
    Schema = mongoose.Schema,
    autoIncrement = require('mongoose-auto-increment');

autoIncrement.initialize(mongoose);

var notificationSchema = new Schema({
  id: String,
  content: {type: String, required: true },
  author: {type: String, required: false },
  recipients: {type: [String], required: true },
  creationDate: { type: Date, required: true, default: Date.now },
});

notificationSchema.plugin(autoIncrement.plugin, 'Notification');
var Notification = mongoose.model('Notification', notificationSchema, 'Notifications');

exports.Notification = Notification;