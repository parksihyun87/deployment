package com.example.hibnb_project.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "img")
public class ImgEntity {
    @Id
    @Column(name = "accomid", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accomid", nullable = false)
    private AccomEntity accom;

    @Size(max = 255)
    @NotNull
    @Column(name = "img1", nullable = false)
    private String img1;

    @Size(max = 255)
    @Column(name = "img2")
    private String img2;

    @Size(max = 255)
    @Column(name = "img3")
    private String img3;

    @Size(max = 255)
    @Column(name = "img4")
    private String img4;

}