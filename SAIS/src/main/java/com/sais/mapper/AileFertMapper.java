package com.sais.mapper;

import com.sais.dto.AileFertDTO;
import com.sais.entity.AileFert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AileFertMapper {

    @Mapping(source = "muracaat.id", target = "muracaatId")
    @Mapping(source = "kisi.id", target = "kisiId")
    @Mapping(source = "kisi.tcKimlikNo", target = "tcKimlikNo")
    @Mapping(source = "kisi.adSoyad", target = "adSoyad")
    @Mapping(source = "kisi.dogumTarihi", target = "dogumTarihi")
    @Mapping(source = "kisi.yas", target = "yas")
    @Mapping(source = "kisi.cinsiyet", target = "cinsiyet")
    @Mapping(source = "kisi.sgkDurum", target = "sgkDurum")
    @Mapping(source = "kisi.ogrenimDurum", target = "ogrenimDurum")
    @Mapping(source = "yakinlikKodu.id", target = "yakinlikKoduId")
    @Mapping(source = "yakinlikKodu.adi", target = "yakinlikAdi")
    @Mapping(source = "ozelStatu.id", target = "ozelStatuId")
    @Mapping(source = "ozelStatu.adi", target = "ozelStatuAdi")
    @Mapping(source = "meslek.id", target = "meslekId")
    @Mapping(source = "meslek.adi", target = "meslekAdi")
    @Mapping(source = "engelBilgisi.engelliTipi.id", target = "engelliTipiId")
    @Mapping(source = "engelBilgisi.engelliTipi.adi", target = "engelliTipiAdi")
    @Mapping(source = "engelBilgisi.engelOrani", target = "engelOrani")
    @Mapping(source = "hastalikBilgisi.hastalik.id", target = "hastalikId")
    @Mapping(source = "hastalikBilgisi.hastalik.adi", target = "hastalikAdi")
    @Mapping(source = "hastalikBilgisi.hastalik.kronik", target = "kronik")
    @Mapping(target = "engelliMi", expression = "java(aileFert.getEngelBilgisi() != null)")
    @Mapping(target = "hastalikVarMi", expression = "java(aileFert.getHastalikBilgisi() != null)")
    AileFertDTO toDto(AileFert aileFert);

    List<AileFertDTO> toDtoList(List<AileFert> aileFertler);
}


