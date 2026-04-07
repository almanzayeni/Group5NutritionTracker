package edu.westga.cs3212.group5.nutritiontracker.view;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class FoodItemCell extends ListCell<FoodItem> {

	@FunctionalInterface
	public interface RemoveHandler {
		void remove(FoodItem item);
	}

	private final RemoveHandler removeHandler;

	private final Label descLabel = new Label();
	private final Label kcalLabel = new Label();
	private final Button removeButton = new Button("✕");
	private final Region spacer = new Region();
	private final HBox root = new HBox(10);

	public FoodItemCell(RemoveHandler removeHandler) {
		this.removeHandler = removeHandler;

		descLabel.setStyle("-fx-text-fill: #1f5c33; -fx-font-weight: bold;");
		kcalLabel.setStyle("-fx-text-fill: rgba(31,92,51,0.75);");
		HBox.setHgrow(spacer, Priority.ALWAYS);

		removeButton.setFocusTraversable(false);
		removeButton.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: #1f5c33;"
				+ "-fx-font-weight: bold;" + "-fx-background-radius: 8;" + "-fx-border-color: rgba(31,92,51,0.35);"
				+ "-fx-border-radius: 8;" + "-fx-padding: 2 8 2 8;");

		root.getChildren().addAll(descLabel, kcalLabel, spacer, removeButton);
		root.setStyle("-fx-alignment: center-left; -fx-padding: 6 8 6 8;");

		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		removeButton.setOnAction(e -> {
			FoodItem item = getItem();
			if (item != null)
				removeHandler.remove(item);
		});
	}

	@Override
	protected void updateItem(FoodItem item, boolean empty) {
		super.updateItem(item, empty);

		if (empty || item == null) {
			setGraphic(null);
		} else {
			descLabel.setText(item.getDescription());
			kcalLabel.setText("— " + String.format("%.0f", item.getCalories()) + " kcal");
			setGraphic(root);
		}
	}
}