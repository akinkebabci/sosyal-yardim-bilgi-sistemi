package com.sais.mapper;

import com.sais.dto.MuracaatDTO;
import com.sais.entity.Muracaat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface MuracaatMapper {

    @Mapping(source = "basvuruSahibi.adSoyad", target = "basvuruSahibiAdSoyad")
    @Mapping(source = "basvuruSahibi.tcKimlikNo", target = "basvuruSahibiTcNo")
    MuracaatDTO toDto(Muracaat muracaat);

    Muracaat toEntity(MuracaatDTO dto);

    List<MuracaatDTO> toDtoList(List<Muracaat> muracaatlar);
}


