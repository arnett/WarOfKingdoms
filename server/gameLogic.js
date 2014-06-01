exports.Move = function (action, target) {

	this.action = action;
	this.targetTerritory = target;
}

exports.Territory = function (name) {
	this.name = name;
}
