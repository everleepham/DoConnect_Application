
# DoConnect - Doctor Appointment System

DoConnect is a web application for booking doctor appointments, where users can log in, select doctors, choose services (online consultation or in-cabin), and schedule appointments. The system integrates email notifications and stores appointment data in a PostgreSQL database.

## Features

- **Patient Management**: Create, update, and manage patient details.
- **Doctor Search**: Find doctors by specialty, by name
- **Appointment Scheduling**: Schedule, find, and update appointments.
- **Appointment Search**: Search appointments by patient, doctor, or date.
- **Email Notifications**: Clients receive an email confirmation upon booking an appointment.

## Architecture

The system is built using **Spring Boot** and **Maven** with a **PostgreSQL** database. The backend is divided into four modules:

- **Patients**: Stores patient information (ID, first name, last name, email, password).
- **Doctors**: Stores doctor details (ID, first name, last name, email, specialization).
- **Appointments**: Manages appointment records (ID, patient ID, doctor ID, service, appointment date/time).

## Technologies

- **Spring Boot**: Framework for backend development.
- **Maven**: Build tool for dependency management.
- **PostgreSQL**: Database for data storage.
- **JUnit & Mockito**: Unit testing and mocking for test cases.

## Setup

1. Clone the repository.
2. Configure your PostgreSQL database and update connection details in `application.yml`.
3. Build and run the application using Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Access the app at `http://localhost:8080`.

## Test Cases

Test cases for services such as creating patients, finding doctors, and managing appointments are written using **JUnit** and **Mockito** to ensure functionality.
