package com.sais.service;

import com.sais.entity.*;
import com.sais.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LookupService {

    private final YakinlikKoduRepository yakinlikKoduRepository;
    private final MeslekRepository meslekRepository;
    private final OzelStatuRepository ozelStatuRepository;
    private final EngelliTipiRepository engelliTipiRepository;
    private final HastalikRepository hastalikRepository;
    private final GelirTuruRepository gelirTuruRepository;
    private final BorcTuruRepository borcTuruRepository;
    private final YardimDilimiRepository yardimDilimiRepository;
    private final YardimDonemiRepository yardimDonemiRepository;
    private final YardimRedSebebiRepository yardimRedSebebiRepository;
    private final HesapBilgisiRepository hesapBilgisiRepository;

    public List<YakinlikKodu> getAllYakinlikKodlari() {
        return yakinlikKoduRepository.findAllActive();
    }

    public List<Meslek> getAllMeslekler() {
        return meslekRepository.findAllActive();
    }
    public List<OzelStatu> getAllOzelStatuler() {
        return ozelStatuRepository.findAllActive();
    }

    public List<EngelliTipi> getAnaEngelliTipleri() {
        return engelliTipiRepository.findAnaTipler();
    }

    public List<EngelliTipi> getAltEngelliTipleri(Long ustTipId) {
        return engelliTipiRepository.findAltTiplerByUstTipId(ustTipId);
    }

    public List<EngelliTipi> getAllAltEngelliTipleri() {
        return engelliTipiRepository.findAllAltTipler();
    }

    public List<Hastalik> getAllHastaliklar() {
        return hastalikRepository.findAllActive();
    }

    public List<Hastalik> getKronikHastaliklar() {
        return hastalikRepository.findKronikHastaliklar();
    }

    public List<GelirTuru> getAllGelirTurleri() {
        return gelirTuruRepository.findAllActive();
    }

    public List<BorcTuru> getAllBorcTurleri() {
        return borcTuruRepository.findAllActive();
    }

    public List<YardimDilimi> getAllYardimDilimleri() {
        return yardimDilimiRepository.findAllActive();
    }

    public List<YardimDonemi> getAllYardimDonemleri() {
        return yardimDonemiRepository.findAllActive();
    }

    public List<YardimRedSebebi> getAllYardimRedSebepleri() {
        return yardimRedSebebiRepository.findAllActive();
    }

    public List<HesapBilgisi> getHesapBilgileriByKisiId(Long kisiId) {
        return hesapBilgisiRepository.findByKisiId(kisiId);
    }

    public HesapBilgisi getVarsayilanHesap(Long kisiId) {
        return hesapBilgisiRepository.findVarsayilanHesap(kisiId).orElse(null);
    }
}


