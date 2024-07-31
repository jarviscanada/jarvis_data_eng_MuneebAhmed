# Introduction
This project is an Angular-based trading application designed to provide users with a seamless and interactive experience for managing their trading accounts. The application allows users to view their trading dashboard, manage traders, and perform various trading operations such as deposits and withdrawals. The primary users of this application are traders who need a robust and user-friendly interface to manage their trading activities. The project leverages modern web technologies including Angular for the frontend, Spring Boot for the backend, and Docker for containerization. The frontend is built using Angular CLI version 12, and it incorporates Angular Material for UI components and styling.


# Quick Start

To get started with the project, follow these steps:

1. Ensure Docker is installed and running:
    ```sh
    docker --version
    ```

2. Clone the repository:
    ```sh
    git clone https://github.com/jarviscanada/jarvis_data_eng_MuneebAhmed/angular-frontend.git
    cd angular-trading-app
    ```

3. Build and run the backend Docker image:
    ```sh
    docker-compose build
    docker-compose up
    ```

4. Navigate to the frontend directory:
    ```sh
    cd angular-frontend
    ```

5. Install Node.js and npm if not already installed:
    ```sh
    node --version
    npm --version
    ```

6. Install project dependencies:
    ```sh
    npm install
    ```

7. Start the frontend application:
    ```sh
    npm start
    ```

8. Open your browser and navigate to `http://localhost:4200` to access the application.

# Implemenation
The project is implemented using a microservices architecture with a clear separation between the frontend and backend services. The backend is a Spring Boot application that provides RESTful APIs for managing traders and their accounts. The frontend is an Angular application that consumes these APIs and provides a rich user interface for interacting with the trading data.

## Architecture

The application architecture consists of the following components:
- **Frontend**: Angular application served by Nginx
- **Backend**: Spring Boot application
- **Database**: H2 in-memory database for development

# Test
The application has been tested using Postman for API testing, google dev tools for browser testing and manual testing.

# Deployment
The application is deployed using Docker. Both the frontend and backend services are containerized and managed using Docker Compose. This ensures that the application can be easily deployed and scaled across different environments.

# Improvements
There are several areas for improvement in the application:
- **Responsive UI**: Enhance the UI to be fully responsive and provide a better experience on mobile devices.
- **Validations**: Add comprehensive form validations to ensure data integrity and improve user experience.
- **Error Handling**: Implement better error handling mechanisms to provide more informative feedback to the users.