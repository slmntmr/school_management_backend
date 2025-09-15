package com.example.school_management_backend.controller;

import com.example.school_management_backend.dto.ChangePasswordRequestDTO;
import com.example.school_management_backend.dto.OgrenciRequestDTO;
import com.example.school_management_backend.dto.OgrenciResponseDTO;
import com.example.school_management_backend.service.CustomUserDetails;
import com.example.school_management_backend.service.CustomUserDetailsService;
import com.example.school_management_backend.service.OgrenciService;
import com.example.school_management_backend.security.CustomUserDetails; // Eğer bu sınıf varsa, import et
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

 // validasyon için

@RestController
@RequestMapping("/api/ogrenci")
@RequiredArgsConstructor
public class OgrenciController {

    private final OgrenciService ogrenciService;
    private final CustomUserDetails customUserDetails;

    /**
     * 1. Öğrenci Kaydı
     *     - Sadece ADMIN veya MUDUR rolündeki kullanıcılar yeni öğrenci kaydı yapabilir.
     *     - İstek gövdesinde gelen OgrenciRequestDTO doğrulanır.
     */
    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN','MUDUR')")
    public ResponseEntity<OgrenciResponseDTO> register(
            @Valid @RequestBody OgrenciRequestDTO requestDTO) {
        OgrenciResponseDTO resp = ogrenciService.registerOgrenci(requestDTO);
        return ResponseEntity.ok(resp);
    }

    /**
     * 2. Profil Görüntüleme (Kendi Profil)
     *     - OGRENCI rolündeki kullanıcı kendi profilini görür.
     *     - ADMIN veya MUDUR rolleri başka öğrencilerin profillerini görüntüleme yetkisine sahipse burayı genişletilebilir.
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('OGRENCI') or hasAnyRole('ADMIN','MUDUR')")
    public ResponseEntity<OgrenciResponseDTO> profile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // userDetails.getEmail() kullanarak öğrencinin verilerini alıyoruz
        OgrenciResponseDTO resp = ogrenciService.getByEmail(userDetails.getEmail());
        return ResponseEntity.ok(resp);
    }

    /**
     * 3. Profil Güncelleme (Kendi Profil)
     *     - OGRENCI kendi profilini günceller.
     *     - ADMIN veya MUDUR, başka öğrencileri güncelleme yetkisine sahipse bu endpoint ya da başka bir endpoint ile yapılabilir.
     */
    @PutMapping("/profile")
    @PreAuthorize("hasRole('OGRENCI') or hasAnyRole('ADMIN','MUDUR')")
    public ResponseEntity<OgrenciResponseDTO> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody OgrenciRequestDTO updateDTO) {
        OgrenciResponseDTO resp = ogrenciService.updateOgrenci(userDetails.getEmail(), updateDTO);
        return ResponseEntity.ok(resp);
    }

    /**
     * 4. Şifre Değiştirme
     *     - Öğrenci kendi hesabının şifresini değiştirebilir.
     */
    @PostMapping("/change-password")
    @PreAuthorize("hasRole('OGRENCI')")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangePasswordRequestDTO passwordRequest) {
        ogrenciService.changePassword(userDetails.getId(), passwordRequest);
        return ResponseEntity.ok("Şifre başarıyla değiştirildi");
    }

    /**
     * 5. Hesap Silme
     *     - Öğrenci kendi hesabını silebilir.
     *     - ADMIN veya MUDUR, başkalarının hesaplarını silebilir.
     *     - Kendi hesabını silerken id kontrolü yapılır.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("(hasRole('OGRENCI') and #id == authentication.principal.id) or hasAnyRole('ADMIN','MUDUR')")
    public ResponseEntity<String> deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        ogrenciService.deleteOgrenci(id);
        return ResponseEntity.ok("Öğrenci hesabı silindi");
    }
}
