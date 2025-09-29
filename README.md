# Capitec Transaction Dispute Portal

A web application for managing transaction disputes, built with modern technologies and containerized for easy deployment.

## Architecture Overview

This project consists of several integrated components:

- **Frontend**: React.js application with modern UI/UX
- **Backend**: Spring Boot REST API with comprehensive business logic
- **Database**: PostgreSQL for data persistence
- **Authentication**: Keycloak for authentication
- **Proxy**: Nginx reverse proxy for routing and CORS handling
- **Containerization**: Docker Compose for orchestrated deployment

## Local Setup Guide

### Prerequisites

- **Docker** (version 20.10 or later)
- **Docker Compose** (version 2.0 or later)
- **Git** (to clone the repository)

### 1. Clone the Repository

```bash
git clone https://github.com/matipadeveloper/capitecdisputeportal.git
cd capitec-dispute-portal
```

### 2. Environment Configuration

The project uses environment variables for configuration. Add your variables to a .env file in your docker-compose.yml directory or export environment variables for your credentials
Below are the environment variables needed to be set.

Please note the below are placeholders, replace them with your actual values. (You can find these in the docker-compose.yml file)

```env
# Database Configuration
POSTGRES_DB=<capitec-dispute-database>
POSTGRES_USER=<capitec-dispute-db-admin>
POSTGRES_PASSWORD=<capitec-dispute-db-password>

# Keycloak Configuration
KEYCLOAK_ADMIN=<capitec-dispute-admin>
KEYCLOAK_ADMIN_PASSWORD=<capitec-dispute-password>
KEYCLOAK_REALM=<capitec-dispute-realm>
KEYCLOAK_CLIENT_ID=<capitec-dispute-client> <- this is the client id you will create in keycloak
KEYCLOAK_CLIENT_SECRET=<capitec-dispute-client-secret> <- this is the client secret you will create in keycloak

# Database Schema
KEYCLOAK_DB_SCHEMA=<capitec-dispute-keycloak>
```

### 3. Build and Start the Application

```bash
cd capitec-dispute-portal-infrastructure
docker-compose up --build
```

This command will:
- Build all custom Docker images
- Start all services in the correct order
- Handle dependencies automatically
- Set up the complete application stack


## 4. User Management & Authentication

You will most likely have issues with authentication, to resolve this you will need to create a admin user in keycloak and then create a realm, client and users that will be able to access the portal.

### Default Admin Access

**Keycloak Admin Console**: http://localhost:5050/auth/admin
- Username: `KEYCLOAK_ADMIN` <- this is the username you set in your .env file
- Password: `KEYCLOAK_ADMIN_PASSWORD` <- this is the password you set in your .env file

- Then create a realm that aligns with your KEYCLOAK_REALM name
- Create a client that aligns with KEYCLOAK_CLIENT_ID the KEYCLOAK_CLIENT_SECRET can be pulled from keycloak.
- Create users and assign them to roles as needed, under the realm these users will be able to login to the portal to check there transactions

Then restart the applications with `docker-compose restart` this should be once off just to get your data setup

### 5. Access the Application

Once all services are running, you can access:

- **Web Application**: http://localhost/portal
- **API Documentation (Swagger)**: http://localhost/swagger-ui/
- **Keycloak Admin Console**: http://localhost:5050/auth/admin
- **Direct API Access**: http://localhost/api

### Application Authentication

The application uses Keycloak for authentication with OAuth2/JWT tokens. Users must be created in Keycloak to access the dispute portal.

## Application Features

### Core Functionality

1. **Customer Management**
   - View customer details
   - Create new customers
   - Update customer information

2. **Transaction Management**
   - View all transactions
   - Filter transactions by customer
   - Create new transactions
   - Update transaction details

3. **Dispute Management**
   - Create disputes for transactions (one dispute per transaction)
   - View dispute details with status tracking
   - Update dispute information and status
   - Track dispute status: Open → Pending → Resolved

### API Endpoints

The application provides RESTful APIs for:

- **Authentication**: `/api/auth/*`
- **Customers**: `/api/customers/*`
- **Transactions**: `/api/transactions/*`
- **Disputes**: `/api/disputes/*`

All API endpoints (except authentication) require valid JWT tokens. The JWT can be obtained via the swagger login endpoint.

##  Troubleshooting

### Common Issues

1. **Services won't start**: Check Docker daemon is running, or application logs
2. **Database connection errors**: Verify PostgreSQL container is healthy
3. **Authentication issues**: Ensure Keycloak is properly configured, with correct realm, client, and users
4. **CORS errors**: Verify nginx proxy configuration
