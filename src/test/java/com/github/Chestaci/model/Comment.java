package com.github.Chestaci.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс dto для comment.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Comment {

    private Integer id;
    private Integer postID;
    @Builder.Default
    private Content content = Content.builder().build();
    private String commentStatus;
}
