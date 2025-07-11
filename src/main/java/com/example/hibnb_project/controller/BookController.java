package com.example.hibnb_project.controller;

import com.example.hibnb_project.data.dto.BookDTO;
import com.example.hibnb_project.data.dto.BookNAccomDTO;
import com.example.hibnb_project.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/book")
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/list")
    public ResponseEntity<List<BookNAccomDTO>> findbooksbyUsername(@RequestParam("username") String username) {//username 필요
        BookDTO bookDTO = BookDTO.builder().username(username).build();
        List<BookNAccomDTO> bookNAccomDTOList= this.bookService.findbooksbyUsername(bookDTO);
        return ResponseEntity.status(HttpStatus.OK).body(bookNAccomDTOList);
    }// 숙소 정보 자세하게 필요함 -> bookdto에 숙소 객체 중 포함할 것 고려

    @GetMapping(value = "/list/hostid")
    public ResponseEntity<List<BookDTO>> findbooksbyHostid(@RequestParam("hostid") String hostid) {
        List<BookDTO> bookDTOList= this.bookService.findbooksbyHostId(hostid);
        return ResponseEntity.status(HttpStatus.OK).body(bookDTOList);
    }

     @PostMapping(value = "/save")
    public ResponseEntity<String> saveBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bookService.saveBook(bookDTO));
    }

    @PutMapping(value = "/update")//(id, username, accomid, checkindate, checkoutdate, totalprice)5가지 프론트에서 필요
    public ResponseEntity<String> updateBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bookService.updateBook(bookDTO));
    }

    @PutMapping(value = "/cancel")
    public ResponseEntity<String> cancelBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.bookService.cancelBook(bookDTO));
    }

}
