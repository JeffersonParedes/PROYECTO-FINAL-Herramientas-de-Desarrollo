package com.foro.app.entity.converter;

import com.foro.app.entity.Reporte.MotivoReporte;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MotivoReporteConverter implements AttributeConverter<MotivoReporte, String> {

    @Override
    public String convertToDatabaseColumn(MotivoReporte attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().replace('_', ' ');
    }

    @Override
    public MotivoReporte convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return MotivoReporte.valueOf(dbData.replace(' ', '_'));
        } catch (IllegalArgumentException e) {
            return MotivoReporte.otros;
        }
    }
}

