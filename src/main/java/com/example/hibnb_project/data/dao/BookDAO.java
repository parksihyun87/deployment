package com.example.hibnb_project.data.dao;

import com.example.hibnb_project.data.dto.BookDTO;
import com.example.hibnb_project.data.entity.AccomEntity;
import com.example.hibnb_project.data.entity.BookEntity;
import com.example.hibnb_project.data.entity.UserEntity;
import com.example.hibnb_project.data.repository.AccomRepository;
import com.example.hibnb_project.data.repository.BookRepository;
import com.example.hibnb_project.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookDAO {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final AccomRepository accomRepository;

    public List<BookEntity> findbooksbyUsername(String username) {
        return this.bookRepository.findAllByUsername(username);
    }

    public Set<BookEntity> findbooksbyHostId(String hostid) {
        UserEntity user = this.userRepository.findById(hostid).orElse(null);
        if(user == null) {
            throw new EntityNotFoundException("User not found");
        }
        List<AccomEntity> accomEntityList = this.accomRepository.findByHostid(user);
        if(accomEntityList.isEmpty()) {
            throw new EntityNotFoundException("Accom not found");
        }
        Set<BookEntity> bookEntitySet = new HashSet<>();
        for(AccomEntity accomEntity : accomEntityList) {
            bookEntitySet.addAll(accomEntity.getBooks());
        }
        return bookEntitySet;
    }

    public void saveBook(String username, Integer accomid, LocalDate checkindate,LocalDate checkoutdate, Integer totalPrice, Integer person) {
        Optional<UserEntity> user = this.userRepository.findById(username);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("유저명 오류");
        }
        Optional<AccomEntity> accom = this.accomRepository.findById(accomid);
        if(!accom.isPresent()) {
            throw new EntityNotFoundException("숙소명 오류");
        }

        BookEntity bookEntity = BookEntity.builder()
                .username(user.get())
                .accomid(accom.get())
                .checkindate(checkindate)
                .checkoutdate(checkoutdate)
                .totalPrice(totalPrice)
                .status("예약")
                .yesorno(false)
                .payment("0")
                .build();

        this.bookRepository.save(bookEntity);
    }

    public void updateBook(Integer bookid, String username, Integer accomid, LocalDate checkindate,LocalDate checkoutdate, Integer totalPrice) {
        Optional<UserEntity> user = this.userRepository.findById(username);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("유저명 오류");
        }
        Optional<AccomEntity> accom = this.accomRepository.findById(accomid);
        if(!accom.isPresent()) {
            throw new EntityNotFoundException("숙소명 오류");
        }
        Optional<BookEntity> bookEntity = this.bookRepository.findById(bookid);
        if (!bookEntity.isPresent()) {
            throw new EntityNotFoundException("예약 정보 없음");
        }

        BookEntity updateBook = bookEntity.get();

        if (!updateBook.getUsername().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 예약의 소유자가 아닙니다.");
        }
        updateBook.setCheckindate(checkindate);
        updateBook.setCheckoutdate(checkoutdate);
        updateBook.setTotalPrice(totalPrice);

        this.bookRepository.save(updateBook);
    }

    public void cancelBook(Integer bookid, String username, Integer accomid) {
        Optional<UserEntity> user = this.userRepository.findById(username);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("유저명 오류");
        }
        Optional<AccomEntity> accom = this.accomRepository.findById(accomid);
        if(!accom.isPresent()) {
            throw new EntityNotFoundException("숙소명 오류");
        }
        Optional<BookEntity> bookEntity = this.bookRepository.findById(bookid);
        if (!bookEntity.isPresent()) {
            throw new EntityNotFoundException("예약 정보 없음");
        }

        BookEntity updateBook = bookEntity.get();

        if (!updateBook.getUsername().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 예약의 소유자가 아닙니다.");
        }
        updateBook.setStatus("예약취소");

        this.bookRepository.save(updateBook);
    }

    public void payBook(String username, Integer bookid) {
        Optional<UserEntity> user = this.userRepository.findById(username);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("유저명 오류");
        }

        Optional<BookEntity> bookEntity = this.bookRepository.findById(bookid);
        if (!bookEntity.isPresent()) {
            throw new EntityNotFoundException("예약 정보 없음");
        }

        BookEntity updateBook = bookEntity.get();

        if ("1".equals(updateBook.getPayment())) {
            throw new IllegalStateException("이미 결제가 완료된 예약입니다.");
        }

        if (!updateBook.getUsername().getUsername().equals(username)) {
            throw new IllegalArgumentException("해당 예약의 소유자가 아닙니다.");
        }
        updateBook.setPayment("1");

        this.bookRepository.save(updateBook);
    }
}
