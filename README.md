
# **Fraud Mitigation Attendance System Using Geofence**  
A robust attendance system leveraging **geofencing** and **live location tracking** to prevent fraudulent attendance marking. This project includes:  
- **Android App** for users.  
- **Web App** for administrators.  

---

## **Features**  
- **Geofencing-based attendance verification.**  
- **Live location tracking** of users.  
- **Firebase Realtime Database** integration for attendance records.  
- **Administrator portal** to manage geofences and events.  
- **Simplified latitude and longitude storage** for efficiency.  

---

## **Setup Instructions**  

### **1. Set up Google Maps API Key**  
1. Visit the [Google Cloud Console](https://console.cloud.google.com/).  
2. Create a new project or use an existing one.  
3. Enable the **Maps JavaScript API** and **Geolocation API** for your project.  
4. Generate an API key.  
5. Restrict the API key to your app or domain to ensure security.  

### **2. Configure the Android App**  
1. Clone this repository:  
   ```bash  
   git clone https://github.com/your-username/fraud-mitigation-attendance.git  


Open the project in Android Studio.
Replace the placeholder YOUR_API_KEY in the file where the Google Maps API is used:
html
Copy code
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=drawing"></script>  
Configure the Firebase project in the app:
Go to the Firebase Console.
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
Administrator Role
Creates geofences and events using the web app.
Manages attendance records stored in Firebase.
User Role
Uses the Android app to mark attendance.
The app verifies the user's live location against the geofence.
Geofencing and Attendance
If a user is outside the geofence, they cannot mark attendance.
Attendance is logged in real-time in Firebase.
Challenges and Innovations
Implemented a custom recasting algorithm to compare user location with geofence coordinates.
Optimized coordinate handling by storing them as a single string in the database and splitting them when needed.
Addressed Firebase's asynchronous behavior with callbacks for smooth data handling.
![app4](https://github.com/user-attachments/assets/e7fcfd79-ae43-4978-83d0-cc9cb916bea6)
![app3](https://github.com/user-attachments/assets/2a5940ce-267e-4725-9484-d2c0a9426464)
![app2](https://github.com/user-attachments/assets/c53e387f-0ca8-421b-887c-63c2e9497ebd)
![app1](https://github.com/user-attachments/assets/dffe7a02-e319-4b59-96fa-f001d07dbe9b)
![website4](https://github.com/user-attachments/assets/b478302e-aaae-418c-8794-d81a441b5e98)
![website3](https://github.com/user-attachments/assets/6c0fc7ca-89d6-4482-bb02-337733f66fec)
![website2](https://github.com/user-attachments/assets/0d43a9a7-4783-4278-9e4c-e5b478ea18f3)
![website1](https://github.com/user-attachments/assets/94d9c57b-f3ed-4501-ad06-d51700a8c8f1)
