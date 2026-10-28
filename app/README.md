# Smart Pantry Manager

## Project Description

Smart Pantry Manager is an Android application developed in Java that helps users manage ingredients available in their pantry and discover recipes they can make using those ingredients.

The application uses strict recipe matching. A recipe is suggested only when the user has every required ingredient in sufficient quantity.

## Main Features

- Add pantry ingredients
- View pantry ingredients
- Update pantry ingredients
- Delete pantry ingredients
- Store ingredient quantities and units
- Save pantry information between app sessions
- Suggest recipes based on available pantry ingredients
- Check required ingredient quantities
- Handle simple unit conversions
- Handle simple singular and plural ingredient names
- View recipe ingredients and preparation instructions
- Display feedback when no recipes can be made
- Save user preferences
- Filter recipes using a vegetarian preference

## Recipe Matching

The application uses strict ingredient matching.

For a recipe to be displayed:

1. Every required ingredient must exist in the user's pantry.
2. The available quantity must be equal to or greater than the required quantity.
3. Compatible units are converted when necessary.
4. Simple singular and plural ingredient names are normalised.

If even one required ingredient is missing or insufficient, the recipe is not suggested.

## Database

The application uses SQLite for local data storage.

SQLite was selected because it:

- Works directly on Android devices.
- Does not require an internet connection.
- Provides persistent local storage.
- Supports structured tables and relationships.
- Is suitable for storing pantry items, recipes and recipe ingredients.

Pantry information remains available after the application is closed and reopened.

## Technologies Used

- Java
- Android Studio
- SQLite
- XML
- RecyclerView
- SharedPreferences
- Git
- GitHub

## Application Screens

The application contains the following main screens:

### Home
Provides access to the main features of the application.

### My Pantry
Allows the user to add, view, update and delete pantry ingredients.

### Suggested Recipes
Displays recipes that can be made using the ingredients currently available in the pantry.

### Recipe Detail
Displays the complete ingredient requirements and preparation instructions for a selected recipe.

### Settings
Allows the user to save their name and vegetarian recipe preference.

## Recipe Data

The application contains 20 predefined recipes stored in the database.

Each recipe contains:

- Recipe name
- Required ingredients
- Required quantities
- Units
- Preparation instructions
- Vegetarian classification

## Data Persistence

SQLite is used to persist pantry and recipe data.

SharedPreferences is used to persist user settings such as:

- User name
- Vegetarian preference

This allows information to remain available after the application is closed and reopened.

## How to Run the Application

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to synchronize the project.
4. Start an Android emulator or connect an Android device.
5. Select the `app` run configuration.
6. Click the Run button in Android Studio.
7. The Smart Pantry Manager application will launch.

## Testing

The application was tested for:

- Adding pantry ingredients
- Updating pantry ingredients
- Deleting pantry ingredients
- Database persistence
- Recipe matching
- Insufficient ingredient quantities
- Unit conversion
- Singular and plural ingredient names
- Vegetarian filtering
- Recipe detail display
- No matching recipe feedback
- User settings persistence

## Author

Mcdonald Mudavanhu