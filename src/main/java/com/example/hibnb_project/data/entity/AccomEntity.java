package com.example.hibnb_project.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accom")
public class AccomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accomid", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hostid", nullable = false)
    private UserEntity hostid;

    @Size(max = 10)
    @NotNull
    @Column(name = "hostname", nullable = false, length = 10)
    private String hostname;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 255)
    @NotNull
    @Column(name = "detailaddr", nullable = false)
    private String detailaddr;

    @NotNull
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Size(max = 10)
    @NotNull
    @Column(name = "type", nullable = false, length = 10)
    private String type;

    @Size(max = 255)
    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull
    @Column(name = "max_capacity", nullable = false)
    private Integer maxcapacity;

    @NotNull
    @Column(name = "price_per_night", nullable = false)
    private Integer pricePerNight;

    @Column(name = "bedrooms")
    private Integer bedrooms;

    @Column(name = "beds")
    private Integer beds;

    @Column(name = "bathrooms")
    private Integer bathrooms;

    @Column(name = "average")
    private Double average;

    @OneToMany(mappedBy = "accomid")
    private Set<BookEntity> books = new LinkedHashSet<>();

    @OneToOne(mappedBy = "accom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private ImgEntity img;

    @OneToMany(mappedBy = "accomid")
    private Set<ReportEntity> reports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "accomid")
    private Set<ReviewEntity> reviews = new LinkedHashSet<>();

    public void setImg(ImgEntity img) {
        this.img = img;
        img.setAccom(this);
    }
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

}