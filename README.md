
UTracker - Task Manager Application

Project Overview
UTracker is a robust web-based task management application designed to help users organize their tasks effectively. Built with Spring Boot, Thymeleaf, and Bootstrap, UTracker provides a user-friendly interface for creating, updating, and managing tasks. This project showcases my ability to develop full-stack applications using modern web technologies.

Note: UTracker is a work in progress, and I plan to add more features and improvements in the future.

Key Features
- User Registration & Authentication:
  - Secure user registration and login functionality using Spring Security with BCrypt password hashing.
- Task Management:
  - Create tasks with details such as name, start date, due date, and status.
  - Update existing tasks or mark them as complete.
  - Delete tasks that are no longer needed.
- Role-Based Access Control:
  - User roles (User, Admin) ensure appropriate access to features and data.
  - Admins can manage all users and their tasks, while regular users can only manage their own tasks.
- Responsive Design:
  - Mobile-friendly UI built with Bootstrap for a seamless user experience across devices.
- Data Validation:
  - Input validation to ensure data integrity and enhance user experience.

Technologies Used
IDEs
- IntelliJ: For Java development, leveraging its excellent utility and support for Spring Boot.
- VS Code: For HTML and CSS development, providing better support for web technologies.

Backend
- Spring Boot: Simplifies the creation of production-ready applications with minimal configuration.
- Spring Data JPA: Manages ORM and database access through repositories.
- Spring Security: Ensures secure authentication and role-based access control.
- Spring Validation: Provides robust data validation mechanisms.
- Lombok: Reduces boilerplate code in data model classes with annotations like @Getter, @Setter, and @NoArgsConstructor.
- MySQL: Primary database for data persistence.
- Hibernate: Facilitates ORM mapping for database interactions.

Frontend
- Thymeleaf: Template engine for rendering dynamic HTML content.
- Bootstrap: Utilizes responsive design elements for a modern, mobile-friendly UI.
  Note: HTML and CSS are areas I’m actively learning, so design may evolve over time.

Security
- BCrypt: Utilized for secure password hashing.
- Spring Security: Configured to manage user authentication and authorization effectively.

Project Structure
- Controllers: Handle HTTP requests and responses, routing between the UI and services.
- Services: Encapsulate core business logic to ensure the application operates as intended.
- Repositories: Interface with the database for data operations using Spring Data JPA.
- Entities: Define application data models (e.g., User, Task, Role).
- DTOs: Data Transfer Objects that secure and optimize data transfer between application layers.

Docker Support
UTracker includes a Docker setup for easy deployment and development. You can build and run the application using Docker Compose. The Docker configuration includes separate Dockerfiles for PC and Raspberry Pi versions.

Docker Support for PC
- Dockerfile: This file defines how to build the Docker image for the UTracker application. It uses the OpenJDK 21 slim image, sets the working directory, and specifies the entry point to run the application.
- docker-compose.pc.yml: This Docker Compose file orchestrates the application and its dependencies (like the MySQL database) for PC environments.

Docker Support for Raspberry Pi
- Dockerfile.rpi: This file is similar to the PC version, but it's optimized for Raspberry Pi environments, ensuring compatibility with ARM architecture.
- docker-compose.rpi.yml: This Docker Compose file is configured specifically for Raspberry Pi environments and includes the necessary adjustments for running on ARM-based devices.

Running the Application with Docker
To run UTracker with Docker on either a PC or Raspberry Pi, follow these steps:

1. Clone the repository to your local machine (or Raspberry Pi).
2. Choose the appropriate Docker Compose file based on your system:
   - For PC: docker-compose -f docker-compose.pc.yml up --build
   - For Raspberry Pi: docker-compose -f docker-compose.rpi.yml up --build
3. Once built, access the application in your web browser at http://localhost:8080.

Default Credentials
- Admin Account:
  - Username: admin
  - Password: password
- Initial User Account:
  - Username: jsmith
  - Password: test123

Layout Note
The initial page layout and navigation bar were created using a Bootstrap template, allowing for a responsive design. Customizations have been made to the color scheme, spacing, and component styles to meet the application’s specific requirements.

Getting Started
To explore UTracker, visit the registration page to create an account and log in to your dashboard. From there, you can manage your tasks and utilize the application's features.

Future Enhancements
- Implement user notifications for task reminders.
- Integrate a calendar view for enhanced task visualization.
- Add functionality for recurring tasks.
- Incorporate JavaScript features as I learn more.
- Implement sorting and pagination for tasks and users.
- Additional features as ideas arise.

Acknowledgements
Thanks to the open-source community for the libraries and tools that have facilitated this project.  
Inspired by the necessity for effective personal and team task management.
