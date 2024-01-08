package com.moddy.server.domain.prefer_offer_condition;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PreferOfferCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
