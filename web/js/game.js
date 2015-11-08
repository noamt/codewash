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

        game.loadAndRun(function (elapsedTime, dt) {
        });
    }
};