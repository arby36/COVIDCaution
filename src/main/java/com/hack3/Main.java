package com.hack3;

import java.io.*;
import java.util.*;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.google.firebase.database.*;

public class Main {

    public static class DatabaseTest {
        public String firstname;
        public String lastname;
        public String[] locations;
        public DatabaseTest(String firstname, String lastname, String[] locations) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.locations = locations;
        };
    }

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
        //Database Test//
        Scanner databaseUsernameScanner = new Scanner(System.in);
        System.out.println("Enter the username to be searched:");
        String databaseUsername = databaseUsernameScanner.nextLine();
        System.out.println("Searching for: " + databaseUsername);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/balex");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseTest user = dataSnapshot.getValue(DatabaseTest.class);
                System.out.println(user.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        Scanner confirmScanner = new Scanner(System.in);
        confirmScanner.nextLine();
        //End Database Test//

        //Start Notification Analysis//
        Scanner in = new Scanner(new File ("src/main/java/com/hack3/main.in"));

        String timePositiveCheckedIn = in.next();
        String timeYouWereThere = in.next();
        String dayPositive = in.next();
        String dayYou = in.next();
        String location = in.next();

        in.close();

        int dayPositiveInt = 0;
        int dayYouInt = 0;

        switch(dayPositive) {
            case "Sunday":
                dayPositiveInt = 1;
                break;
            case "Monday":
                dayPositiveInt = 2;
                break;
            case "Tuesday":
                dayPositiveInt = 3;
                break;
            case "Wednesday":
                dayPositiveInt = 4;
                break;
            case "Thursday":
                dayPositiveInt = 5;
                break;
            case "Friday":
                dayPositiveInt = 6;
                break;
            case "Saturday":
                dayPositiveInt = 7;
                break;
        }

        switch(dayYou) {
            case "Sunday":
                dayYouInt = 1;
                break;
            case "Monday":
                dayYouInt = 2;
                break;
            case "Tuesday":
                dayYouInt = 3;
                break;
            case "Wednesday":
                dayYouInt = 4;
                break;
            case "Thursday":
                dayYouInt = 5;
                break;
            case "Friday":
                dayYouInt = 6;
                break;
            case "Saturday":
                dayYouInt = 7;
                break;
        }

        String result = "";
        int timePositiveCheckedInInteger = toMinutes(timePositiveCheckedIn);
        int timeYouWereThereInteger = toMinutes(timeYouWereThere);

        if (Math.abs(dayYouInt - dayPositiveInt) == 1) {
            timeYouWereThereInteger += 1440;
        } else if (Math.abs(dayYouInt - dayPositiveInt) == 2) {
            timeYouWereThereInteger += (1440 * 2);
        } else if (Math.abs(dayYouInt - dayPositiveInt) == 3) {
            timeYouWereThereInteger += (1440 * 3);
        } else if (Math.abs(dayYouInt - dayPositiveInt) == 4) {
            timeYouWereThereInteger += (1440 * 4);
        } else if (Math.abs(dayYouInt - dayPositiveInt) == 5) {
            timeYouWereThereInteger += (1440 * 5);
        } else if (Math.abs(dayYouInt - dayPositiveInt) == 6) {
            timeYouWereThereInteger += (1440 * 6);
        }

        if (timeYouWereThereInteger >= timePositiveCheckedInInteger &&
                timeYouWereThereInteger <= (1440 + timePositiveCheckedInInteger)) {

            result = "You are at EXTREMELY HIGH RISK of having COVID-19. You were at " + location + " within 1 day " +
                    "of someone " +
                    "who tested positive for the virus." + "\n" + "For your information, the time was " + timeYouWereThere +
                    " and the day was " + dayYou + "." +
                    " Please seek help immediately. If this is not possible, please self-quarantine.";

        } else if (timeYouWereThereInteger > (1440 + timePositiveCheckedInInteger) &&
                timeYouWereThereInteger <= (2880 + timePositiveCheckedInInteger)) {

            result = "You are at HIGH RISK of having COVID-19. You were at " + location + " within 1-2 days " +
                    "of someone " +
                    "who tested positive for the virus." + "\n" + "For your information, the time was " + timeYouWereThere +
                    " and the day was " + dayYou + "." +
                    " Please seek help immediately. If this is not possible, please self-quarantine.";

        } else if (timeYouWereThereInteger > (2880 + timePositiveCheckedInInteger) &&
                timeYouWereThereInteger <= (4320 + timePositiveCheckedInInteger)) {

            result = "You are at MEDIUM RISK of having COVID-19. You were at " + location + " within 2-3 days " +
                    "of someone " +
                    "who tested positive for the virus." + "\n" + "For your information, the time was " + timeYouWereThere +
                    " and the day was " + dayYou + "." +
                    " Please seek help immediately. If this is not possible, please self-quarantine.";

        } else if (timeYouWereThereInteger > (4320 + timePositiveCheckedInInteger) &&
                timeYouWereThereInteger <= (10080 + timePositiveCheckedInInteger)) {

            result = "You are at LOW RISK of having COVID-19. You were at " + location + " within 3-7 " +
                    "as someone " +
                    "who tested positive for the virus." + "\n" + "For your information, the time was " + timeYouWereThere +
                    " and the day was " + dayYou + "." +
                    " Please seek help immediately. If this is not possible, please self-quarantine.";

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
        return hoursInMins + mins;
    };
    //End Notification Analysis//
    //Helper Functions//
//    private static String scannerPrompt(String prompt) {
//        Scanner promptScanner = new Scanner(System.in);
//        System.out.println(prompt);
//        String response = promptScanner.nextLine();
//        System.out.println("You entered: " + response);
//        promptScanner.close();
//        return response;
//    };
    //End Helper Functions//
}
