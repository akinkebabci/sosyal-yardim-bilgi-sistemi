package com.sais.mapper;

import com.sais.dto.AileMaddiDurumDTO;
import com.sais.dto.BorcBilgisiDTO;
import com.sais.dto.GayrimenkulBilgisiDTO;
import com.sais.dto.GelirBilgisiDTO;
import com.sais.entity.AileMaddiDurum;
import com.sais.entity.BorcBilgisi;
import com.sais.entity.GayrimenkulBilgisi;
import com.sais.entity.GelirBilgisi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AileMaddiDurumMapper {

    @Mapping(source = "muracaat.id", target = "muracaatId")
    AileMaddiDurumDTO toDto(AileMaddiDurum aileMaddiDurum);

    // Gelir Bilgisi Mapper
    @Mapping(source = "kisi.id", target = "kisiId")
    @Mapping(source = "kisi.tcKimlikNo", target = "tcKimlikNo")
    @Mapping(source = "kisi.adSoyad", target = "adSoyad")
    @Mapping(source = "gelirTuru.id", target = "gelirTuruId")
    @Mapping(source = "gelirTuru.adi", target = "gelirTuruAdi")
    @Mapping(target = "yakinlik", ignore = true) // TODO: Yakınlık bilgisi AileFert'ten alınacak
    GelirBilgisiDTO gelirToDto(GelirBilgisi gelirBilgisi);

    List<GelirBilgisiDTO> gelirToDtoList(List<GelirBilgisi> gelirBilgileri);

    // Borç Bilgisi Mapper
    @Mapping(source = "borcTuru.id", target = "borcTuruId")
    @Mapping(source = "borcTuru.adi", target = "borcTuruAdi")
    BorcBilgisiDTO borcToDto(BorcBilgisi borcBilgisi);

    List<BorcBilgisiDTO> borcToDtoList(List<BorcBilgisi> borcBilgileri);

    // Gayrimenkul Bilgisi Mapper
    GayrimenkulBilgisiDTO gayrimenkulToDto(GayrimenkulBilgisi gayrimenkulBilgisi);
}


