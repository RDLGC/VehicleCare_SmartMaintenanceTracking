# VehicleCare - Smart Maintenance Tracking

VehicleCare is a comprehensive Android application designed to help vehicle owners track their maintenance schedules, service history, and vehicle health.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Project Progress Summary

### 1. Navigation & Logic (Latest)
- **Vehicle Management Implementation**:
    - **Data Layer**: Implemented `VehicleEntity`, `VehicleDao`, and expanded `AppDatabase` for local vehicle storage.
    - **Remote Sync**: Updated `SupabaseApi` with endpoints for adding and fetching vehicles.
    - **Business Logic**: Created `VehicleRepository` and `VehicleViewModel` to manage the lifecycle of vehicle data, ensuring offline-first availability with remote synchronization.
    - **Integrity**: Enforced vehicle type constraints ('Car', 'Motorcycle', 'Truck') at both the database (SQL CHECK) and application levels.
- **Authentication Flow**: 
    - Full integration of **Firebase Auth** and **Supabase Profiles**.
    - Robust sync logic with detailed error reporting and RLS compatibility.
- **Centralized Event Handling**: Decoupled UI and logic using the `Events` class.

### 2. UI/UX Implementation
Modern interface using `Material Design 3` and `primary_green` branding.

#### Core Modules Implemented:
- **Authentication**: Login and Create Account screens.
- **Vehicle Management**: Add Vehicle interface with type-specific card selection.
- **Maintenance Tracking**: Comprehensive service record logging form.

### 3. Resource Configuration
- **Cloud Config**: Placeholder system for Supabase URL/Key and Firebase `google-services.json` integration.

## System Architecture
- **Presentation**: MVVM + LiveData + XML.
- **Domain**: Repository pattern for Auth and Vehicles.
- **Data**: Room (Local), Supabase (Remote), Firebase (Auth & Messaging).

## Technical Standards
- **Sync Strategy**: Array-based JSON inserts for Supabase compatibility.
- **Security**: Row Level Security (RLS) integration for profile and vehicle data.
- **Responsiveness**: `NestedScrollView` implementation across all forms.
