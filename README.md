## Weather-Forecast-Api

# Introduction
The Weather API project is designed to provide a weather forecast service. Users can retrieve current weather information and forecasts for a specified location by querying the API.
# System Requirements
1. Java 17 or higher
2. MySQL
# Installation
1. Clone the Project
   git clone https://github.com/buidinhnguyen2002/weather-api.git
2. Install Dependencies
   mvn clean install
# Running the Application
 ## 1. Run file src/main/java/com.example.weatherapi/WeatherApiApplication.java
 ## 2. Run via IDE
    Open the project in an IDE like IntelliJ IDE, and select Run to start the application.
# Configuration
   ## Database Configuration
   In src/main/resources/application.properties, configure the database settings as follows:
      spring.datasource.url=jdbc:mysql://localhost:3306/your_database
      spring.datasource.username=your_username
      spring.datasource.password=your_password
### Note: Replace your_database, your_username, and your_password with your actual database details.
# API Endpoints
1. GET /api/v1/weather/forecast?location={city/province} - Get a list of weather forecasts for that province/city
2. GET /api/v1/weather/more-forecast?location={city/province}&date={date} - Get a list of weather forecasts for the next days of the province/city
3. POST /api/v1/weather/register-notification?email={email}&location={city/province} - Sign up for weather alerts via email and subscription confirmation email
4. GET /api/v1/weather/confirm?token={token} - Email verification
5. POST /api/v1/weather/unsubscribe?email={email} - Unsubscribe from notifications
# Other features
1. Send daily weather information at 5am to registered users' emails
2. Temporarily store weather information to reload during the day
# References
   - Spring Boot Documentation
   - Maven Documentation
   - MySQL Documentation
