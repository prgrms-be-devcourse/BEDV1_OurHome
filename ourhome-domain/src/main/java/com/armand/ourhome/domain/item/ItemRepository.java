package com.armand.ourhome.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

//  List<Item> findAllByName(String name);

  List<Item> findAllByCompany(Company company);

  List<Item> findAllByCategory(Category category);

}
