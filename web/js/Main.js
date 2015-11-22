/**
 * Created by noam on 11/22/15.
 */

var CodewashGame = CodewashGame || {};

CodewashGame.game = new Phaser.Game(800, 450, Phaser.AUTO, '');

CodewashGame.game.state.add('Boot', CodewashGame.Boot);
CodewashGame.game.state.add('Preload', CodewashGame.Preload);
CodewashGame.game.state.add('Game', CodewashGame.Game);

CodewashGame.game.state.start('Boot');