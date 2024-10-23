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
- **Responsive Design**: Mobile-friendly UI built with Bootstrap for a seamless user experience across devices.
- **Data Validation**: Implemented input validation to ensure data integrity and improve user experience.

## Technologies Used

### IDEs Used:
- **IntelliJ**: Used for the Java development due to phenomenal utility and support for Java.
- **VS Code**: Used for HTML development due to better support and utility for HTML files.

### Backend:
- **Spring Boot**: Provides a simplified framework for creating production-ready, enterprise-level applications.
- **Spring Data JPA**: Handles ORM and simplifies database access with repositories and entity management.
- **Spring Security**: Ensures authentication and role-based access control.
- **Spring Validation**: Handles data validation.
- **Lombok**: Reduces boilerplate code with annotations like `@Getter`, `@Setter`, `@NoArgsConstructor`, and `@AllArgsConstructor` to manage data model classes more efficiently.
- **MySQL**: Primary database for data persistence.
- **Hibernate**: ORM used for mapping.

### Frontend:
- **Thymeleaf**: Template engine for rendering dynamic content in HTML views.
- **Bootstrap**: Provides responsive design elements for a modern and mobile-friendly UI.
- **Note**: HTML and CSS is probably not the greatest I learned as I went and am still learning. Project is more focused on the backend.

### Security:
- **BCrypt**: Used for password hashing to enhance security.
- **Spring Security**: Configured for user authentication and authorization.

## Project Structure
- **Controllers**: Manage HTTP requests and responses, directing traffic between the UI and services.
- **Services**: Contain the core business logic, ensuring the application functions as intended.
- **Repositories**: Interface with the database to handle data operations using Spring Data JPA.
- **Entities**: Define the application's data models (e.g., `User`, `Task`, `Role`).
- **DTOs**: Data Transfer Objects to ensure secure and efficient data transfer between layers and avoid exposing sensitive information.

## Layout Note
The page layout and navigation bar were initially provided by a Bootstrap template, which streamlined the design process and facilitated the creation of a responsive user interface. Several customizations have been made to enhance the layout, including modifications to the color scheme, spacing, and component styles, ensuring the design aligns with the specific requirements of this application.

## Getting Started
To explore the features of UTracker, visit the registration page to create an account and log in to your dashboard. Here, you can manage your tasks and take advantage of the application's features.

## Future Enhancements
- Implement user notifications for task reminders.
- Integrate a calendar view for better task visualization.
- Add functionality for recurring tasks.
- Add JavaScript features when I learn it.
- -Add sorting and pagination for tasks and users.
- And more when thought of.

## Acknowledgements
- Thanks to the open-source community for the libraries and tools that facilitated this project.
- Inspired by the necessity for effective personal and team task management.
