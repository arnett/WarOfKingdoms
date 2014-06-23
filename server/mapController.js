var utilsModule = require('./utils.js');


Territory = function (name, region, owner) {
    this.name   = name;
    this.region = region;
    this.owner  = owner;
}

exports.Map = function () {
    this.map = this.createMap();
}

this.Map.prototype.addOwnerInTerritory = function(owner, territoryName) {
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].name == territoryName)
            this.map[i].owner = owner;
}

this.Map.prototype.get = function(i) {
    return this.map[i];
}

this.Map.prototype.length = function() {
    return this.map.length;
}

this.Map.prototype.getByName = function(name) {
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].name == name)
            return this.map[i]

    return null
}

this.Map.prototype.update = function(conflicts, nonConflicting) {
    for (var i = 0; i < nonConflicting.length; i++)
        this.getByName(nonConflicting[i].target.name).owner = nonConflicting[i].origin.owner;

    for (var i = 0; i < conflicts.length; i++) {
        var biggestDiceValue = utilsModule.getBiggestDiceValueIndex(conflicts[i].diceValues);

        if (biggestDiceValue != -1) {
            this.getByName(conflicts[i].territory.name).owner = conflicts[i].houses[biggestDiceValue];
        }
    }
}

this.Map.prototype.isLastSurvivor = function(ownerName) {
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].owner != null && this.map[i].owner.name != ownerName)
            return false;

    return true;
}

this.Map.prototype.doPlayerKeepTerritory = function(ownerName, territoryName) {
    return this.getByName(territoryName).owner.name == ownerName;
}

this.Map.prototype.getNumberOfTerritoriesConqueredInRegion = function(ownerName, region) {
    var num = 0;
    for (var i = 0; i < this.map.length; i++)
        if (this.map[i].region == region && this.map[i].owner != null && this.map[i].owner.name == ownerName)
            num++;

    return num;
}

this.Map.prototype.createMap = function () {
	var territory = null;
	var territoriesArray = new Array();

	// NORTH
	territory = new Territory("A", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("B", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("C", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("D", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("E", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("F", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("G", "N", null);
	territoriesArray.push(territory);

	territory = new Territory("H", "N", null);
	territoriesArray.push(territory);

	// CENTER
	territory = new Territory("I", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("J", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("K", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("L", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("M", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("N", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("O", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("P", "C", null);
	territoriesArray.push(territory);

	territory = new Territory("Q", "C", null);
	territoriesArray.push(territory);

	// SOUTH
	territory = new Territory("R", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("S", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("T", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("U", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("V", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("X", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("Z", "S", null);
	territoriesArray.push(territory);

	territory = new Territory("Y", "S", null);
	territoriesArray.push(territory);

    territory = new Territory("Z", "S", null);
    territoriesArray.push(territory);

    return territoriesArray;
}

