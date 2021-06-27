package com.platformbuilders.clients.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Entity
@Table(name = "client")
@Data
public class Client {

    @Builder
    public Client(@NonNull String name, @NonNull LocalDate dateOfBirth, @NonNull String email) {
        this.oid = UUID.randomUUID().toString();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }

    @Id
    @ApiModelProperty(hidden = true)
    @Column(name = "oid", nullable = false)
    private String oid;

    @Column(name = "name", nullable = false)
    private String name;

    @Transient
    @ApiModelProperty(hidden = true)
    private String age;

    @Column(name = "date_birth", columnDefinition = "DATE", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "email", nullable = false)
    @Email(message = "Email should be valid")
    private String email;

    public Client() {
        this.oid = UUID.randomUUID().toString();
    }

    public String getAge() {
        int years = Period.between(this.dateOfBirth, LocalDate.now()).getYears();
        if (years > 0)
            return years + " year" + getPluralCharacter(years);

        int months = Period.between(this.dateOfBirth, LocalDate.now()).getMonths();
        if (months > 0)
            return months + " month" + getPluralCharacter(months);

        int days = Period.between(this.dateOfBirth, LocalDate.now()).getDays();
        if (days < 0)
            return "this client is not born yet";

        return days + " day" + getPluralCharacter(days);
    }

    private String getPluralCharacter(int number) {
        return (number > 1 ? "s" : "");
    }

    @Override
    public String toString() {
        return "Client{" +
                "oid=" + oid +
                ", name='" + name + '\'' +
                ", age=" + getAge() +
                ", dateOfBirth=" + dateOfBirth +
                ", email='" + email + '\'' +
                '}';
    }
}
