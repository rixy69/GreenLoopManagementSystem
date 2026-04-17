# GreenLoop Management System
Java Swing-based desktop application for GreenLoop to manage eco-friendly packaging products, clients, inventory, orders, delivery assignments, and reports with automated email notifications.

## Prerequisites
- Java 8 or higher
- MySQL 8.0 or higher
- MySQL Connector/J JAR (included in `lib/`)
- A valid JDK on the machine, or `JAVA_HOME` configured

## Setup
1. Install and start MySQL server.
2. Create the database: Run the SQL script `GreenLoop_db.sql` in MySQL Workbench or command line.
3. Ensure the MySQL user has access to the `greenloop` database (default: username `root`, password `root`).
4. Keep the full `GreenLoopSystem` folder together when moving or sharing the project. Do not separate `src`, `resources`, or `lib`.

## How to Run
1. Open Command Prompt in the project root directory (`GreenLoopSystem/`).
2. Run `build.bat` to compile the project.
3. Run `run.bat` to start the application.

**Important:** Always use `run.bat` to launch the app, as it sets the correct classpath including all required JAR files. Running `java Main` directly will fail due to missing dependencies.

If you encounter database connection errors:
- Verify MySQL is running.
- Check that the `greenloop` database exists.
- Ensure the classpath includes `lib/mysql-connector-j-8.2.0.jar`.
- Verify the application database credentials still match your MySQL server settings.

## Database Restore
You can restore the full project database at any time by rerunning:

```sql
source GreenLoop_db.sql;
```

Or from Windows command line:

```bat
mysql -u root -p < GreenLoop_db.sql
```

This script recreates the database from scratch and also inserts sample data for:
- Roles
- Employees
- Customers
- Suppliers
- Parts
- Inventory
- Notifications
- Order types
- Job types
- Orders
- Order items
- Jobs

## Default Login
- Username: `rixy1`
- Password: `Rixy@8248`

## Features
- User authentication
- Dashboard with panels for Customers, Employees, Inventory, Orders, Jobs
- Search functionality in all panels (single search field filters all columns)
- CRUD operations for all entities
- Modern shared UI shell with reusable background, top bar, sidebar, login screen, and footer

## Requirement Notes
The project covers the core workflow from the assignment, but some terms differ from the original document:
- `Customers` are used instead of `Clients`
- `Parts` / `Inventory` are used instead of a dedicated eco-product catalogue
- `Employees` / `Jobs` are used instead of a dedicated delivery-agent and vehicle module

See `PROJECT_DETAILS.txt` for the full requirement mapping and project handoff notes.
