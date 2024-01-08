package com.moddy.server.domain.day_off;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class DayOff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
