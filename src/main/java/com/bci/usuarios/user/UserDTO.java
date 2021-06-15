package com.bci.usuarios.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastlogin;
    private String token;
    private Boolean isactive;

}
