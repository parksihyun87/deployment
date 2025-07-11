package com.example.hibnb_project.service;


import com.example.hibnb_project.data.dao.AccomDAO;
import com.example.hibnb_project.data.dao.BookDAO;
import com.example.hibnb_project.data.dto.AccomDTO;
import com.example.hibnb_project.data.dto.AccomSeachDTO;
import com.example.hibnb_project.data.dto.BookDTO;
import com.example.hibnb_project.data.dto.ReviewDTO;
import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.BookEntity;
import com.example.hibnb_project.data.entity.ImgEntity;
import com.example.hibnb_project.data.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AccomService {
    private final AccomDAO accomDAO;

    public List<AccomDTO> findAllAccoms() {
        List<AccomEntity> accomEntityList = this.accomDAO.findAllAccoms();
        List<AccomDTO> accomDTOList = new ArrayList<>();
        for (AccomEntity accomE : accomEntityList) {

            double avgSum = 0;
            Set<ReviewEntity> reviewEntityList = accomE.getReviews();
            for (ReviewEntity reE : reviewEntityList) {
                avgSum += reE.getRating();
            }
            double avg = reviewEntityList.size() > 0 ? avgSum / reviewEntityList.size() : 0.0;

            Set<ReviewEntity> review = accomE.getReviews();
            List<String> imageUrlList = new ArrayList<>();


            if (accomE.getImg() != null && accomE.getImg().getImg1() != null) {
                imageUrlList.add(accomE.getImg().getImg1());
            }

            List<LocalDate> earliestCheckin = new ArrayList<>();
            List<LocalDate> earliestCheckout = new ArrayList<>();

            if (accomE.getBooks() != null && !accomE.getBooks().isEmpty()) {
                earliestCheckin = accomE.getBooks().stream()
                        .map(BookEntity::getCheckindate)
                        .distinct() // 중복 제거
                        .sorted()   // 날짜순 정렬
                        .collect(Collectors.toList());

                earliestCheckout = accomE.getBooks().stream()
                        .map(BookEntity::getCheckoutdate)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList());
            }

            AccomDTO accomDTO = AccomDTO.builder()
                    .id(accomE.getId())
                    .hostid((accomE.getHostid()).getUsername())
                    .hostname(accomE.getHostname())
                    .address(accomE.getAddress())
                    .detailaddr(accomE.getDetailaddr())
                    .description(accomE.getDescription())
                    .type(accomE.getType())
                    .imageUrl(accomE.getImageUrl())
                    .imageUrls(imageUrlList)
                    .average(avg)
                    .maxcapacity(accomE.getMaxcapacity())
                    .pricePerNight(accomE.getPricePerNight())
                    .bedrooms(accomE.getBedrooms())
                    .beds(accomE.getBeds())
                    .bathrooms(accomE.getBathrooms())
                    .latitude(accomE.getLatitude())
                    .longitude(accomE.getLongitude())
                    .checkinDates(earliestCheckin)
                    .checkoutDates(earliestCheckout)
                    .build();
            accomDTOList.add(accomDTO);
        }
        return accomDTOList;
    }

    public List<AccomDTO> findDetailedAccom(AccomSeachDTO accomSeachDTO) {
        List<AccomEntity> accomEntityList = this.accomDAO.findDetailedAccom(accomSeachDTO.getAddress(),
                accomSeachDTO.getCheckindate(), accomSeachDTO.getCheckoutdate(), accomSeachDTO.getMaxcapacity());

        List<AccomDTO> accomDTOList = new ArrayList<>();

        for (AccomEntity accomE : accomEntityList) {
            Set<ReviewDTO> reviewDTOSet = new HashSet<>();

            Set<ReviewEntity> reviewEntitySet = accomE.getReviews();
            for (ReviewEntity reE : reviewEntitySet) {
                ReviewDTO reviewDTO = ReviewDTO.builder()
                        .id(reE.getId())
                        .accomid(reE.getAccomid().getId())
                        .bookid(reE.getBookid().getId())
                        .username(reE.getUsername().getUsername())
                        .rating(reE.getRating())
                        .comment(reE.getComment())
                        .createdAt(reE.getCreatedAt())
                        .build();
                reviewDTOSet.add(reviewDTO);
            }

            double avgSum = 0;
            Set<ReviewEntity> reviewEntityList = accomE.getReviews();
            for (ReviewEntity reE : reviewEntityList) {
                avgSum += reE.getRating();
            }
            double avg = reviewEntityList.size() > 0 ? avgSum / reviewEntityList.size() : 0.0;

            ImgEntity imgEntity = accomE.getImg();

            List<String> imageUrls = new ArrayList<>();

            imageUrls = Stream.of(imgEntity.getImg1(), imgEntity.getImg2(), imgEntity.getImg3(), imgEntity.getImg4())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());


            AccomDTO accomDTO = AccomDTO.builder()
                    .id(accomE.getId())
                    .hostid((accomE.getHostid()).getUsername())
                    .hostname(accomE.getHostname())
                    .address(accomE.getAddress())
                    .detailaddr(accomE.getDetailaddr())
                    .description(accomE.getDescription())
                    .type(accomE.getType())
                    .imageUrl(accomE.getImageUrl())
                    .average(avg)
                    .maxcapacity(accomE.getMaxcapacity())
                    .pricePerNight(accomE.getPricePerNight())
                    .bedrooms(accomE.getBedrooms())
                    .beds(accomE.getBeds())
                    .bathrooms(accomE.getBathrooms())
                    .imageUrls(!imageUrls.isEmpty() ? imageUrls : null)
                    .reviews(reviewDTOSet)
                    .latitude(accomE.getLatitude())
                    .longitude(accomE.getLongitude())
                    .build();
            accomDTOList.add(accomDTO);
        }
        return accomDTOList;
    }

    public AccomDTO findById(Integer id) {
        AccomEntity entity = accomDAO.findById(id);
        if (entity == null) {
            return null;
        }
        List<String> imageUrls = new ArrayList<>();

//        imageUrls = Stream.of(imgEntity.getImg1(), imgEntity.getImg2(), imgEntity.getImg3(), imgEntity.getImg4())
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
        // Entity -> DTO 변환 (예: ModelMapper 사용하거나 수동 변환)
        AccomDTO dto = new AccomDTO();
        dto.setId(entity.getId());
        dto.setHostid(entity.getHostid().getUsername());  // hostid가 UserEntity인 경우
        dto.setHostname(entity.getHostid().getName());
        dto.setAddress(entity.getAddress());
        dto.setDetailaddr(entity.getDetailaddr());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setImageUrl(entity.getImageUrl());
        dto.setMaxcapacity(entity.getMaxcapacity());
        dto.setPricePerNight(entity.getPricePerNight());
        dto.setBedrooms(entity.getBedrooms());
        dto.setBeds(entity.getBeds());
        dto.setBathrooms(entity.getBathrooms());
        dto.setAverage(entity.getAverage());
//        dto.setImageUrls(entity.getImageUrls());
        // 필요하면 리뷰, 체크인 날짜 등도 변환 추가

        return dto;
    }

    public List<AccomDTO> findByHostid(String hostid) {
        List<AccomEntity> accomEntityList = this.accomDAO.findByHostid(hostid);
        if(accomEntityList == null || accomEntityList.size() == 0) {
            return null;
        }
        List<AccomDTO> accomDTOList = new ArrayList<>();
        for (AccomEntity accomE : accomEntityList) {
            ImgEntity imgEntity = accomE.getImg();
            List<String> urls = new ArrayList<>();
            if (imgEntity != null) {
                urls.add(imgEntity.getImg1());
                urls.add(imgEntity.getImg2());
                urls.add(imgEntity.getImg3());
                urls.add(imgEntity.getImg4());
                urls = urls.stream()
                        .filter(item -> item != null)
                        .collect(Collectors.toList());
            }

            AccomDTO accomDTO = AccomDTO.builder()
                    .id(accomE.getId())
                    .hostid((accomE.getHostid()).getUsername())
                    .hostname(accomE.getHostname())
                    .address(accomE.getAddress())
                    .detailaddr(accomE.getDetailaddr())
                    .description(accomE.getDescription())
                    .imageUrls(urls)
                    .type(accomE.getType())
                    .beds(accomE.getBeds())
                    .bedrooms(accomE.getBedrooms())
                    .bathrooms(accomE.getBathrooms())
                    .imageUrls(!urls.isEmpty() ? urls : null)
                    .maxcapacity(accomE.getMaxcapacity())
                    .pricePerNight(accomE.getPricePerNight())
                    .latitude(accomE.getLatitude())
                    .longitude(accomE.getLongitude())
                    .build();
            accomDTOList.add(accomDTO);
        }
        return accomDTOList;
    }

    public List<AccomDTO> findByBookTop5() {
        List<BookEntity> books = this.accomDAO.findTop5Books();
        List<AccomDTO> accomDTOList = new ArrayList<>();
        for (BookEntity bookEntity : books) {
            AccomEntity accomEntity = bookEntity.getAccomid();
            ImgEntity imgEntity = accomEntity.getImg();
            List<String> urls = new ArrayList<>();
            if (imgEntity != null) {
                urls.add(imgEntity.getImg1());
                urls.add(imgEntity.getImg2());
                urls.add(imgEntity.getImg3());
                urls.add(imgEntity.getImg4());
                urls = urls.stream()
                        .filter(item -> item != null)
                        .collect(Collectors.toList());
            }
            AccomDTO accomDTO = AccomDTO.builder()
                    .id(accomEntity.getId())
                    .hostid((accomEntity.getHostid()).getUsername())
                    .hostname(accomEntity.getHostname())
                    .address(accomEntity.getAddress())
                    .detailaddr(accomEntity.getDetailaddr())
                    .description(accomEntity.getDescription())
                    .type(accomEntity.getType())
                    .beds(accomEntity.getBeds())
                    .bedrooms(accomEntity.getBedrooms())
                    .bathrooms(accomEntity.getBathrooms())
                    .imageUrls(!urls.isEmpty() ? urls : null)
                    .maxcapacity(accomEntity.getMaxcapacity())
                    .pricePerNight(accomEntity.getPricePerNight())
                    .longitude(accomEntity.getLongitude())
                    .latitude(accomEntity.getLatitude())
                    .build();
            accomDTOList.add(accomDTO);
        }
        return accomDTOList;
    }


    public String saveAccom(AccomDTO accomDTO) throws IOException {
        this.accomDAO.saveAccom(accomDTO.getHostid(), accomDTO.getHostname(),
                accomDTO.getAddress(), accomDTO.getDetailaddr(), accomDTO.getDescription(), accomDTO.getType(),
                accomDTO.getImageUrl(), accomDTO.getMaxcapacity(), accomDTO.getPricePerNight(), accomDTO.getBedrooms(), accomDTO.getBeds(), accomDTO.getBathrooms(),
                accomDTO.getImages()
        );
        return "숙소 등록 성공";
    }

    public String updateAccom(AccomDTO accomDTO) throws IOException {
        this.accomDAO.updateAccom(accomDTO.getId(), accomDTO.getHostid(), accomDTO.getHostname(),
                accomDTO.getAddress(), accomDTO.getDetailaddr(), accomDTO.getDescription(), accomDTO.getType(),
                 accomDTO.getMaxcapacity(), accomDTO.getPricePerNight(), accomDTO.getBedrooms(), accomDTO.getBeds(), accomDTO.getBathrooms(),
                accomDTO.getImages(), accomDTO.getUrlsToDelete()
        );
        return "숙소 수정 성공";
    }

    public String deleteAccom(AccomDTO accomDTO) {
        this.accomDAO.deleteAccom((accomDTO.getId()), accomDTO.getHostid());
        return "숙소 삭제 성공";
    }

}
