const fs = require('fs');
const users = JSON.parse(fs.readFileSync('./users.json', 'utf8'));

function login(username, password) {
  return users.some(user => user.username === username && user.password === password);
}

module.exports = { login };
