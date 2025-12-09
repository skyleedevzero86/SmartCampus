package com.sleekydz86.server.market.board.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "image", indexes = {@Index(name = "idx_image_finding", columnList = "board_id")})
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String uniqueName;

    public boolean isSameImageId(final Long id) {
        return this.id.equals(id);
    }
}
