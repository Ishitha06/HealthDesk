
package healthdesk.e1.model;

import java.time.LocalDateTime;

public class PatientRecordManualTest {

    public static void main(String[] args) {

        LocalDateTime time = LocalDateTime.now();

        // Test 1: Valid PatientRecord creation
        PatientRecord patient = new PatientRecord(
                "P101",
                "Ananya",
                "T01",
                "Cardiology",
                "WAITING",
                "Counter1",
                time
        );

        System.out.println("Test 1: Valid creation - "
                + (patient.getPatientID().equals("P101") ? "PASS" : "FAIL"));

        // Test 2: Null patient ID
        try {
            new PatientRecord(null, "Ananya", "T01", "Cardiology",
                    "WAITING", "Counter1", time);
            System.out.println("Test 2: Null patient ID - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2: Null patient ID - PASS");
        }

        // Test 3: Blank patient name
        try {
            new PatientRecord("P102", " ", "T02", "Cardiology",
                    "WAITING", "Counter1", time);
            System.out.println("Test 3: Blank name - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 3: Blank name - PASS");
        }

        // Test 4: Null token
        try {
            new PatientRecord("P103", "Rahul", null, "Cardiology",
                    "WAITING", "Counter1", time);
            System.out.println("Test 4: Null token - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4: Null token - PASS");
        }

        // Test 5: Blank department
        try {
            new PatientRecord("P104", "Rahul", "T04", " ",
                    "WAITING", "Counter1", time);
            System.out.println("Test 5: Blank department - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5: Blank department - PASS");
        }

        // Test 6: Null status
        try {
            new PatientRecord("P105", "Rahul", "T05", "Cardiology",
                    null, "Counter1", time);
            System.out.println("Test 6: Null status - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 6: Null status - PASS");
        }

        // Test 7: Blank assigned counter
        try {
            new PatientRecord("P106", "Rahul", "T06", "Cardiology",
                    "WAITING", " ", time);
            System.out.println("Test 7: Blank assigned counter - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 7: Blank assigned counter - PASS");
        }

        // Test 8: Null arrival time
        try {
            new PatientRecord("P107", "Rahul", "T07", "Cardiology",
                    "WAITING", "Counter1", null);
            System.out.println("Test 8: Null arrival time - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 8: Null arrival time - PASS");
        }

        // Test 9: Update patient name
        patient.setName("Ananya Kulkarni");
        System.out.println("Test 9: Update name - "
                + (patient.getName().equals("Ananya Kulkarni") ? "PASS" : "FAIL"));

        // Test 10: Update patient status
        patient.setStatus("IN_PROGRESS");
        System.out.println("Test 10: Update status - "
                + (patient.getStatus().equals("IN_PROGRESS") ? "PASS" : "FAIL"));

        // Test 11: Update assigned counter
        patient.setAssignedCounter("Counter2");
        System.out.println("Test 11: Update assigned counter - "
                + (patient.getAssignedCounter().equals("Counter2") ? "PASS" : "FAIL"));

        // Test 12: Invalid name update
        try {
            patient.setName(" ");
            System.out.println("Test 12: Blank name update - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 12: Blank name update - PASS");
        }

        // Test 13: Invalid status update
        try {
            patient.setStatus(null);
            System.out.println("Test 13: Null status update - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 13: Null status update - PASS");
        }

        // Test 14: Invalid counter update
        try {
            patient.setAssignedCounter(" ");
            System.out.println("Test 14: Blank counter update - FAIL");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 14: Blank counter update - PASS");
        }

        System.out.println("\nManual testing completed.");
    }
}
