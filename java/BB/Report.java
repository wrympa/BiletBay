package BB;

public class Report {
    private String reported;
    private String reporter;
    private String reason;
    public String getReported() {
        return reported;
    }
    public String getReporter() {
        return reporter;
    }
    public String getReason() {
        return reason;
    }
    public Report(String reporter, String reported, String reason){
        this.reason = reason;
        this.reported = reported;
        this.reporter = reporter;
    }
}
