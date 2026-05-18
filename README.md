# VehicleCare - Smart Maintenance Tracking

VehicleCare is a comprehensive Android application designed to help vehicle owners track their maintenance schedules, service history, and vehicle health.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Project Progress Summary

### 1. Navigation & Logic (Latest)
- **Vehicle Management UI Integration**:
    - **Created `AddVehicleActivity.java`**: Implemented full logic for the vehicle addition screen, including interactive type selection (Car, Motorcycle, Truck) with visual feedback.
    - **MVVM Binding**: Connected the UI to `VehicleViewModel` to support robust validation, asynchronous adding, and automatic synchronization with Supabase.
    - **Repository Enhancement**: Added `addSuccessLiveData` to accurately signal UI transitions upon successful data persistence.
    - **Integrity**: Enforced vehicle type constraints and mandatory name validation.
- **Data Persistence & Networking**: 
    - **Room Database**: Implemented `VehicleEntity` and `VehicleDao` for local storage.
    - **Supabase API**: Established endpoints for vehicle management.
- **Authentication**: Full integration of Firebase Auth and Supabase Profiles with multi-cloud sync.

### 2. UI/UX Implementation
Modern interface using `Material Design 3` and `primary_green` branding.

#### Core Modules Implemented:
- **Authentication**: Login and Create Account screens.
- **Vehicle Management**: 
    - **Add Vehicle**: Interactive form with data validation and cloud sync.
- **Maintenance Tracking**: Comprehensive service record logging form.

### 3. Resource Configuration
- **Cloud Config**: Integration with Firebase and Supabase.

## System Architecture
- **Presentation**: MVVM + LiveData + XML.
- **Domain**: Repository pattern for Auth and Vehicles.
- **Data**: Room (Local), Supabase (Remote), Firebase (Auth & Messaging).

## Technical Standards
- **Sync Strategy**: Offline-first with background remote synchronization.
- **Visual Feedback**: Dynamic UI state updates for selection and loading.
