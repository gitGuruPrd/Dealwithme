# 🏠 Deal With Me 
## 📌 Overview
The **Deal With Me** is a full-featured real estate platform built with **Spring boot**.  It allows users to list, edit, search, and express interest in properties while ensuring security through **JWT-based authentication**. The system integrates PostgreSQL, handles image uploads, email and supports pagination for property listings.
## 🚀 Features
✅ **JWT Authentication** – Secure endpoints with JSON Web Tokens  
✅ **Property Management** – Create, update, and fetch property listings  
✅ **Image Upload Support** – Upload property images with `MultipartFile`  
✅ **Interest Expression** – Users can express interest in a property  
✅ **Filtering & Pagination** – Search by location, price, and type  
✅ **Integration Testing** – Uses **Testcontainers** with PostgreSQL  
✅ **Unit Testing** – Uses **JUnit and Mockito** framework

---

## 🛠️ Tech Stack
- **Spring Boot** – Backend framework
- **Spring Security** – JWT-based authentication
- **Java Mail Sender** – Email based verification
- **Testcontainers** – Database testing
- **MockMvc & JUnit 5** – API integration testing

---
## 🏗️ Installation & Setup

### 1️⃣ Clone the Repository
```sh
git clone https://github.com/gitGuruPrd/dealwithme.git
cd dealwithme
