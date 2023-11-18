package com.example.Library.models.forRoles;

import com.example.Library.models.forUsers.User;
import com.example.Library.util.roles.ROLE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter @Setter Long id;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private @Getter @Setter ROLE roleName;

    @OneToMany(mappedBy = "role")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private @Getter @Setter List<User> userList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role_name=" + roleName +
                '}';
    }
}
