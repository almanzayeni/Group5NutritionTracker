package edu.westga.cs3212.group5.nutritiontracker;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Main Application class.
 * @author Justin Smith
 * @version Spring 2026
 */
public class App extends Application {
	
	private static final String WINDOW_TITLE = "Login";
	private static final String GUI_FXML = "view/LoginPage.fxml";

	@Override
	public void start(Stage primaryStage) {
		try {
			Pane root = this.loadGui();
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.setTitle(WINDOW_TITLE);
			primaryStage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	private Pane loadGui() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(GUI_FXML));
		return (Pane) loader.load();
	}

	/**
	 * Entry point for the application
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
