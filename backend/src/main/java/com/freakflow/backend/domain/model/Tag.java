package com.freakflow.backend.domain.model;

import com.freakflow.backend.domain.valueobject.TagName;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name ="value",column =@Column(name = "name",nullable = false, unique = true, length = 50) )
    })
    private TagName name;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String description;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Question> questions = new HashSet<>();

}
