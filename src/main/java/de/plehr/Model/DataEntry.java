package de.plehr.Model;
 
import java.sql.Timestamp;

import javax.persistence.*;
 
@Entity
@Table(name = "entry")
public class DataEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String source;

    private String topic;

    private String value;
    
    private Timestamp timestamp;
}