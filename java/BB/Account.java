package BB;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String username;
    private String phone_number;
    private String image;
    private String password_hash;
    private List<Ticket> tickets;
    private boolean mod;

    public Account(String username, String phone_number, String image, String password, int mod) {
        this.username = username;
        this.phone_number = phone_number;
        this.image = image;
        this.password_hash = generateHash(password);
        this.tickets = new ArrayList<>();
        if (mod == 0) this.mod = false;
        else if (mod == 1) this.mod = true;
    }

    public boolean isModerator() { return this.mod;}
    public void setPasswordHash(String passHash){
        this.password_hash = passHash;
    }

    private String generateHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            String res = hexToString(digest);
            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "Error occurred while generating hash";
        }
    }

    private static String hexToString(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int val = bytes[i];
            val = val & 0xff;  // remove higher bits, sign
            if (val < 16) buff.append('0'); // leading 0
            buff.append(Integer.toString(val, 16));
        }
        return buff.toString();
    }

    public String getUsername() {
        return this.username;
    }

    public List<Ticket> getTickets() {
        return this.tickets;
    }

    public String getPhoneNumber() {
        return this.phone_number;
    }

    public String getImage() {
        return this.image;
    }

    public String getPasswordHash() {
        return this.password_hash;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername.toLowerCase();
    }

    public void changePhoneNumber(String newNumber) {
        this.phone_number = newNumber;
    }

    public void removeImage() {
        this.image = "default.jpg";
    }

    public void changeImage(String newImage) {
        this.image = newImage;
    }

    public boolean checkPassToChange(String oldPass) {
        return this.password_hash.equals(generateHash(oldPass));
    }

    // jsp-shi shevamowmebt da tu false daabruna wina paroli arasworad sheiyvana da ver shecvlis
    public void changePassword(String oldPass, String newPass) {
        if (checkPassToChange(oldPass)) {
            this.password_hash = generateHash(newPass);
        }
    }
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public void removeTicket(String t) {
        for (int i = 0; i < tickets.size(); i++) {
            if (t.equals(tickets.get(i).getTicket_id())) tickets.remove(i);
        }
    }
}
