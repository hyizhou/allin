package org.hyizhou.titaniumstation.systemstatus.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
@Data
@Entity
@Table(name = "netconthourly", schema = "titanium")
public class NetworkContHourlyPojo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "interface_name")
    private String interfaceName;
    @Basic
    @Column(name = "bytes_received")
    private Long bytesReceived;
    @Basic
    @Column(name = "bytes_sent")
    private Long bytesSent;
    @Basic
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Basic
    @Column(name = "hour")
    private Timestamp hour;
}
