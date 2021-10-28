package com.armand.ourhome.domain.item.repository;

import com.armand.ourhome.domain.item.domain.Category;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

//  List<Item> findAllByName(String name);

  List<Item> findAllByCompany(Company company);

  List<Item> findAllByCategory(Category category);

}
