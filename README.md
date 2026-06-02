# Tic-Tac-Toe Client

A dynamic, real-time multiplayer Tic-Tac-Toe client application equipped with a modern Graphical User Interface (GUI). This repository contains the client-side logic responsible for rendering game boards, managing user authentication, and communicating with the central game server over secure TCP/IP sockets.

---

## 🚀 Features

- **Authentication & Profiles:** Secure registration and login interfaces seamlessly connected to the server database.
- **Interactive Lobbies:** View active online players, send/receive real-time match invitations, and track live leaderboards.
- **Real-time Gameplay:** Responsive, click-to-play grid featuring smooth turn synchronization, move validations, and game-state announcements (Win/Loss/Tie).
- **Game Recording & Replay:** Locally save matches or stream them back step-by-step to review strategies.
- **Offline Mode:** Play locally against a friend on the same machine or challenge a built-in AI bot when offline.

---

## 📸 Screenshots

### 🔑 Authentication & Navigation
| Login Screen | Main Menu (Guest) | Game Mode Selection |
|:---:|:---:|:---:|
| <img width="250" src="https://github.com/user-attachments/assets/e2f3c943-7299-4ba4-a8b8-5d90dd1b2107" /> | <img width="250" src="https://github.com/user-attachments/assets/4267c8bc-c1ec-471a-b401-c64aa16acb5a" /> | <img width="250" src="https://github.com/user-attachments/assets/27dc1e59-d2d9-4a78-843e-c9e563b21bef" /> |

---

### 🌐 Multiplayer Lobby & Sessions
| Multiplayer Menu | Main Menu (Online) | Online Players List |
|:---:|:---:|:---:|
| <img width="250" src="https://github.com/user-attachments/assets/87b7517f-f25e-4726-82b5-be0cf95c5ce9" /> | <img width="250" src="https://github.com/user-attachments/assets/48c04f4a-755d-4d3b-9af7-6448ebc07fae" /> | <img width="250"  src="https://github.com/user-attachments/assets/0054d40e-1b4b-43c0-8a50-db55ffe4263a" /> |

---

### 🎮 Gameplay Outcomes & Match History
| Game Win Screen | Game Loss Screen | Save Match Dialog | Saved Matches / Replays |
|:---:|:---:|:---:|:---:|
| <img width="180" src="https://github.com/user-attachments/assets/c365ec67-3319-457d-b4b1-ace63829e14c" /> | <img width="180" src="https://github.com/user-attachments/assets/5db71f5a-f9e7-43b2-9c9f-4604f2cce8bd" /> | <img width="180"  src="https://github.com/user-attachments/assets/ba25ee95-3a55-467b-ac52-7e43854cfa85" /> | <img width="180"  src="https://github.com/user-attachments/assets/024e1762-ac41-40c2-bf7b-5c56bd8acae5" /> |

---

## 🏗️ Architecture

The client application isolates its visual elements from networking routines to ensure a smooth, non-blocking UI experience:

* **UI Layer:** Designed with JavaFX (FXML / Controller pattern) for custom button assets, grids, animations, and transitions.
* **Network Listener Thread:** A dedicated background thread that continuously listens for incoming server payloads (e.g., match requests, opponent moves) and updates the UI safely using `Platform.runLater()`.
* **State Manager:** Maintains localized game conditions, tracks active turn sequences, and caches match moves for the replay system.

---

## 🛠️ Tech Stack & Prerequisites

Before setting up the project, ensure you have the following installed:

* **Java Development Kit (JDK):** Version 8 or higher (e.g., JDK 11 / JDK 17)
* **JavaFX SDK:** (If using modular Java 11+)
* **Build Tool:** Maven or Gradle (or standard IntelliJ/Eclipse project configuration)

---

## ⚙️ Getting Started

### Cloning the Repository
```bash
git clone [https://github.com/ITI-Tic-Tac-Toe-Java/ClientGUI.git](https://github.com/ITI-Tic-Tac-Toe-Java/ClientGUI.git)
```
cd ClientGUI
Network Configuration
Before launching, make sure the client points to the correct server IP address and port. Open your network config file (e.g., src/main/resources/network.properties or your hardcoded connection class) and verify:

Properties
server.ip=127.0.0.1
server.port=5005
Building & Running the App
Option 1: Using an IDE (IntelliJ IDEA / NetBeans / Eclipse)
Open the cloned folder as a Maven or Gradle project in your IDE.

Verify that your JDK and JavaFX modules are properly mapped.

Locate the main application entry point (e.g., ClientApp.java).

Run the class.

Option 2: Running via JAR File
If you have a pre-built executable JAR file:

Bash
java -jar ClientApp.jar
🎮 How to Play
Connect: Ensure the Server App is running, then launch the Client.

Access Account: Create a new account or log in with your existing credentials.

Challenge an Opponent: Browse the list of active players in the lobby and click Invite on an available player, or accept an incoming match request.

Win the Game: Place three of your marks (X or O) in a horizontal, vertical, or diagonal row to win!

🤝 Team Members
Developed with ❤️ as part of the ITI Project collaboration.
- Abdullh Gaber
- Elbaraa
- Thaowpsta
- Esraa Ehab
