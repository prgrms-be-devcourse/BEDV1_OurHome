package com.armand.ourhome.domain.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "company")
public class Company {

  private static final int MAX_NAME_LENGTH = 15;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, length = MAX_NAME_LENGTH)
  private String name;

  public Company(String name) {
    validateName(name);

    this.name = name;
  }

  private void validateName(String name) {
    Assert.notNull(name, "회사 이름은 null이 될 수 없습니다");

    if (name.isEmpty() || name.length() > MAX_NAME_LENGTH) {
      throw new IllegalArgumentException(
          MessageFormat.format("회사 이름의 길이는 1이상 15이하입니다. name = {0}", name));
    }
  }

}
