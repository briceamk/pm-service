package cm.gelodia.pm.organization.mapper;

import cm.gelodia.pm.organization.dto.AddressDto;
import cm.gelodia.pm.organization.model.Address;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(AddressMapperDecorator.class)
public interface AddressMapper {

    Address map(AddressDto addressDto);
    AddressDto map(Address address);
}
