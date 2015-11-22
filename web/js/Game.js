/**
 * Created by noam on 11/22/15.
 */

//title screen
var CodewashGame = CodewashGame || {};
CodewashGame.Game = function(){};

CodewashGame.Game.prototype = {
    //find objects in a Tiled layer that containt a property called "type" equal to a certain value
    findObjectsByType: function(type, map, layer) {
        var result = new Array();
        map.objects[layer].forEach(function(element){
            if(element.properties.type === type) {
                //Phaser uses top left, Tiled bottom left so we have to adjust the y position
                //also keep in mind that the cup images are a bit smaller than the tile which is 16x16
                //so they might not be placed in the exact pixel position as in Tiled
                element.y -= map.tileHeight;
                result.push(element);
            }
        });
        return result;
    },
    //create a sprite from an object
    createFromTiledObject: function(element, group) {
        var sprite = group.create(element.x, element.y, element.properties.sprite);

        //copy all properties to the sprite
        Object.keys(element.properties).forEach(function(key){
            sprite[key] = element.properties[key];
        });
    },
    createItems: function() {
        //create items
        this.items = this.game.add.group();
        this.items.enableBody = true;
        var item;
        result = this.findObjectsByType('item', this.map, 'middleGroundLayer');
        result.forEach(function(element){
            this.createFromTiledObject(element, this.items);
        }, this);
    },
    createEntrances: function() {
        //create doors
        this.entrances = this.game.add.group();
        this.entrances.enableBody = true;
        result = this.findObjectsByType('entrance', this.map, 'objectsLayer');

        result.forEach(function(element){
            this.createFromTiledObject(element, this.entrances);
        }, this);
    },
    create: function() {
        this.map = this.game.add.tilemap('firstLevel');

        //the first parameter is the tileset name as specified in Tiled, the second is the key to the asset
        this.map.addTilesetImage('dirtTile', 'dirtTile');
        this.map.addTilesetImage('dirtPathTile', 'dirtPathTile');

        //create layer
        this.dirtLayer = this.map.createLayer('dirtLayer');
        this.middleGroundLayer = this.map.createLayer('middleGroundLayer');

        //collision on blockedLayer
        this.map.setCollisionBetween(1, 100000, true, 'middleGroundLayer');

        //resizes the game world to match the layer dimensions
        this.dirtLayer.resizeWorld();

        this.createItems();
        this.createEntrances();
    }
};