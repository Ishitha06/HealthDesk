package healthdesk.e1.model;
import java.time.LocalDateTime;
public class PatientRecord {
    private String patientId;
    private String name;
    private String token;
    private String department;
    private String status;
    private String assignedCounter;
    private LocalDateTime arrivalTime;

   public PatientRecord(String patientId, String name, String token, String department, String status, String assignedCounter, LocalDateTime arrivalTime) {
        if (patientId == null||patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID cannot be null or blank!");
        }
        this.patientId = patientId;
        if (name == null||name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;

        if (token == null||token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or blank!");
        }
        this.token = token;
        if (department == null||department.isBlank()) {
            throw new IllegalArgumentException("Department be null or blank!");
        }
        this.department = department;
        if (status == null||status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be null or blank!");
        }
        this.status =status ;
        if (assignedCounter== null||assignedCounter.isBlank()) {
            throw new IllegalArgumentException("Assigned Counter cannot be null or blank!");
        }
        this.assignedCounter = assignedCounter;
        if (arrivalTime == null) {
            throw new IllegalArgumentException("Arrival Time cannot be null or blank!");
        }
        this.arrivalTime = arrivalTime;}

        public String getPatientID(){
           return this.patientId;
        }
        public String getName(){
        return this.name;
        }
        public String getToken(){return this.token;}
    public  String getDepartment(){return this.department;}
    public String getStatus(){return this.status;}
    public String getAssignedCounter(){return this.assignedCounter;}
    public LocalDateTime getArrivalTime(){return this.arrivalTime;}

public void setName(String name){
    if (name == null||name.isBlank()) {
        throw new IllegalArgumentException("Name cannot be null or blank!");
    }
    this.name = name;
}


public void setStatus(String status){
    if (status == null||status.isBlank()) {
        throw new IllegalArgumentException("Status cannot be null or blank!");
    }
    this.status =status ;
}
public void setAssignedCounter(String assignedCounter){
    if (assignedCounter== null||assignedCounter.isBlank()) {
        throw new IllegalArgumentException("Assigned Counter cannot be null or blank!");
    }
    this.assignedCounter = assignedCounter;
}


}