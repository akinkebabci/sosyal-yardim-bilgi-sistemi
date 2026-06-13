package com.sais.service;

import com.sais.entity.Personel;
import com.sais.exception.DuplicateResourceException;
import com.sais.exception.ResourceNotFoundException;
import com.sais.repository.PersonelRepository;
import com.sais.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PersonelService {

    private final PersonelRepository personelRepository;

    public Personel create(Personel personel) {
        if (!ValidationUtil.isValidTcKimlikNo(personel.getTcKimlikNo())) {
            throw new IllegalArgumentException("Geçersiz TC Kimlik Numarası");
        }

        if (personelRepository.findByTcKimlikNo(personel.getTcKimlikNo()).isPresent()) {
            throw new DuplicateResourceException("Personel", "tcKimlikNo", personel.getTcKimlikNo());
        }

        return personelRepository.save(personel);
    }

    public Personel update(Personel personel) {
        if (personel.getId() == null) {
            throw new IllegalArgumentException("Güncellenecek personel ID'si boş olamaz");
        }

        findById(personel.getId());

        return personelRepository.save(personel);
    }

    @Transactional(readOnly = true)
    public Personel findById(Long id) {
        return personelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personel", "id", id));
    }

    @Transactional(readOnly = true)
    public Optional<Personel> findByTcKimlikNo(String tcKimlikNo) {
        return personelRepository.findByTcKimlikNo(tcKimlikNo);
    }

    @Transactional(readOnly = true)
    public List<Personel> findAllActive() {
        return personelRepository.findAktifPersoneller();
    }

    @Transactional(readOnly = true)
    public List<Personel> findTahkikatYetkililer() {
        return personelRepository.findTahkikatYetkililer();
    }

    @Transactional(readOnly = true)
    public List<Personel> findKomisyonUyeleri() {
        return personelRepository.findKomisyonUyeleri();
    }

    public void delete(Long id) {
        Personel personel = findById(id);
        personel.setAktif(false);
        personelRepository.save(personel);
    }
}


