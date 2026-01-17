package com.happydieting.dev.model;

import com.happydieting.dev.listener.MediaAuditListener;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = false, callSuper = false)
@EntityListeners({MediaAuditListener.class})
@Entity
@Table(name = "MEDIA", indexes = {@Index(name = "codemedia_index", columnList = "code", unique = true)})
public class MediaModel extends ItemModel{

    @Column(unique = true, nullable = false)
    private String code;

    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] content;

    private String contentType;
    private String fileName;
    private String mediaUrl;
    private String filePath;

    @Embedded
    private MediaOwner owner;

}
