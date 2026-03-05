package edu.westga.cs3212.group5.nutritiontracker.view;
import com.jfoenix.controls.JFXHamburger;

import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

/**
 * Dashboard View
 * 
 * @author vfilpo :)
 */
public class HomeDashboardPageController {
	@FXML
    private Button calendarButton;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;
    
    @FXML
    private DatePicker calendarPicker;
    
    @FXML
    private Pane menuPane;
    
    private HomeDashboardViewModel viewModel;
   
    /**
     * Sets the ViewModel that data will be pulled from.
	 * After calling this method, the {@code calendarPicker.valueProperty()} will be
	 * bound bidirectionally to the ViewModel's {@code selectedDateProperty}, 
	 * ensuring that changes in either the UI or the ViewModel are reflected in both.
     * 
     * @param the HomeDashboardViewModel instance to bind to this controller
     */
    private void setViewModel(HomeDashboardViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties();
    }

    private void bindProperties() {
        calendarPicker.valueProperty().bindBidirectional(viewModel.selectedDateProperty());
        //TODO: remove
        System.out.println(this.calendarPicker.getValue());
        
    }

    @FXML
    private void initialize() {
    	this.setViewModel(new HomeDashboardViewModel());
        //TODO: remove
        /*
         * Below is how we will add event listeners to make the UI reactive:
         * 
         * viewModel.selectedDateProperty().addListener((obs, oldDate, newDate) -> {...});
         */
    }
}
