
exports.House = function (name) {
  this.name = name;
}

exports.Move = function (origin, target, action) {
  this.origin = origin;
	this.target = target;
	this.action = action;
}

exports.Territory = function (name, owner) {
	this.name = name;
	this.owner = owner;
}

exports.Player = function (id, name, house) {
	this.id = id;
	this.name = name;
  this.house = house;
}

exports.Conflict = function (territory, houses) {
  this.territory = territory;
  this.houses = houses;
}