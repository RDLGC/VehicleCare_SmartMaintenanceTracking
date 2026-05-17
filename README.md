# VehicleCare - Smart Maintenance Tracking

VehicleCare is a comprehensive Android application designed to help vehicle owners track their maintenance schedules, service history, and vehicle health.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Project Progress Summary

### 1. Navigation & Logic (Latest)
- **Centralized Event Handling**: Established `Events.java` to manage cross-activity interactions, starting with the Login to Create Account transition.
- **Activity Framework**:
    - `MainActivity.java`: Serves as the primary entry point (Login Screen).
    - `CreateAccountActivity.java`: Handles user registration with integrated back navigation.
- **Intent Routing**: Functional navigation between Login and Registration screens.

### 2. UI/UX Implementation
The project features a clean, modern interface using `Material Design 3` components and a consistent `primary_green` branding.

#### Core Modules Implemented:
- **Authentication**:
    - **Login (`login.xml`)**: Welcome screen with email/password authentication and social login options.
    - **Create Account (`create_account.xml`)**: Registration form with Full Name, Email, and Password fields.
- **Vehicle Management**:
    - **Add Vehicle (`add_vehicle.xml`)**: Features type selection (Car, Motorcycle, Truck) using `MaterialCardView`, and detailed input for year, mileage, and model.
- **Maintenance Tracking**:
    - **Add Service Record (`add_service.xml`)**: Comprehensive form to log service type, date, mileage, cost, and provider.
- **Profile & Settings**:
    - UI layouts for **Edit Profile**, **Change Password**, **Email Preferences**, and **Notifications** are established.

### 3. Resource Configuration
- **Color Palette**:
    - `primary_green` (#1B9B65): Primary branding.
    - `light_green` (#D1F5E8): Secondary accent.
    - `facebook_blue` (#1877F2): Social branding.
    - `white`, `light_gray`, `border_gray`: Structural colors.
- **Assets**: 
    - Custom vector library: `ic_car`, `ic_person`, `ic_email`, `ic_lock`, `ic_back`, `ic_google`, `ic_facebook`.
    - Custom drawables for rounded cards, themed buttons, and bordered input fields.

## Technical Standards
- **Material Design**: Extensive use of `TextInputLayout`, `MaterialButton`, and `MaterialCardView`.
- **Accessibility**: Standard inclusion of `contentDescription` for imagery and `autofillHints` for text inputs.
- **Responsiveness**: Use of `NestedScrollView` across all form-heavy layouts to ensure compatibility with various screen sizes and keyboard overlays.
- **Decoupled Logic**: UI event listeners are organized in a dedicated `Events` class to maintain clean activity code.
