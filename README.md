# VehicleCare - Smart Maintenance Tracking

This project is an Android application for tracking vehicle maintenance.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Recent Updates

### Logic Implementation: Navigation & Events
Connected the UI screens using Intents and established a central event handling structure.

#### 1. Activity Management
- **Created `CreateAccountActivity.java`**: Handles the logic for the account creation screen, including a functional back button.
- **Manifest Update**: Registered `CreateAccountActivity` in `AndroidManifest.xml`.
- **MainActivity Update**: Configured `MainActivity` (acting as the Login screen) to initialize event listeners.

#### 2. Event Handling (`Events.java`)
- Created a dedicated `Events` class to decouple UI logic from Activity classes.
- **`setupLoginEvents`**: Implemented an `OnClickListener` for `tvSignUp` in the login layout that triggers an `Intent` to navigate to `CreateAccountActivity`.

---

### UI Implementation: Create Account Screen
The "Create Account" interface has been fully implemented in `create_account.xml`, matching the design requirements.

#### 1. Resource Expansion
- **Icons**: Added `ic_back.xml` for navigation and `ic_person.xml` for the name field.
- **Colors**: Added `light_green` (#D1F5E8) for specialized UI elements.
- **Drawables**: Created `bg_logo_create_account.xml` for the themed icon container.
- **Strings**: Added resources for "Get Started", "Full Name", "Confirm Password", and other registration labels.

#### 2. Layout Implementation (`create_account.xml`)
- **Toolbar**: Features a custom toolbar with a back button and title.
- **Header**: Includes a themed car logo container, "Get Started" header, and descriptive subtitle.
- **Social Integration**: Reused stylized buttons for Google and Facebook login.
- **Form Card**: A white card containing registration fields and a primary "Create Account" button.
- **UX**: Wrapped in `NestedScrollView` for better accessibility and keyboard handling.

---

### UI Implementation: Login Screen
The login interface has been fully implemented in `login.xml` based on the design specifications.

#### 1. Color Palette Configuration
Updated `res/values/colors.xml` with standard branding colors.

#### 2. Layout Implementation (`login.xml`)
- **Root**: `ConstraintLayout` with a primary green background.
- **Form Card**: A white card covering 70% of the screen with login inputs and social buttons.

## Accessibility & Best Practices
- Added `contentDescription` for all image resources.
- Configured `autofillHints` for all relevant input fields (name, email, password).
- Used `NestedScrollView` to ensure responsiveness.
- Applied `app:tint` for XML-based icon styling.
