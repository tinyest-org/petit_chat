package org.tyniest.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.tyniest.common.mapper.MapperUtils;

@MapperConfig(
        implementationName = "$<CLASS_NAME>ImplDefinitionClass",
        componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
            MapperUtils.class,
        })
public interface MapstructConfig {}

