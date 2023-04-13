package com.example.japapp.controller;

import com.example.japapp.dto.UserDto;
import com.example.japapp.exception.MainException;
import com.example.japapp.model.Book;
import com.example.japapp.model.User;
import com.example.japapp.service.impl.BooksService;
import com.example.japapp.service.impl.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RequestMapping
@Controller()
public class BookController {
    private final BooksService bookService;
    private final FileService fileService;
    public BookController(BooksService bookService, FileService fileService) {
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @GetMapping("/books")
    public String getBooks(Model model) {
        try {
            List<Book> books = bookService.findAllBooks();
            model.addAttribute("books", books);
            return "books";
        } catch (MainException e) {
            return "redirect:/error/500";
        }
    }

    @GetMapping("/books/{bookId}")
    public String getBook(@PathVariable long bookId, Model model) {
        try {
            Book book = bookService.findById(bookId);
            String image = "/content/" + book.getImage();
            model.addAttribute("book", book);
            model.addAttribute("image", image);
            return "book";
        } catch (MainException e) {
            model.addAttribute("bookException", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/create-book")
    public String getCreatingBookPage() {
        return "createBook";
    }

    @PostMapping("/create-book")
    public String postUser(@RequestParam("title") String title,
                           @RequestParam("annotation") String annotation,
                           @RequestParam("image") MultipartFile image,
                           HttpServletRequest request) {

        String filename = fileService.saveFile(image);
        Book suspect = new Book(title, annotation);
        suspect.setImage(filename);

        UserDto userDto = (UserDto) request.getSession().getAttribute("user");
        suspect.setUser(new User(userDto.getId(), userDto.getName(), userDto.getEmail()));

        Book book = bookService.saveBook(suspect);
        return "redirect:/books/" + book.getId();

    }

    @ModelAttribute("profileLink")
    public String profileLink(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return user != null ? "/profile" : null;
    }
}
