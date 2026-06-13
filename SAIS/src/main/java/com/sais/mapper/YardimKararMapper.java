package com.sais.mapper;

import com.sais.dto.YardimKararDTO;
import com.sais.entity.YardimKarar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface YardimKararMapper {

    @Mapping(source = "muracaat.id", target = "muracaatId")
    @Mapping(source = "yardimAltTipi.id", target = "yardimAltTipiId")
    @Mapping(source = "yardimAltTipi.adi", target = "yardimAdi")
    @Mapping(source = "yardimAltTipi.yardimTipi", target = "yardimTipi")
    @Mapping(source = "yardimDilimi.id", target = "yardimDilimiId")
    @Mapping(source = "yardimDilimi.adi", target = "yardimDilimiAdi")
    @Mapping(source = "yardimDonemi.id", target = "yardimDonemiId")
    @Mapping(source = "yardimDonemi.aySayisi", target = "ayKont")
    @Mapping(source = "hesapBilgisi.id", target = "hesapBilgisiId")
    @Mapping(source = "hesapBilgisi.iban", target = "iban")
    @Mapping(source = "redSebebi.id", target = "redSebebiId")
    @Mapping(source = "redSebebi.adi", target = "redSebebiAdi")
    YardimKararDTO toDto(YardimKarar yardimKarar);

    List<YardimKararDTO> toDtoList(List<YardimKarar> yardimKararlar);
}


