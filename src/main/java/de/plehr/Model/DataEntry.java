package de.plehr.Model;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "entry")
public class DataEntry {
    public DataEntry(String source, String topic, String value, Timestamp timestamp) {
        setSource(source);
        setTopic(topic);
        setValue(value);
        setTimestamp(timestamp);
    }

    public DataEntry() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String source;

    private String topic;

    private String value;

    private Timestamp timestamp;

    public Integer getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}