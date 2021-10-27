package com.armand.ourhome.domain.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.text.MessageFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ItemDetail {

  private static final int MAX_NAME_LENGTH = 50;
  private static final int MAX_IMAGE_URL_LENGTH = 100;

  @Column(nullable = false, unique = true, length = MAX_NAME_LENGTH)
  private String name;

  @Lob
  @Column(nullable = false)
  private String description;

  @Column(nullable = false, length = MAX_IMAGE_URL_LENGTH)
  private String imageUrl;

  @Builder
  public ItemDetail(String name, String description, String imageUrl) {
    validate(name, description, imageUrl);
    this.name = name;
    this.description = description;
    this.imageUrl = imageUrl;
  }

  private void validate(String name, String description, String imageUrl) {
    validateName(name);
    validateDescription(description);
    validateImageUrl(imageUrl);
  }

  private void validateName(String name) {
    Assert.notNull(name, "상품 이름은 null이 될 수 없습니다.");

    if (name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException(
          MessageFormat.format("상품 이름의 길이는 1이상 50이하입니다. name = {0}", name));
    }
  }

  private void validateDescription(String description) {
    Assert.notNull(description, "상품 설명은 null이 될 수 없습니다.");

    if (description.isEmpty()) {
      throw new IllegalArgumentException(
          MessageFormat.format("상품 설명의 길이는 1이상입니다. description = {0}", description));
    }
  }

  private void validateImageUrl(String imageUrl) {
    Assert.notNull(imageUrl, "상품 이미지는 null이 될 수 없습니다.");

    if (imageUrl.isEmpty() || imageUrl.length() > MAX_IMAGE_URL_LENGTH) {
      throw new IllegalArgumentException(
          MessageFormat.format("이미지 url의 길이는 1이상 100이하입니다. imageUrl = {0}", imageUrl));
    }
  }

}
