package com.example.hibnb_project.service;

import com.example.hibnb_project.data.dao.BookDAO;
import com.example.hibnb_project.data.dto.AccomDTO;
import com.example.hibnb_project.data.dto.BookDTO;
import com.example.hibnb_project.data.dto.BookNAccomDTO;
import com.example.hibnb_project.data.dto.ReviewDTO;
import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.BookEntity;
import com.example.hibnb_project.data.entity.ImgEntity;
import com.example.hibnb_project.data.entity.ReviewEntity;
import com.example.hibnb_project.data.repository.AccomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookDAO bookDAO;
    private final AccomRepository accomRepository;

    public List<BookNAccomDTO> findbooksbyUsername(BookDTO bookDTO) {

        List<BookEntity> bookEntities =this.bookDAO.findbooksbyUsername(bookDTO.getUsername());

        List<BookNAccomDTO> bookNAccomDTOList = new ArrayList<>();
        for (BookEntity bookE : bookEntities) {
            AccomEntity accomEntity = this.accomRepository.findById(bookE.getAccomid().getId()).orElse(null);

            Set<ReviewDTO> reviewDTOSet = new HashSet<>();

            Set<ReviewEntity> reviewEntitySet = accomEntity.getReviews();
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
            Set<ReviewEntity> reviewEntityList = accomEntity.getReviews();
            for (ReviewEntity reE : reviewEntityList) {
                avgSum += reE.getRating();
            }
            double avg = reviewEntityList.size() > 0 ? avgSum / reviewEntityList.size() : 0.0;


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
                    .imageUrls(urls)
                    .type(accomEntity.getType())
                    .beds(accomEntity.getBeds())
                    .bedrooms(accomEntity.getBedrooms())
                    .bathrooms(accomEntity.getBathrooms())
                    .imageUrls(!urls.isEmpty() ? urls : null)
                    .maxcapacity(accomEntity.getMaxcapacity())
                    .pricePerNight(accomEntity.getPricePerNight())
                    .average(avg)
                    .reviews(reviewDTOSet)
                    .build();

            BookNAccomDTO bNADTO = BookNAccomDTO.builder()
                    .id(bookE.getId())
                    .username(bookE.getUsername().getUsername())
                    .accomid(bookE.getAccomid().getId())
                    .checkindate(bookE.getCheckindate())
                    .checkoutdate(bookE.getCheckoutdate())
                    .totalPrice(bookE.getTotalPrice())
                    .status(bookE.getStatus())
                    .yesorno(bookE.getYesorno())
                    .payment(bookE.getPayment())
                    .address(bookE.getAccomid().getAddress())
                    .type(bookE.getAccomid().getType())
                    .accom(accomDTO)
                    .build();
            bookNAccomDTOList.add(bNADTO);
        }
        return bookNAccomDTOList;
    }

    public List<BookDTO> findbooksbyHostId(String hostId) {
        Set<BookEntity> bookEntities = this.bookDAO.findbooksbyHostId(hostId);
        List<BookDTO> bookDTOList = new ArrayList<>();
        for (BookEntity bookE : bookEntities) {
            BookDTO bookDTO = BookDTO.builder()
                    .id(bookE.getId())
                    .username(bookE.getUsername().getUsername())
                    .accomid(bookE.getAccomid().getId())
                    .checkindate(bookE.getCheckindate())
                    .checkoutdate(bookE.getCheckoutdate())
                    .totalPrice(bookE.getTotalPrice())
                    .status(bookE.getStatus())
                    .yesorno(bookE.getYesorno())
                    .payment(bookE.getPayment())
                    .address(bookE.getAccomid().getAddress())
                    .type(bookE.getAccomid().getType())
                    .build();
            bookDTOList.add(bookDTO);
        }
        return bookDTOList;
    }

    public String saveBook(BookDTO bookDTO) {
        this.bookDAO.saveBook(bookDTO.getUsername(),bookDTO.getAccomid(),bookDTO.getCheckindate()
                ,bookDTO.getCheckoutdate(),bookDTO.getTotalPrice(), bookDTO.getPerson()
        );
        return "예약 성공";
    }

    public String updateBook(BookDTO bookDTO) {
        this.bookDAO.updateBook(bookDTO.getId(),bookDTO.getUsername(),bookDTO.getAccomid(),bookDTO.getCheckindate()
                ,bookDTO.getCheckoutdate(),bookDTO.getTotalPrice()
        );
        return "업데이트 성공";
    }

    public String cancelBook(BookDTO bookDTO) {
        this.bookDAO.cancelBook(bookDTO.getId(),bookDTO.getUsername(),bookDTO.getAccomid());
        return "예약취소 성공";
    }

    public String payBook(String username, Integer bookid) {
        this.bookDAO.payBook(username,bookid);
        return "업데이트 성공";
    }
}
