var gameLogicModule = require('./gameLogic.js');

exports.createTerritories = function () {

	var territory = null;
	var territoriesArray = new Array();
	
	territory = new gameLogicModule.Territory("A");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("B");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("C");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("D");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("E");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("F");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("G");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("H");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("I");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("J");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("K");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("L");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("M");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("N");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("O");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("P");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Q");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("R");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("S");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("T");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("U");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("V");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("X");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Z");
	territoriesArray.push(territory);

	territory = new gameLogicModule.Territory("Y");
	territoriesArray.push(territory);

    return territoriesArray;
}