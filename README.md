# Online Help Desk System (OHD)

## Abstract:
The Online Help Desk (OHD) system is a web-based application designed to automate the process of handling service requests within a campus environment. The system enables users such as students and faculty to register, log in, and raise service requests related to various facilities like labs, hostels, and classrooms.
Each request is tracked with different statuses such as unassigned, assigned, in-progress, and closed. The system provides role-based access where administrators can assign requests and monitor their progress.
The application uses Spring Boot for backend development, Thymeleaf for frontend rendering, and MySQL for database management. Email notifications are triggered automatically during request creation and status updates.
Additionally, the system includes reporting features and a scheduler for periodic updates, making it efficient, scalable, and user-friendly



## Features

- 🔐 User Registration & Login
- 🔑 Change Password
- 📋 Create Service Requests
- 📊 Track Request Status (Unassigned → Closed)
- 👨‍💼 Admin Dashboard
- 📌 Assign Requests
- 🔄 Update Request Status
- ❌ Reject Requests
- 📧 Email Notifications
- 📊 Reports Generation
- ⏰ Scheduled Reports
- 🆘 Help Module


## Modules

- Login & Registration
- Requests Module
- Facilities Module
- Request Tracker
- Admin Module
- Reports
- Email Alerts
- Help Module

  ## ER Diagrm
  <img width="538" height="714" alt="Untitled (2)" src="https://github.com/user-attachments/assets/4c790af5-6bba-4b94-b387-83b8cc4d21cb" />



## Technologies Used

### Frontend
- HTML
- CSS
- Thymeleaf

### Backend
- Spring Boot
- Spring MVC

### Database
- MySQL

### Others
- Spring Data JPA
- Hibernate
- JavaMailSender
- Scheduler


## Architecture

The project follows MVC Architecture:

- Controller → Handles requests
- Service → Business logic
- Repository → Database interaction
- Database → MySQL


## Project Workflow

1. User logs in
2. User creates request
3. Request stored in database
4. Email notification sent
5. Admin assigns request
6. Assignee updates status
7. User tracks request
8. Request closed
   <img width="538" height="956" alt="Untitled (1)" src="https://github.com/user-attachments/assets/614a6be8-8c4a-4f98-8b13-bfa4f85d8424" />


## Database Design
<img width="538" height="725" alt="Untitled" src="https://github.com/user-attachments/assets/83ec2909-f76f-4aaf-86d1-0d06ad29b17e" />

###  Users Table
- id
- name
- email
- password
- role

###  Request Table
- id
- title
- description
- priority
- status
- assigned_to
- facility
- close_reason
- severity
- created_by

### Facility Table
- id
- name

##  Relationships

- One User → Many Requests
- One Facility → Many Requests

## Email Feature

- Sent on request creation
- Sent on status updates
- Sent to user and facility head

## Reports

- Manual reports
- Scheduled reports using Scheduler


## Validations

- Login validation
- Input validation
- Role-based access control

## Exception Handling

- Try-Catch blocks
- Error messages displayed properly

## How to Run

1. Clone repository
2. Open in IDE (Eclipse)
3. Configure MySQL in `application.properties`
4. Run Spring Boot Application
5. Open browser → `http://localhost:8083`


## Screenshots
<img width="1891" height="1002" alt="image" src="https://github.com/user-attachments/assets/718055a9-48f7-4ada-881c-41f5311a71e3" />
<img width="1395" height="1017" alt="image" src="https://github.com/user-attachments/assets/9cfa5cfb-a665-4276-9cb2-b523ee4f0303" />



## Conclusion

The Online Help Desk System successfully automates request handling, improves efficiency, and provides a scalable solution for campus service management.


---
