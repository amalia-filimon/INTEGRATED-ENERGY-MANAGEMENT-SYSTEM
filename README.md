# Integrated Energy Management System

A distributed energy management platform designed to monitor, manage, and interact with smart energy devices and users in real time. The system provides secure user authentication via JWT, device control, real-time data monitoring, alerts, and WebSocket-based communication, all deployed via Docker for a complete infrastructure setup.

---

## 🚀 Overview

The system is composed of multiple independent microservices that work together to support key operations for an energy management application. Each service is deployed in a containerized environment to ensure scalability, maintainability, and security.

---

## 🌐 Tech Stack

* **Backend:** Spring Boot, Spring Security, JWT, WebSockets, REST API, RabbitMQ
* **Frontend:** React
* **Database:** MySQL
* **DevOps:** Docker

---

## 📁 Project Structure

```
integrated-energy-management-system/
├── chat_service/                       # Real-time user-admin communication via WebSockets
├── device_management_service/         # Device CRUD and user-device association
├── user_management_service/           # User accounts, JWT authentication, security config
├── monitoring_communication_service/  # Real-time monitoring, WebSocket alerts
├── smart_metering_device_simulator/   # Simulates energy consumption data via RabbitMQ
├── frontend/                           # React-based user interface
├── sensor.csv                          # Input data for simulation
└── README.md
```

---

## 🔧 Features

* **JWT-Based User Authentication:** Register, authenticate, and authorize users (admin/client) securely
* **Role-Based Access Control:** Implemented via Spring Security configuration
* **Device Management:** Admin can add, assign, and manage smart devices for clients
* **Monitoring & Alerts:** Track hourly energy consumption and alert users via WebSockets
* **Real-Time Chat:** WebSocket-based chat between users and administrators with token-secured access
* **Data Simulation:** Devices simulate consumption data and send it via RabbitMQ

---

## 🐳 Deployment

All components are containerized using Docker. Services are configured to communicate within an isolated network.

```bash
# Start all services
docker-compose up --build
```

---

## 🔐 Security

* **Spring Security** with custom JWT authentication and filters
* Stateless API access with **Bearer tokens**
* WebSocket secured via token handshake

---

