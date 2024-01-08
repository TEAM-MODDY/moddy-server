package com.moddy.server.domain.model;

import com.moddy.server.domain.user.User;
import jakarta.persistence.Entity;

@Entity
public class Model extends User {
    private String year;
}
