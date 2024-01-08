package com.moddy.server.domain.prefer_hair_style;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PreferHairStyle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
