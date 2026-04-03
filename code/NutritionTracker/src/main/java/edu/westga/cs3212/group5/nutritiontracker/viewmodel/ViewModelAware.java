package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

/**
 * Interface for controllers that require a {@link HomeDashboardViewModel}.
 * 
 * <p>Implementing this interface allows {@code switchTo} to forward the
 * current {@link HomeDashboardViewModel} to any controller it navigates to,
 * ensuring that user data and state persist across page transitions.
 *
 * @author vfilpo :)
 * @version Spring 2026
 */
public interface ViewModelAware {
	
    /**
     * Sets the ViewModel for this controller.
     * 
     * <p>This method should be called immediately after {@code FXMLLoader.load()}
     * and before the stage is shown, so that all bindings and UI setup
     * that depend on the ViewModel are initialized correctly.
     *
     * @param viewModel the {@link HomeDashboardViewModel} to bind to this controller
     */
    void setViewModel(HomeDashboardViewModel viewModel);
}