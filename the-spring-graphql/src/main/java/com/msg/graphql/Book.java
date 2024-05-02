package com.msg.graphql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {

  String id;
  String name;

  public static Book getById(String id) {
    List<Book> books =
        Arrays.asList(
            new Book("book-1", "Harry Potter and the Philosopher's Stone"),
            new Book("book-2", "Moby Dick"),
            new Book("book-3", "Interview with the vampire"));

    System.out.println("percy: record");
    return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElse(null);
  }
}
