package com.example.books.mapper;

import com.example.books.dto.AuthorDto;
import com.example.books.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDto toAuthorDto(Author author) {
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .age(author.getAge())
                .followersNumber(author.getFollowersNumber())
                .build();
    }

    public Author toAuthor(AuthorDto authorDto) {
        return Author.builder()
                .id(authorDto.id())
                .name(authorDto.name())
                .age(authorDto.age())
                .followersNumber(authorDto.followersNumber())
                .build();
    }
}
