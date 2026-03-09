Log Food or Meal Consumption

Actor

User



Preconditions

User is logged in.

Food or meal items exist in the system.



Main Flow

1. User navigates to the dashboard.
2. User selects a date.
3. User selects "Add food" button.
4. System asks if the user would like to search for a food, or add a custom food.
5. User searches or browses available food or meal items.
6. User selects a food or meal item.
7. User confirms it was eaten.
8. System records the item in the daily log.
9. System updates remaining calories and nutrition values.



Alternate Flows

4a. Add Custom Food

* A new window pops up allowing the user to input food information
* User clicks submit

4b. Invalid food values input

* Pop up error message
* Allow user to continue adding values to correct invalid input.



Postconditions

The selected food or meal is recorded for the specified day.

(If custom food) Custom food is added to user database, server is updated, custom food is now available in subsequent searches.

