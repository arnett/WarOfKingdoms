exports.Move = function (action, target) {

	this.action = action;
	this.targetTerritory = target;
}

exports.Territory = function (name, owner) {
	this.name = name;
	this.owner = owner;
}

exports.Player = function (id, name) {
	this.id = id;
	this.name = name;
}
