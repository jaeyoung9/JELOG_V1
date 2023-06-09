package jelog.server.main.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * TimeDate : 2023-07-03 21:01
 *
 * */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class DN_UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int DN_UID;
    private String DA_SignID;
    private String DN_Passwd;
    private String DN_Salt;
}
