# VehicleCare - Smart Maintenance Tracking

VehicleCare is a comprehensive Android application designed to help vehicle owners track their maintenance schedules, service history, and vehicle health.

## Project Maintenance Rules
- **Automatic Documentation**: The AI assistant is instructed to automatically update this `README.md` file after every task to reflect the latest changes and project state without being prompted by the user.

## Project Progress Summary

### 1. Navigation & Logic (Latest)
- **Proactive Maintenance Reminders (Phase 2)**:
    - **Smart Alerts**: Implemented `ReminderEntity`, `ReminderDao`, and updated `AppDatabase` (v3) to support custom mileage and date-based notifications.
    - **Cloud Sync**: Updated `SupabaseApi` with endpoints for managing reminders and established the `ReminderRepository` for cross-platform data integrity.
    - **Push Notifications**: Integrated **Firebase Cloud Messaging (FCM)**. The app now automatically captures the device's FCM token upon login/signup and syncs it to the Supabase profile for remote alerting.
    - **FCM Service**: Created `FCMService.java` to handle background data messages and display high-priority maintenance notifications.
    - **UI Integration**: Completed `SetReminderActivity.java` with a vehicle selection dropdown and interactive reminder-type selector.
- **Service History Module**: Full tracking of maintenance events with aggregate cost analysis and vehicle-specific logging.
- **Vehicle Management Dashboard**: Dynamic garage management with interactive type selection and background sync.

### 2. UI/UX Implementation
Modern interface using `Material Design 3` and `primary_green` branding.

#### Core Modules Implemented:
- **Smart Features**: "Set Reminder" interface with dynamic input handling.
- **Service Tracking**: aggregate history and per-vehicle dashboard stats.
- **Vehicle Management**: Dynamic RecyclerView-based garage list.

### 3. Resource Configuration
- **Cloud Infrastructure**: Full integration with Firebase (Auth/Messaging) and Supabase (PostgreSQL).

## System Architecture
- **Presentation**: MVVM + LiveData + RecyclerView.
- **Domain**: Repository pattern for Auth, Vehicles, Services, and Reminders.
- **Data**: Room (Local), Supabase (Remote), Firebase (Auth & FCM).

## Technical Standards
- **Relational Integrity**: Foreign key constraints with cascade delete for services and reminders.
- **Proactive Sync**: Real-time FCM token management to enable server-side maintenance triggers.
- **Performance**: Asynchronous data handling via Single-thread executors and Retrofit callbacks.
