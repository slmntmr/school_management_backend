package com.example.school_management_backend.repository;

import com.example.school_management_backend.entity.User;
import com.example.school_management_backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository anotasyonu bu sınıfın bir Spring bean olduğunu belirtir
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Kullanıcıyı e-posta adresine göre bulmak için özel bir metod
    Optional<User> findByEmail(String email);

    // Aynı email var mı kontrol etmek için boolean metodu
    boolean existsByEmail(String email);

    // İstersen öğrenci filtrelemek için:
    List<User> findAllByRole(Role role);
}
