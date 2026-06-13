package com.sais.mapper;

import com.sais.dto.TutanakBilgisiDTO;
import com.sais.entity.TutanakBilgisi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TutanakBilgisiMapper {

    @Mapping(source = "muracaat.id", target = "muracaatId")
    @Mapping(source = "tahkikatPersonel.id", target = "tahkikatPersonelId")
    @Mapping(source = "tahkikatPersonel.adSoyad", target = "tahkikatPersonelAdi")
    TutanakBilgisiDTO toDto(TutanakBilgisi tutanakBilgisi);

    TutanakBilgisi toEntity(TutanakBilgisiDTO dto);
}


