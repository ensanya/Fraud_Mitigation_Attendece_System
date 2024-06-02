package com.example.userinterfaceclientside;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
class Point {
    double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

public class PolygonTest {
    static final double INF = 1000;

    public interface GeofenceCallback {
        void onGeofenceCheckResult(boolean insideGeofence);
    }

    public static void main(final Point userLocation, final GeofenceCallback callback) {
        // Get reference to your Firebase database
        DatabaseReference geofenceRef = FirebaseDatabase.getInstance().getReference("events").child("coordinates");
        System.out.println("polygon");
        // Retrieve geofence data from Firebase
        geofenceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Create a list to store geofence points
                List<Point> geofenceList = new ArrayList<>();

                // Iterate over each geofence coordinate in the dataSnapshot
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the coordinate string from the snapshot
                    String coordinateString = snapshot.getValue(String.class);

                    // Split the coordinate string into latitude and longitude
                    String[] parts = coordinateString.split(",");
                    if (parts.length == 2) {
                        double latitude = Double.parseDouble(parts[0]);
                        double longitude = Double.parseDouble(parts[1]);

                        // Create a Point object and add it to the list
                        Point point = new Point(latitude, longitude);
                        geofenceList.add(point);
                    }
                }

                // Convert the list to an array
                Point[] geofenceArray = geofenceList.toArray(new Point[0]);

                // Check if the user's location is inside any of the geofences
                boolean insideGeofence = isInside(geofenceArray, geofenceArray.length, userLocation);
                // Call the callback method with the geofence check result
                callback.onGeofenceCheckResult(insideGeofence);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }


    static int orientation(Point p, Point q, Point r) {
        double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) {
            return 0; // collinear
        }
        return (val > 0) ? 1 : 2; // clock or counterclockwise
    }

    static boolean onSegment(Point p, Point q, Point r) {
        return (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y));
    }

    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2,  p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        if (o4 == 0 && onSegment(p2, q1, q2)) {
            return true;
        }

        return false;
    }

    static boolean isInside(Point[] polygon, int n, Point p) {
        if (n < 3) {
            return false;
        }

        Point extreme = new Point(INF, p.y);
        int count = 0, i = 0;

        do {
            int next = (i+1)  % n;

            if (doIntersect(polygon[i], polygon[next], p, extreme)) {
                if (orientation(polygon[i], p, polygon[next]) == 0) {
                    return onSegment(polygon[i], p, polygon[next]);
                }
                count++;
            }
            i = next;
        } while (i != 0);

        return (count % 2 == 1);
    }
}
