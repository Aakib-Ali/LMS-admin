package com.example.LMS.service;

import com.example.LMS.mapper.Mapper;
import com.example.LMS.dto.response.BookResponse;
import com.example.LMS.model.Book;
import com.example.LMS.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
	@Autowired
	private Mapper mapper;


    @Autowired
    private BookRepository bookRepository;

    public List<BookResponse> getAllActiveBooks() {
        List<Book> book= bookRepository.findByIsActiveTrue();
        List<BookResponse> bookResponse = new ArrayList<BookResponse>();
        for(Book b:book) {
        	bookResponse.add(mapper.toResponse(b));
        }
        return bookResponse;
    }
    
    public List<BookResponse> getAllBooks(){
    	List<Book> book= bookRepository.findAll();
        List<BookResponse> bookResponse = new ArrayList<BookResponse>();
        for(Book b:book) {
        	bookResponse.add(mapper.toResponse(b));
        }
        return bookResponse;
    }
    
    Optional<Book> findByBookId(String BookId){
    	return bookRepository.findByBookId(BookId);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

//    public List<Book> searchBooks(String searchTerm) {
//        return bookRepository.searchBooks(searchTerm);
//    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryAndIsActiveTrue(category);
    }

    public List<String> getAllCategories() {
        return bookRepository.findAllCategories();
    }

    public Book saveBook(Book book) {
        if (book.getId() == null) {
            book.setCreatedDate(LocalDateTime.now());
        } else {
            book.setUpdatedDate(LocalDateTime.now());
        }
        return bookRepository.save(book);
    }

    public void deactivateBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book bookToDeactivate = book.get();
            bookToDeactivate.setIsActive(false);
            bookToDeactivate.setUpdatedDate(LocalDateTime.now());
            bookRepository.save(bookToDeactivate);
        }
    }
    
    
    public List<BookResponse> searchBooks(String searchTerm, String searchType){
    	List<Book> books;
    	if(searchTerm==null || searchTerm.trim().isEmpty()) {
    		return getAllBooks();
    	}
    	if(searchTerm.trim().length()<1) {
    		switch(searchType.toLowerCase()) {
        	case "title":
        		books=bookRepository.findByTitleContainingIgnoreCaseAndIsActiveTrue(searchTerm);
        		break;
        	case "author":
        		books=bookRepository.findByAuthorContainingIgnoreCaseAndIsActiveTrue(searchType);
        		break;
        	case "category":
        		books=bookRepository.findByCategoryContainingIgnoreCaseAndIsActiveTrue(searchType);
        	default:
        		books=bookRepository.searchBooks(searchTerm);
        	}
    	}
    	else {
    		books=bookRepository.searchBooks(searchTerm);
    	}
    	List<BookResponse> bookResponse=new ArrayList();
    	for(Book book:books) {
    		bookResponse.add(mapper.toResponse(book));
    	}
    	return bookResponse;
    }

    public boolean isBookAvailable(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.isPresent() && book.get().getAvailableCopies() > 0 && book.get().getIsActive();
    }
   
}