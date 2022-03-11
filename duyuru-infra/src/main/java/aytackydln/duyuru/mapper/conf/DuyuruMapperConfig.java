package aytackydln.duyuru.mapper.conf;


import aytackydln.duyuru.mapper.UserMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.WARN,
        uses = {UserMapper.class}
)
public interface DuyuruMapperConfig {
}
