package com.myweddi.users.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAccountRepository extends JpaRepository<ServiceAccount, Long> {
}
