import java.io.IOException;

public class Game {

	public static void main(String[] args) {
		Game game = new Game();
		game.run(args);
	}
	
	/*
	 * note: There are two temporary variables whose definitions need to be relocated
	 *  line 83 (GameState) - sets the starting player
	 *  line 49 (GUI) - sets number of detectives playing
	 */
	public void run(String[] args)
	{
		GameState state = new GameState();
		state.setMapFilename("map.jpg");

        PositionReader p = new PositionReader();
        // Initialise and start GUI
        GUI gui = new GUI();
		gui.registerMapVisualisable(state);
        gui.registerPlayerVisualisable(state);
        gui.registerInitialisable(state);
        gui.registerVisualisable(state);
        gui.registerControllable(state);
		gui.run();
	}
}
