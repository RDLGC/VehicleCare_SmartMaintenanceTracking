# VehicleCare - Smart Maintenance Tracking

VehicleCare is a comprehensive Android application designed to help vehicle owners track their maintenance schedules, service history, and vehicle health.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Project Progress Summary

### 1. Navigation & Logic (Latest)
- **Manage Vehicles Dashboard Implementation**:
    - **UI Framework**: Created `ManageVehiclesActivity.java` to display the user's garage.
    - **Dynamic List**: Implemented `VehicleAdapter` and `item_vehicle.xml` to show vehicle names, types, and mileage with type-specific icons.
    - **Data Integration**: Connected the list to `VehicleViewModel`, supporting real-time updates from Room and cloud synchronization from Supabase.
    - **UX Features**: Added loading states (ProgressBar), empty state handling ("No vehicles added yet"), and dual entry points for adding vehicles (Header icon and FAB).
- **Vehicle Management**:
    - Full "Add Vehicle" flow with interactive type selection and multi-cloud persistence.
- **Authentication**: Firebase Auth and Supabase Profiles integration with multi-cloud sync.

### 2. UI/UX Implementation
Modern interface using `Material Design 3` and `primary_green` branding.

#### Core Modules Implemented:
- **Authentication**: Login and Create Account screens.
- **Vehicle Management**: 
    - **Manage Vehicles**: Dynamic RecyclerView-based list with offline support.
    - **Add Vehicle**: Interactive form with data validation.

### 3. Resource Configuration
- **Cloud Config**: Integration with Firebase and Supabase.

## System Architecture
- **Presentation**: MVVM + LiveData + RecyclerView.
- **Domain**: Repository pattern for Auth and Vehicles.
- **Data**: Room (Local), Supabase (Remote), Firebase (Auth & Messaging).

## Technical Standards
- **Sync Strategy**: Offline-first with background remote synchronization.
- **Data Integrity**: Enforced vehicle type constraints and unique user/name mapping.
