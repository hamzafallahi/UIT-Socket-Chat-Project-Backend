# UIT-Socket-Chat-Project-Backend
StringBoot
# 1. Describe the General Project Idea

The project is a real-time distributed chat system that allows multiple users to communicate over a network.

## It supports:

- Public messaging (chat room)
- Private messaging between users
- File sharing
- User authentication
- Persistent chat history

## Technical Stack & Features

- Backend: Spring Boot
- Frontend: Angular
- Database: MongoDB
- Real-time communication: WebSockets
- API design: JSON:API 1.1 standards with serialization/deserialization
- Data fetching & caching: TanStack Query
- Internationalization: i18n support for multiple languages
- UI: Responsive (desktop and mobile)

> **Goal:** Build a scalable, secure, and modern chat application inspired by real-world messaging systems.

---

# 2. Define the Project Modules

The system is divided into 5 main modules:

1. Authentication Module
2. Chat Module
3. File Management Module
4. User Management Module
5. Admin Module

---

# 3. Define the Module Functionalities

## 1. Authentication Module

- User login
- User registration
- Password encryption (bcrypt)
- JWT-based authentication (or aswar.io — still under consideration)
- Secure API communication following JSON:API format

---

## 2. Chat Module

- Send public messages
- Send private messages
- Real-time message delivery (WebSockets)
- Display chat history
- Show connected users
- Optimized data fetching and caching using TanStack Query
- Support multilingual message display (i18n)

---

## 3. File Management Module

- Upload files
- Store files on server (or cloud later, e.g., Cloudinary)
- Notify users when a file is shared
- Download files
- Validate file size and type

---

## 4. User Management Module

- Create/update/delete users (admin)
- Track online/offline status
- Prevent duplicate login
- User profiles (optional improvement)

---

## 5. Admin Module

- View connected users
- Broadcast messages
- Shut down or manage server
- Monitor system activity (e.g., Umami — optional)
- View logs and usage statistics (e.g., Grafana + Prometheus — optional)

---

# 4. Specify the Project Actors

The system has 2 main actors:

## User
- Any authenticated person using the chat

## Administrator
- The system manager (server/controller side)

---

# 5. Assign Functionalities to Actors

## User

- Register / Login
- Send public messages
- Send private messages
- Upload/download files
- View chat history
- See connected users
- Logout
- Change language (i18n)
- Use the application on different screen sizes (responsive UI)

---

## Administrator

- Manage users (add/remove)
- View all connected clients
- Broadcast messages
- Monitor chat activity
- Manage system (shutdown, logs)

---

# 6. Define the Workflows

## 1. Authentication Workflow
User → enters username/password
→ Backend validates credentials
→ If valid → access granted + token/session created
→ If invalid → error message


---

## 2. Messaging Workflow


User sends message
→ Frontend (Angular)
→ Backend (Spring Boot via WebSocket)
→ Server processes message
→ Broadcast to all users (or specific user for private msg)


---

## 3. File Upload Workflow


User selects file
→ Upload to backend
→ Server stores file
→ Notification sent to other users
→ Users can download file


---

## 4. User Connection Workflow


User logs in
→ Server marks user as "online"
→ Notify other users
→ On logout → mark as "offline"


---

## 5. Chat History Workflow


User connects
→ Backend fetches last messages from MongoDB
→ Sends them to client
→ Client displays history


---

# 7. The GitHub Project Links

## Frontend Repository
https://github.com/hamzafallahi/UIT-Socket-Chat-Project-Frontend

## Backend Repository
https://github.com/hamzafallahi/UIT-Socket-Chat-Project-Backend
