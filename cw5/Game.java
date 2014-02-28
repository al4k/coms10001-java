import java.io.IOException;

public class Game {

	public static void main(String[] args) {
		Game game = new Game();
		game.run(args);
	}
	
	
	public void run(String[] args)
	{
		GameState state = new GameState();
		state.setMapFilename("map.jpg");

        PositionReader p = new PositionReader();
        // Initialise and start GUI
        GUI gui = new GUI();
		gui.registerMapVisualisable(state);
        gui.registerInitialisable(state);
        gui.registerPlayerVisualisable(state);
		gui.run();
	}
}
