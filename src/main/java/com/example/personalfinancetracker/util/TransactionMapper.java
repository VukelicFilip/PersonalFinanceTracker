package com.example.personalfinancetracker.util;

import com.example.personalfinancetracker.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    void update(@MappingTarget Transaction transaction, Transaction updateInfo);
}
