package com.example.school_management_backend.enums;

// Kullanıcı rollerini tanımlar: her biri sistemdeki bir yetki seviyesini temsil eder
public enum Role {
    ADMIN,      // Sistemde tam yetkili kullanıcı
    MUDUR,      // Okul müdürü, bazı yönetim işlemleri yapabilir
    OGRETMEN,   // Öğretmen, öğrencilere not verebilir vs.
    OGRENCI     // Öğrenci, sadece kendi verilerini görebilir
}
