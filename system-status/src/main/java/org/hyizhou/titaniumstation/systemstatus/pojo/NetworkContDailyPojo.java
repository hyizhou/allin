package org.hyizhou.titaniumstation.systemstatus.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author hyizhou
 * @date 2024/1/18
 */
@Data
@Entity
@Table(name = "netcontdaily", schema = "titanium")
public class NetworkContDailyPojo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "interface_name")
    private String interfaceName;
    @Basic
    @Column(name = "period_start")
    private Time periodStart;
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
    @Column(name = "date")
    private Date date;

}
