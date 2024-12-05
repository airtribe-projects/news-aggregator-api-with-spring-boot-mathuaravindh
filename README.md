# News Aggregator API

This is a Spring Boot-based API that aggregates news articles based on user preferences. The application allows users to register, log in, manage their preferences, and fetch news articles based on their preferences.

## Features

- **User Authentication**: Users can register and log in to access personalized news preferences and articles.
- **News Preferences**: Users can set and update their news preferences (e.g., Sports, Technology, Politics, etc.).
- **News Aggregation**: Based on the preferences, the API fetches relevant news articles from various sources.

## Endpoints

### 1. **POST /api/register**
Registers a new user with email, username, and password.

**Request Body**:

```json
{
  "username": "Ceaser",
  "password": "password123",
  "email": "Ceaser@example.com",
   "preferences": ["TECHNOLOGY", "POLITICS"]
}
```

**Response**:

```json
{
    "message": "User registered successfully."
}
```

---

### 2. **POST /api/login**
Authenticates a user and returns a JWT token.

**Request Body**:

```json
{
    "username": "john_doe",
    "password": "password123"
}
```

**Response**:

```json
{
    "token": "your-jwt-token"
}
```

---

### 3. **GET /api/preferences**
Retrieves the current news preferences of the logged-in user. The user must provide a valid JWT token in the `Authorization` header.

**Request Headers**:

```text
Authorization: Bearer <your-jwt-token>
```

**Response**:

```json
{
    "preferences": ["SPORTS", "TECHNOLOGY"]
}
```

---

### 4. **PUT /api/preferences**
Updates the news preferences for the logged-in user. The user must provide a valid JWT token in the `Authorization` header and a list of preferences in the request body.

**Request Headers**:

```text
Authorization: Bearer <your-jwt-token>
```

**Request Body**:

```json
{
    "preferences": ["SPORTS", "POLITICS", "TECHNOLOGY"]
}
```

**Response**:

```json
{
    "message": "Preferences updated successfully."
}
```

---

### 5. **GET /api/news**
Fetches news articles based on the logged-in user's preferences. The user must provide a valid JWT token in the `Authorization` header.

**Request Headers**:

```text
Authorization: Bearer <your-jwt-token>
```

**Response**:

```json
{
    "articles": [
        {
            "title": "Tech Innovations in 2024",
            "description": "A look at the latest tech trends and innovations.",
            "url": "https://news.example.com/tech-innovations-2024"
        },
        {
            "title": "Sports Highlights of the Week",
            "description": "The biggest sports moments from the past week.",
            "url": "https://news.example.com/sports-highlights-week"
        }
    ]
}
```

---

## Authentication

All endpoints, except for `/api/register` and `/api/login`, require the user to be authenticated with a **JWT token**. You can obtain the token by logging in via the `/api/login` endpoint. The token should be included in the `Authorization` header as a Bearer token.

### Example of Authorization Header:

```text
Authorization: Bearer <your-jwt-token>
```

## Preferences

The user can update their preferences to choose topics like:

- SPORTS
- TECHNOLOGY
- POLITICS
- ENTERTAINMENT
- HEALTH
- BUSINESS

These preferences will dictate the news articles retrieved from the `/api/news` endpoint.
Preferences will be autoloaded into the DB from the predefined enum store on application start.

## Running the Application

1. Clone the repository:

   ```bash
   git clone https://github.com/your-repository/news-aggregator-api.git
   ```

2. Navigate to the project directory:

   ```bash
   cd news-aggregator-api
   ```

3. Build the application:

   ```bash
   ./gradlew build
   ```

4. Run the application:

   ```bash
   ./gradlew bootRun
   ```

5. The application will be running on `http://localhost:8080`.

## Testing with Postman or cURL

To interact with the API, you can use tools like **Postman** or **cURL**.

### Example cURL for Login:

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username": "john_doe", "password": "password123"}'
```

This will return the JWT token that you can use for subsequent requests.

---

## Technologies Used

- **Spring Boot**: Backend framework for building the API.
- **Spring Security**: For handling authentication and authorization.
- **JWT (JSON Web Token)**: For stateless authentication.
- **H2 Database**: In-memory database for storing user and preference data.
- **Gradle**: Build automation tool for Java.

---
