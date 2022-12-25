package org.tyniest.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

@MapperConfig(
        implementationName = "$<CLASS_NAME>ImplDefinitionClass",
        componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
        })
public interface MapstructConfig {}

