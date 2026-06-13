package com.sais.mapper;

import com.sais.dto.MuracaatDokumanDTO;
import com.sais.entity.MuracaatDokuman;
import org.mapstruct.*;


@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MuracaatDokumanMapper {

    @Mapping(source = "muracaat.id", target = "muracaatId")
    @Mapping(source = "yukleyenPersonel.id", target = "yukleyenPersonelId")
    @Mapping(source = "yukleyenPersonel.ad", target = "yukleyenPersonelAd")
    @Mapping(source = "yukleyenPersonel.soyad", target = "yukleyenPersonelSoyad")
    MuracaatDokumanDTO toDto(MuracaatDokuman entity);

    @Mapping(target = "muracaat", ignore = true)
    @Mapping(target = "yukleyenPersonel", ignore = true)
    MuracaatDokuman toEntity(MuracaatDokumanDTO dto);
}

