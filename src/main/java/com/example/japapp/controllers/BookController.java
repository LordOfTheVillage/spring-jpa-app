package com.example.japapp.controllers;

import com.example.japapp.dto.UserDto;
import com.example.japapp.exceptions.MainException;
import com.example.japapp.models.Book;
import com.example.japapp.models.User;
import com.example.japapp.services.impl.BooksService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;


@RequestMapping
@Controller()
public class BookController {
    private BooksService bookService;
    public BookController(BooksService bookService) {
        this.bookService = bookService;
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
            model.addAttribute("book", book);
            return "book";
        } catch (MainException e) {
            model.addAttribute("bookException", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/create-book")
    public String getCreatingBookPage(Model model) {
        model.addAttribute("book", new Book());
        return "createBook";
    }

    @PostMapping("/create-book")
    public String postUser(@ModelAttribute("book") Book suspect, Model model, HttpServletRequest request) {
        try {
            UserDto userDto = (UserDto) request.getSession().getAttribute("user");
            suspect.setUser(new User(userDto.getId(), userDto.getName(), userDto.getEmail()));
            Book book = bookService.saveBook(suspect);
            return "redirect:/books/" + book.getId();
        } catch (MainException e) {
            model.addAttribute("createBookException", e.getMessage());
            return "redirect:/create-book";
        }
    }

    @ModelAttribute("profileLink")
    public String profileLink(HttpServletRequest request) {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        return user != null ? "/profile" : null;
    }
}
