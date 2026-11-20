package healthdesk.e1.model;

public class QueueRecord {
    private String queueId;
    private String department;
    private int servingToken;
    private int lastIssuedToken;
    private int waitingCount;
    private int activeCounters;
    public QueueRecord(String queueId,String department,int servingToken,int lastIssuedToken,int waitingCount,int activeCounters){
        this.queueId=queueId;
        this.department=department;
        if(servingToken<0){
            throw new IllegalArgumentException("Serving token cannot be negative");
        }
        this.servingToken=servingToken;
        if(lastIssuedToken<0){
            throw new IllegalArgumentException("Last issued token cannot be negative");
        }
        if(lastIssuedToken<servingToken){
            throw new IllegalArgumentException("Last issued token cannot be smaller than the serving token");
        }
        this.lastIssuedToken=lastIssuedToken;
        if(waitingCount<0){
            throw new IllegalArgumentException("Waiting count cannot be negative");
        }
        this.waitingCount=waitingCount;
        if(activeCounters<0){
            throw new IllegalArgumentException("Active counters cannot be negative");
        }
        this.activeCounters=activeCounters;




    }
    public String getQueueId(){
        return queueId;
    }
    public String getDepartment(){
        return department;
    }
    public int getServingToken(){
        return servingToken;
    }
    public int getLastIssuedToken(){
        return lastIssuedToken;
    }
    public int getWaitingCount(){
        return waitingCount;
    }
    public int getActiveCounters(){
        return activeCounters;
    }

    public void setServingToken(int servingToken){
        if(servingToken<0){
            throw new IllegalArgumentException("Serving token cannot be negative");
        }
        if (servingToken > this.lastIssuedToken) {
            throw new IllegalArgumentException(
                    "Serving token cannot exceed last issued token"
            );
        }
        this.servingToken=servingToken;
    }
    public void setLastIssuedToken(int lastIssuedToken){
        if(lastIssuedToken<0){
            throw new IllegalArgumentException("Last issued token cannot be negative");
        }
        if (lastIssuedToken < this.servingToken) {
            throw new IllegalArgumentException(
                    "Last issued token cannot be smaller than serving token"
            );
        }
        this.lastIssuedToken=lastIssuedToken;
    }
    public void setWaitingCount(int waitingCount){
        if(waitingCount<0){
            throw new IllegalArgumentException("Waiting count cannot be negative");
        }
        this.waitingCount=waitingCount;
    }
    public void setActiveCounters(int activeCounters){
        if(activeCounters<0){
        throw new IllegalArgumentException("Active counters cannot be negative");
    }
    this.activeCounters=activeCounters;
}
}
