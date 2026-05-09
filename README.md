# VehicleCare - Smart Maintenance Tracking

This project is an Android application for tracking vehicle maintenance.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Recent Updates

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
- **Form Card**: A white card containing:
    - **Full Name**: Input field with person icon.
    - **Email**: Input field with email icon.
    - **Password & Confirm Password**: Two password fields with lock icons.
    - **Primary Action**: A "Create Account" button using the primary green theme.
- **UX**: Wrapped in `NestedScrollView` for better accessibility and keyboard handling.

---

### UI Implementation: Login Screen
The login interface has been fully implemented in `login.xml` based on the design specifications.

#### 1. Color Palette Configuration
Updated `res/values/colors.xml` with the following colors:
- `primary_green` (#1B9B65): Main branding color.
- `facebook_blue` (#1877F2): Official Facebook brand color.
- `text_gray`, `border_gray`, `light_gray`: Various shades for UI hierarchy and borders.

#### 2. Resource Development
Created several custom drawable resources:
- **Backgrounds**: `bg_rounded_top.xml`, `bg_logo_container.xml`, `bg_edittext.xml`, `bg_button_primary.xml`, `bg_button_facebook.xml`.
- **Icons**: `ic_car.xml`, `ic_email.xml`, `ic_lock.xml`, `ic_google.xml`, `ic_facebook.xml`.

#### 3. Layout Implementation (`login.xml`)
- **Root**: `ConstraintLayout` with a primary green background.
- **Header**: Contains the app logo, "VehicleCare" title, and slogan.
- **Form Card**: A `NestedScrollView` containing a white card that covers 70% of the screen.
    - **Action Buttons**: "Sign In" primary button, social login buttons (Google & Facebook).

## Accessibility & Best Practices
- Added `contentDescription` for all image resources.
- Configured `autofillHints` for all relevant input fields (name, email, password).
- Used `NestedScrollView` to ensure responsiveness.
- Applied `app:tint` for XML-based icon styling.
