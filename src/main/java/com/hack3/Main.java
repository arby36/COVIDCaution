package com.hack3;

import java.io.*;
import java.util.*;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        //Database Initialization
        FileInputStream serviceAccount =
                new FileInputStream("key/covidcaution-firebase-adminsdk-e9xkl-8bf10b191d.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://covidcaution.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        //End database initialization






        Scanner in = new Scanner(new File ("main.in"));

        String timePositiveCheckedIn = in.next();
        String timeYouWereThere = in.next();
        String location = in.next();

        in.close();

        String result = "";
        int timePositiveCheckedInInteger = toMinutes(timePositiveCheckedIn);
        int timeYouWereThereInteger = toMinutes(timeYouWereThere);

        if (timePositiveCheckedInInteger == timeYouWereThereInteger) {
            result = "You are in danger of catching COVID-19. You were at " + location + " at the same time as someone " +
                    "who tested positive for the virus. For your information, the time was " + timeYouWereThere + ". " +
                    "Please seek help immediately. If this is not possible, please self-quarantine.";
        }


        PrintWriter out = new PrintWriter(new File("main.out"));
        System.out.println(result);
        out.close();
    }

    private static int toMinutes(String s) {
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        int totalMins = hoursInMins + mins;
        return totalMins;
    }
}
