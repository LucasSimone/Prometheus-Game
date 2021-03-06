package prometheus.gamecontroller;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EventHandler {
	public static char lastKeyPress;
	public static char lastKeyReleased;
	public static ArrayList<KeyCode> inputList = new ArrayList<KeyCode>();
	
	/**
	 * Attach key handlers to the scene object provided
	 * @param s scene to add the handlers to
	 */
	public static void attachEventHandlers(Scene s) {
		keyReleaseHandler krh = new keyReleaseHandler();
		keyPressedHandler kph = new keyPressedHandler();
		s.setOnKeyReleased(krh);
		s.setOnKeyPressed(kph);
	}
	
	/**
	 * Function that keys is a key is currently pressed
	 * @param k key that is being checked
	 * @return true if key is pressed, false otherwise
	 */
	public boolean isKeyDown(KeyCode k) {
		if (inputList.contains(k)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return Current key inputs
	 */
	public static List<KeyCode> getInputList() {
		return inputList;
	}
}

class keyReleaseHandler implements javafx.event.EventHandler<KeyEvent> {
	public keyReleaseHandler() {
	}
	
	/**
	 * Handle key inputs for key releases
	 */
	@Override
	public void handle(KeyEvent evt) {
		KeyCode code = evt.getCode();

		if (EventHandler.inputList.contains(code))
			EventHandler.inputList.remove(code);
	}
}

class keyPressedHandler implements javafx.event.EventHandler<KeyEvent> {
	
	/**
	 * Handle key inputs for key presses
	 */
	@Override
	public void handle(KeyEvent evt) {
		KeyCode code = evt.getCode();

		// only add once... prevent duplicates
		if (!EventHandler.inputList.contains(code))
			EventHandler.inputList.add(code);
	}
}
