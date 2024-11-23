Fraud Mitigation Attendance System Using Geofence
A robust attendance system leveraging geofencing and live location tracking to prevent fraudulent attendance marking. This project includes an Android app for users and a web app for administrators.

Features
Geofencing-based attendance verification.
Live location tracking of users.
Firebase Realtime Database integration for attendance records.
Administrator portal to manage geofences and events.
Simplified latitude and longitude storage for efficiency.
Setup Instructions
1. Set up Google Maps API Key
Visit the Google Cloud Console.
Create a new project or use an existing one.
Enable the Maps JavaScript API and Geolocation API for your project.
Generate an API key.
Restrict the API key to your app or domain to ensure security.
2. Configure the Android App
Clone this repository:
bash
Copy code
git clone https://github.com/your-username/fraud-mitigation-attendance.git
Open the project in Android Studio.
Replace the placeholder YOUR_API_KEY in the file where the Google Maps API is used:
html
Copy code
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=drawing"></script>
Configure the Firebase project in the app:
Go to Firebase Console.
Create a new Firebase project or use an existing one.
Add your Android app to the Firebase project by following the steps provided in the console.
Download the google-services.json file and place it in the /app directory of your Android project.
Firebase Database Structure
1. Users
Stores user data like personal information and live location (latitude and longitude).
Indexed by user ID (or employee ID).
2. Events
Stores geofence coordinates, event close time, and date.
How It Works
Administrator Role:

Creates geofences and events using the web app.
Manages attendance records stored in Firebase.
User Role:

Uses the Android app to mark attendance.
The app verifies the user's live location against the geofence.
Geofencing and Attendance:

If a user is outside the geofence, they cannot mark attendance.
Attendance is logged in real-time in Firebase.
Challenges and Innovations
Implemented a custom recasting algorithm to compare user location with geofence coordinates.
Optimized coordinate handling by storing them as a single string in the database and splitting them when needed.
Addressed Firebase's asynchronous behavior with callbacks for smooth data handling.
