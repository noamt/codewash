/**
 * Created by noam on 11/22/15.
 */

//loading the game assets
var CodewashGame = CodewashGame || {};
CodewashGame.Preload = function(){};

CodewashGame.Preload.prototype = {
    preload: function() {
        //load game assets
        this.load.tilemap('firstLevel', 'assets/tilemaps/first.level.tilemap.json', null, Phaser.Tilemap.TILED_JSON);
        this.load.image('dirtTile', 'assets/tiles/dirt2.png');
        this.load.image('dirtPathTile', 'assets/tiles/dirt.png');
        this.load.image('bluesun', 'assets/bluesun.png');
        this.load.image('container', 'assets/container.png');
//            this.load.image('player', 'assets/images/player.png');
        this.load.image('entrance', 'assets/entrance.png');

    },
    create: function() {
        this.state.start('Game');
    }
};