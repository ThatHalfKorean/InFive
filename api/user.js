var mongoose = require('mongoose'),
    Schema = mongoose.Schema,
    autoIncrement = require('mongoose-auto-increment');

autoIncrement.initialize(mongoose);

var userSchema = new Schema({
  id: String,
  username: {type: String, required: true },
  name: {type: String, required: true },
  blocked: {type: [String], required: false },
  email: {type: String, required: false },
  password: {type: String, required: true },
  friends: {type: [String], required: false },
  authToken: {type: String, required: false },
  tokenExpiration: { type: Date, required: false },
  creationDate: { type: Date, required: true, default: Date.now }
});

userSchema.plugin(autoIncrement.plugin, 'User');
var User = mongoose.model('User', userSchema, 'Users');

exports.User = User;