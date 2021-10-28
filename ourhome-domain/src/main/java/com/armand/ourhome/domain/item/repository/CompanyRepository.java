package com.armand.ourhome.domain.item.repository;

import com.armand.ourhome.domain.item.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
