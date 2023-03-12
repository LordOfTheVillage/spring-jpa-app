package com.example.japapp.services.impl;

import com.example.japapp.exeptions.BookCreatingException;
import com.example.japapp.exeptions.NotFoundBookException;
import com.example.japapp.exeptions.RegistrationException;
import com.example.japapp.models.Book;
import com.example.japapp.repositories.BooksRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BooksService {
    private final BooksRepository booksRepository;
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public Book findById(long id) throws NotFoundBookException {
        Optional<Book> book = this.booksRepository.findById(id);
        if (book.isEmpty()) {
            throw new NotFoundBookException("Book with this id wasn't found");
        }
        return book.get();
    }

    public Book findByTitle(String title) throws NotFoundBookException {
        Optional<Book> book = this.booksRepository.findBookByTitle(title);
        if (book.isEmpty()) {
            throw new NotFoundBookException("Book with this id wasn't found");
        }
        return book.get();
    }

    public Book saveBook(Book book) throws BookCreatingException {
        Optional<Book> suspect = this.booksRepository.findBookByTitle(book.getTitle());
        if (suspect.isPresent()) {
            throw new BookCreatingException("Book with this title already exists!");
        }

        try {
            return booksRepository.save(book);
        } catch (DataAccessException e) {
            throw new BookCreatingException("Cannot create book. Please try again later.");
        }
    }


}
