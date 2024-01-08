package com.moddy.server.domain.har_service_offer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HairServiceOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
