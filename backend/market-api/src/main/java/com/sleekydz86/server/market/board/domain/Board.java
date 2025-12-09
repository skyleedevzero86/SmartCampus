package com.sleekydz86.server.market.board.domain;

import com.sleekydz86.server.global.domain.BaseEntity;
import com.sleekydz86.server.market.board.domain.dto.BoardUpdateResult;
import com.sleekydz86.server.market.community.domain.ImageConverter;
import com.sleekydz86.server.market.community.domain.vo.LikeCount;
import com.sleekydz86.server.market.community.domain.vo.Post;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board extends BaseEntity {

    private Long id;

    private Post post;

    private Long writerId;

    private List<Image> images = new ArrayList<>();

    private LikeCount likeCount;

    @Builder
    public Board(
            final String title,
            final String content,
            final Long writerId,
            final List<MultipartFile> imageFiles,
            final ImageConverter imageConverter
    ) {
        this.post = Post.of(title, content);
        this.writerId = writerId;
        this.images.addAll(imageConverter.convertImageFilesToImages(imageFiles));
        likeCount = LikeCount.createDefault();
    }

    public BoardUpdateResult update(
            final String title,
            final String content,
            final List<MultipartFile> imageFiles,
            final List<Long> deletedImageIds,
            final ImageConverter imageConverter
    ) {
        post.update(title, content);

        List<Image> addedImages = imageConverter.convertImageFilesToImages(imageFiles);
        List<Image> deletedImages = imageConverter.convertImageIdsToImages(deletedImageIds, this.images);

        this.images.addAll(addedImages);
        this.images.removeAll(deletedImages);

        return new BoardUpdateResult(addedImages, deletedImages);
    }

    public void validateWriter(final Long memberId) {
        if (!this.writerId.equals(memberId)) {
            throw new WriterNotEqualsException();
        }
    }

    public void patchLike(final boolean isIncreaseLike) {
        this.likeCount.patchLike(isIncreaseLike);
    }
}
