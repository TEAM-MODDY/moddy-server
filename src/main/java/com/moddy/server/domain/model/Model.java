package com.moddy.server.domain.model;

import com.moddy.server.domain.user.User;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Model extends User {
    private String year;
    public int getAge() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Integer age = currentDateTime.getYear() - Integer.parseInt(this.year) + 1;
        return age;
    }
}
