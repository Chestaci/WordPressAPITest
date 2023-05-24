package com.github.Chestaci.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс dto для post.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Post {

    private Integer id;
    @Builder.Default
    private Title title = Title.builder().build();
    @Builder.Default
    private Content content = Content.builder().build();
    @Builder.Default
    private String status = "publish";
    private String postStatus;
}
