package com.amazon.ata.optionals.nullchecks;

import com.amazon.ata.optionals.nullchecks.dao.AuthorDao;
import com.amazon.ata.optionals.nullchecks.models.Author;
import com.amazon.ata.optionals.nullchecks.models.Book;
import com.amazon.ata.optionals.nullchecks.models.Printing;
import com.amazon.ata.optionals.nullchecks.models.Publisher;

public class GetPublisherOfBestRatedPaperbackForAuthorActivity {
    private final AuthorDao authorDao;

    public GetPublisherOfBestRatedPaperbackForAuthorActivity(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    /**
     * Finds the publisher of the latest paperback version of the
     * named author's highest-rated book. If there is no author by that name,
     * or they have no books, or none of their books have been rated, or there
     * is no paperback version, or the paperback was not published by a company,
     * returns null.
     * @param authorName The name of the author to search for.
     * @return The ID of the publisher of the latest paperback version of the
     * named author's highest-rated book, if any; null otherwise.
     */
    public Publisher handleRequest(String authorName) {
        if (authorName == null) {
            throw new IllegalArgumentException("Author name must not be null!");
        }

        // Since none of these fields are required, calling this in a chain is very likely to throw an NPE
        // authorDao.findAuthorByName(name).getBestRatedBook().getPaperback().getPublisher().getId();

        // Protect each call from null return
        Author author = authorDao.findAuthorByName(authorName);

        if (author != null) {
            Book bestRatedBook = author.getBestRatedBook();
            if (bestRatedBook != null) {
                Printing paperback = bestRatedBook.getPaperback();
                if (paperback != null) {
                    return paperback.getPublisher();
                }
            }
        }

        return null;
    }

}
