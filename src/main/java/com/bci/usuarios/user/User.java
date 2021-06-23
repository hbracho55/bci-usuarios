package com.bci.usuarios.user;

import com.bci.usuarios.phone.Phone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column
    private LocalDateTime modified;

    @Column
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Boolean isactive=false;

    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    private List<Phone> phones;

    public void addPhone(Phone phone){
        if (!this.phones.contains(phone)){
            this.phones.add(phone);
            phone.setUser(this);
        }
    }

    public void removePhone(Phone phone){
        if (this.phones.contains(phone)){
            this.phones.remove(phone);
            phone.setUser(null);
        }
    }

    public User(String name,
                String email,
                String password,
                LocalDateTime created,
                LocalDateTime modified,
                LocalDateTime lastLogin,
                String token,
                Boolean isactive,
                List<Phone> phones) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isactive = isactive;
        this.phones = phones;
    }

}
