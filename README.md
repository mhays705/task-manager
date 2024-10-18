# UTracker - Task Manager Application

## Project Overview
UTracker is a robust web-based task management application designed to help users organize their tasks effectively. Built with Spring Boot, Thymeleaf, and Bootstrap, UTracker provides a user-friendly interface for creating, updating, and managing tasks. This project demonstrates my ability to develop full-stack applications using modern web technologies.

**Note**: UTracker is still a work in progress, and I plan to add more features and improvements in the future.


## Key Features
- **User Registration & Authentication**: Secure user registration and login functionality using Spring Security and BCrypt password hashing.
- **Task Management**: 
  - Create new tasks with details such as name, start date, due date, and status.
  - Update existing tasks or mark them as complete.
  - Delete tasks when no longer needed.
- **Role-Based Access Control**: 
  - User roles (User, Admin) ensure appropriate access to features and data.
  - Admins can manage all users and their tasks, while regular users can manage their own tasks.
- **Responsive Design**: Mobile-friendly UI built with Bootstrap for seamless user experience across devices.
- **Data Validation**: Implemented input validation to ensure data integrity and improve user experience.

## Technologies Used
- **Backend**: 
  - Spring Boot for RESTful services and server-side logic.
  - Spring Data JPA for seamless database interactions.
  - MySQL or H2 (for testing) as the database.
- **Frontend**: 
  - Thymeleaf for dynamic HTML rendering.
  - Bootstrap for responsive and modern UI design.
- **Security**: 
  - Spring Security for user authentication and authorization.

## Project Structure
- **Controllers**: Manage HTTP requests and responses, directing traffic between the UI and services.
- **Services**: Contain the core business logic, ensuring the application functions as intended.
- **Repositories**: Interface with the database to handle data operations.
- **Entities**: Define the application's data models (e.g., User, Task, Role).
- **DTOs**: Ensure secure data transfer and prevent exposing sensitive information.

## Layout Note
The page layout and navigation bar were provided by a Bootstrap template, which helped to streamline the design process and create a responsive user interface.

## Getting Started
To explore the features of UTracker, visit the registration page to create an account and log in to your dashboard. Here, you can manage your tasks and take advantage of the application's features.

## Future Enhancements
- Implement user notifications for task reminders.
- Integrate a calendar view for better task visualization.
- Add functionality for recurring tasks.

## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgements
- Thanks to the open-source community for the libraries and tools that facilitated this project.
- Inspired by the necessity for effective personal and team task management.
