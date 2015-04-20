var mongoose = require('mongoose'),
    Schema = mongoose.Schema,
    autoIncrement = require('mongoose-auto-increment');

autoIncrement.initialize(mongoose);

var eventSchema = new Schema({
  id: String,
  eventTitle: {type: String, required: true },
  host: {type: String, required: false },
  address: {type: String, required: true },
  recipients: {type: [String], required: true },
  creationDate: { type: Date, required: true, default: Date.now },
  eventDate: { type: Date, required: true }
});

eventSchema.plugin(autoIncrement.plugin, 'Event');
var Event = mongoose.model('Event', eventSchema, 'Events');

exports.Event = Event;