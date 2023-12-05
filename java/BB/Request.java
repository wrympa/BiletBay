package BB;

public class Request {
    private String sentBy;
    private String sentTo;
    private String ticketId;
    public Request(String sentBy, String sentTo, String ticketId) {
        this.sentBy = sentBy;
        this.sentTo = sentTo;
        this.ticketId = ticketId;
    }

    public String getSentBy() { return sentBy; }
    public String getSentTo() { return sentTo; }
    public String getTicketId() { return ticketId; }
}
