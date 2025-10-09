# Clixify - URL Shortener with Analytics

[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-61dafb)](https://reactjs.org/)
[![Docker](https://img.shields.io/badge/Docker-✓-2496ED)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Clixify is a full-stack URL shortener application with comprehensive analytics, built with Spring Boot and React, containerized with Docker for easy deployment.

## ✨ Features

### Backend (Completed ✅)
- JWT-based authentication
- URL shortening with unique code generation
- Click analytics with date range filtering
- User-specific URL management
- RESTful API with proper documentation

### Frontend (Planned 🚧)
- Modern, responsive UI with React
- User dashboard for URL management
- Interactive analytics charts
- Intuitive URL creation interface

### Deployment
- Docker containerization
- Easy deployment with Docker Compose
- Environment-based configuration

## 🚀 Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.5
- **Security**: Spring Security 6.x with JWT
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Java**: 17

### Frontend (Coming Soon)
- **Framework**: React 18
- **State Management**: Redux Toolkit
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios

### DevOps
- **Containerization**: Docker
- **Orchestration**: Docker Compose
- **CI/CD**: GitHub Actions (Planned)

## 📂 Project Structure

```
Clixify/
├── clixify-backend-sb/        # Spring Boot backend
│   ├── src/                   # Source code
│   └── Dockerfile             # Backend Dockerfile
│
├── clixify-frontend-react/    # React frontend (Coming Soon)
│   ├── public/                # Static files
│   ├── src/                   # Source code
│   └── Dockerfile             # Frontend Dockerfile
│
├── docker-compose.yml         # Docker Compose configuration
├── .env.example              # Environment variables example
└── README.md                 # This file
```

## 🛠 Installation

### Prerequisites
- Java 17+
- Node.js 16+ (for frontend)
- Docker & Docker Compose
- Maven 3.6.3+ (for local development)

### Local Development Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/clixify.git
   cd clixify
   ```

2. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

3. **Start with Docker Compose**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
   - Frontend: http://localhost:3000 (Coming Soon)
   - Backend API: http://localhost:8080
   - API Docs: http://localhost:8080/swagger-ui.html

## 🌐 API Documentation

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Authenticate and get JWT token

### URL Management
- `POST /api/urls/shorten` - Create short URL
- `GET /api/urls` - Get user's URLs
- `GET /{shortUrl}` - Redirect to original URL

### Analytics
- `GET /api/urls/analytics/{shortUrl}` - Get URL analytics
- `GET /api/urls/totalClicks` - Get total clicks

## 🚀 Deployment

### Production Deployment with Docker

1. **Build and start containers**
   ```bash
   docker-compose -f docker-compose.prod.yml up --build -d
   ```

2. **View logs**
   ```bash
   docker-compose logs -f
   ```

### Environment Variables

Create a `.env` file in the root directory with the following variables:

```env
# Backend
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/clixify
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_secure_password
JWT_SECRET=your_jwt_secret_key

# Frontend (Coming Soon)
REACT_APP_API_URL=http://localhost:8080
```

## 🛡️ Security

- JWT-based authentication
- Password hashing with BCrypt
- CSRF protection
- Secure HTTP headers
- Input validation
- Rate limiting (Planned)

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with ❤️ using Spring Boot and React
- Inspired by popular URL shorteners like Bitly and TinyURL
- Icons by [Shields.io](https://shields.io/)
