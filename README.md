# VehicleCare - Smart Maintenance Tracking

VehicleCare is a comprehensive Android application designed to help vehicle owners track their maintenance schedules, service history, and vehicle health.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Project Progress Summary

### 1. Navigation & Logic (Latest)
- **Service History Module Implementation**:
    - **Data Layer**: Created `ServiceEntity` and `ServiceDao` for robust tracking of maintenance events (Type, Date, Mileage, Cost, Provider).
    - **Database Update**: Expanded `AppDatabase` to include the `services` table with support for total cost aggregation.
    - **Cloud Synchronization**: Updated `SupabaseApi` with specialized endpoints for global service fetching and targeted record insertion.
    - **MVVM Integration**: Developed `ServiceRepository` and `ServiceViewModel` to coordinate between local persistence and the remote Supabase database.
- **Vehicle Management Dashboard**:
    - Full `AddVehicleActivity` integration with interactive type selection.
    - `ManageVehiclesActivity` with dynamic `RecyclerView` and automated background sync.
- **Authentication**: Firebase Auth and Supabase Profiles fully integrated with automated cross-cloud profile creation.

### 2. UI/UX Implementation
Modern interface using `Material Design 3` and `primary_green` branding.

#### Core Modules Implemented:
- **Service Tracking**: Backend logic ready for aggregate history and per-vehicle filtering.
- **Vehicle Management**: Dynamic garage list with offline support.
- **Authentication**: Login and Account Creation flows.

### 3. Resource Configuration
- **Cloud Infrastructure**: Integration with Firebase and Supabase.

## System Architecture
- **Presentation**: MVVM + LiveData + RecyclerView.
- **Domain**: Repository pattern for Auth, Vehicles, and Services.
- **Data**: Room (Local), Supabase (Remote), Firebase (Auth).

## Technical Standards
- **Relational Integrity**: Cascade delete support for services when vehicles are removed.
- **Financial Tracking**: `DECIMAL` support for accurate cost logging.
- **Performance**: Single-thread executor for background DB operations.
