package com.happydieting.dev.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class MediaOwner {

    @Column(name = "owner_id")
    private Long id;

    @Column(name = "owner_type")
    private String type;

    public MediaOwner() {
    }

    public MediaOwner(Long id, String type) {
        this.id = id;
        this.type = type;
    }
}
