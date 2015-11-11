document.onreadystatechange = function () {
    if (document.readyState == "complete") {
        var game = new PixelJS.Engine();

        game.init({
            container: 'game_container',
            width: 800,
            height: 450
        });

        var backgroundLayer = game.createLayer('background');
        var grass = backgroundLayer.createEntity();
        backgroundLayer.static = true;
        grass.pos = { x: 0, y: 0 };
        grass.asset = new PixelJS.Tile();
        grass.asset.prepare({
            name: 'dirt2.png',
            size: {
                width: 800,
                height: 450
            }
        });

        var playerLayer = game.createLayer("players");
        var mal = new PixelJS.Player();
        mal.addToLayer(playerLayer);
        mal.pos = { x: 200, y: 300 };
        mal.size = { width: 32, height: 32 };
        mal.velocity = { x: 100, y: 100 };
        mal.asset = new PixelJS.AnimatedSprite();
        mal.asset.prepare({
            name: 'mal.sprite.png',
            frames: 3,
            rows: 4,
            speed: 100,
            defaultFrame: 1
        });

        game.loadAndRun(function (elapsedTime, dt) {
        });
    }
};