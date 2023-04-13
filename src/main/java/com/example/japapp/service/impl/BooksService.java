package com.example.japapp.service.impl;

import com.example.japapp.exception.MainException;
import com.example.japapp.model.Book;
import com.example.japapp.repository.BooksRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private final BooksRepository booksRepository;
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Book findById(long id) throws MainException {
        Optional<Book> book = this.booksRepository.findById(id);
        if (book.isEmpty()) {
            throw new MainException("Book with this id wasn't found");
        }
        return book.get();
    }

    public Book findByTitle(String title) throws MainException {
        Optional<Book> book = this.booksRepository.findBookByTitle(title);
        if (book.isEmpty()) {
            throw new MainException("Book with this id wasn't found");
        }
        return book.get();
    }

    public List<Book> findAllBooks() throws MainException {
        Optional<List<Book>> books = Optional.ofNullable(this.booksRepository.findAll());
        if (books.isEmpty()) {
            throw new MainException("Books are not found");
        }

        return books.get();
    }

    public Book saveBook(Book book) throws MainException {
        Optional<Book> suspect = this.booksRepository.findBookByTitle(book.getTitle());
        if (suspect.isPresent()) {
            throw new MainException("Book with this title already exists!");
        }

        try {
            return booksRepository.save(book);
        } catch (DataAccessException e) {
            throw new MainException("Cannot create book. Please try again later: " + e.getMessage());
        }
    }


}
