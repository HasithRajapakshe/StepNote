StepNote - Walk and Learn
Tagline: Learn something new with every step.

📋 Overview
StepNote is a unique Android application designed to blend physical activity with micro-learning. The core concept is to encourage users to walk more by making that time productive and engaging. The app plays short, user-created educational content—either audio notes or flashcards—but only while the user is actively walking. If the user stops, the content automatically pauses, creating a seamless and motivating learning experience.

This project is perfect for students, professionals, or anyone looking to incorporate learning into their daily routine, turning a simple walk into a productive study session.

✨ Features
User Authentication: Secure local registration and login system using Room database.

Flashcard System: Create, save, and review custom flashcards with a question on the front and an answer on the back.

Audio Notes: Upload and listen to audio notes. The list of saved notes is displayed for easy access.

🚶‍♂️ Step-Based Playback: The app uses the phone's step detector sensor to control audio playback.

Audio automatically plays when the user is walking.

Audio automatically pauses if the user stops walking for a few seconds.

Playback resumes when the user starts walking again.

Step Tracking: The app counts and saves the number of steps taken during learning sessions, displaying the daily total on the home screen.

Fully Local Database: All user data, flashcards, and audio note details are stored securely on the device using the Room Persistence Library.

Intuitive UI: A clean, modern, and easy-to-navigate user interface designed for a great user experience.

🛠️ Tech Stack
Language: Java

IDE: Android Studio

Database: SQLite with Room Persistence Library for local data storage.

Architecture: Activity-based with a focus on background threading for database operations.

UI Components: Material Design components, RecyclerView for lists, CardView for modern UI elements.

Sensors: SensorManager with TYPE_STEP_DETECTOR for activity recognition.

Dependencies:

androidx.room for database management.

de.hdodenhof:circleimageview for circular profile images.

🚀 Setup
To get this project running on your local machine, follow these simple steps:

Clone the repository:

git clone https://github.com/your-username/StepNote.git

Open in Android Studio:

Launch Android Studio.

Select File > Open and navigate to the cloned project folder.

Build the Project:

Android Studio will automatically sync the Gradle files and download the required dependencies.

Once the sync is complete, click the Run 'app' button (green play icon) to build and run the application on an emulator or a connected Android device.

📸 Screenshots
(Here you would add screenshots of your app)

Splash & Auth

Home Screen

Flashcards







Audio Notes

Audio Player

Profile







