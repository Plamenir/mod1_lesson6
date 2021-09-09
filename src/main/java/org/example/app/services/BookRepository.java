package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        if (!book.getTitle().equals("") || !book.getAuthor().equals("") || book.getSize() != null) {
            book.setId(book.hashCode());
            logger.info("store new book: " + book);
            repo.add(book);
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book completed: " + book);
                return repo.remove(book);
            }
        }
        return false;
    }

    @Override
    public boolean removeItemByRegex(String queryRegex) {
        boolean isRemoved = false;
        Pattern pattern = Pattern.compile(queryRegex);
       for (Book book : retreiveAll()) {
           Matcher matcherTitle = pattern.matcher(book.getTitle());
           Matcher matcherAuthor = pattern.matcher(book.getAuthor());
           Matcher matcherSize = book.getSize() != null ? pattern.matcher(book.getSize().toString()) : pattern.matcher("");
           if (matcherAuthor.find() || matcherTitle.find() || matcherSize.find()) {
               logger.info("remove book completed: " + book);
               isRemoved = true;
               repo.remove(book);
           }
       }
        return isRemoved;
    }
}
