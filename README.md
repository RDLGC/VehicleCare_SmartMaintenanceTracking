# VehicleCare - Smart Maintenance Tracking

This project is an Android application for tracking vehicle maintenance.

## Recent Updates

### UI Implementation: Login Screen
The login interface has been fully implemented in `login.xml` based on the provided design specifications.

#### 1. Color Palette Configuration
Updated `res/values/colors.xml` with the following colors:
- `primary_green` (#1B9B65): Main branding color.
- `facebook_blue` (#1877F2): Official Facebook brand color.
- `text_gray`, `border_gray`, `light_gray`: Various shades for UI hierarchy and borders.

#### 2. Resource Development
Created several custom drawable resources to match the design:
- **Backgrounds**:
    - `bg_rounded_top.xml`: White card background with 32dp top-rounded corners.
    - `bg_logo_container.xml`: Rounded background for the app logo.
    - `bg_edittext.xml`: Outlined background for input fields and buttons.
    - `bg_button_primary.xml` & `bg_button_facebook.xml`: Themed button backgrounds.
- **Icons (Vector Drawables)**:
    - `ic_car.xml`: App logo icon.
    - `ic_email.xml` & `ic_lock.xml`: Input field decorators.
    - `ic_google.xml` & `ic_facebook.xml`: Social login brand icons.

#### 3. String Resources
Updated `res/values/strings.xml` with all necessary UI text, including placeholders, labels, and social login prompts.

#### 4. Layout Implementation (`login.xml`)
- **Root**: `ConstraintLayout` with a primary green background.
- **Header**: Contains the app logo, "VehicleCare" title, and slogan.
- **Form Card**: A `NestedScrollView` containing a white card that covers 70% of the screen.
    - **Welcome Text**: "Welcome Back" header.
    - **Input Fields**: Custom-styled email and password fields with icons.
    - **Action Buttons**: "Sign In" primary button, social login buttons (Google & Facebook).
    - **Footer**: "Don't have an account? Sign Up" redirection.

### Documentation
- **README.md**: Created this documentation file to track project progress, resource changes, and UI implementations.

## Accessibility & Best Practices
- Added `contentDescription` for all image resources.
- Configured `autofillHints` for email and password fields.
- Used `NestedScrollView` to ensure the form is usable on smaller screens or when the keyboard is visible.
- Applied `app:tint` to icons to ensure consistent coloring via XML.
