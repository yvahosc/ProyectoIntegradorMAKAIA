package org.makaia.transactionBankingSystem.model;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "person")
@Schema(title = "Person: Entidad para el almacenamiento de información de " +
        "una persona en el sistema, contiene la información completa de la " +
        "persona.")
public class Person {

    @Id
    @Column(name = "id", length = 10)
    private String id;
    @Column(name = "firstName", nullable = false, length = 100)
    private String firstName;
    @Column(name = "lastName", nullable = false, length = 100)
    private String lastName;
    @Column(name = "phone", nullable = false, length = 14)
    private String phone;
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "person" , cascade = CascadeType.ALL)
    private List<Account> accounts;


    public Person() {
    }

    public Person(String id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
