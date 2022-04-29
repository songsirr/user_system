package com.mj.user.orm.repository;

import com.mj.user.domain.common.CertificatePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificatePhoneRepository extends JpaRepository<CertificatePhone, Long> {

    void deleteAllByPhone(String phone);

    Optional<CertificatePhone> findByPhoneAndCode(String phone, String code);

    Optional<CertificatePhone> findByPhone(String phone);

    Optional<CertificatePhone> findByPhoneAndCertificatedAtIsNotNull(String phone);
}
