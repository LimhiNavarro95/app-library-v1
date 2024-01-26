package com.nav.services.ms_library_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

  private String bookName;
  private String author;
  private String releaseDate;

  /*public Book() {
  }

  public String getBookName() {
    return bookName;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  @Override
  public String toString() {
    return "Book{" +
        "bookName='" + bookName + '\'' +
        ", author='" + author + '\'' +
        ", releaseDate='" + releaseDate + '\'' +
        '}';
  }*/
}
