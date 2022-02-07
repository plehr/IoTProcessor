package de.plehr.Model;

public class MqttUser {

    private int acc;
    private String clientid;
    private String topic;
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Set username of the user and validate the format of username.
     * @param username set username of the user
     * @throws IllegalArgumentException if username is not valid
     */
    public void setUsername(String username) throws IllegalArgumentException {
        if (!username.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$") && !username.contains("_adm"))
            throw new IllegalArgumentException("Invalid MAC address");
        this.username = username;
    }

    /**
     * Check if the user has admin rights.
     * @return true if user is adminisrator
     */
    public boolean isAdmin() {
        return username.contains("_adm");
    }

/**
 * It is allowed to write to the topic if the user is an admin user or would write to the own topic.
 * ACL-Representation: 1 read, 2 write, 3 read and write, 4 subscribe
 * @return true if user is permitted to write to the topic
 */
    public boolean canWrite() {
        if (isAdmin())
            return true;
        if (topic.startsWith(username) && acc == 2)
            return true;
        return false;
    }
/**
 * Generate a fancy string representation of the object.
 */
    @Override
    public String toString() {
        return username + " " + topic + " " + acc;
    }

}