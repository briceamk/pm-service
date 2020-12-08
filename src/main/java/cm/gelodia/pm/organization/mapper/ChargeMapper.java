package cm.gelodia.pm.organization.mapper;

import cm.gelodia.pm.organization.dto.ChargeDto;
import cm.gelodia.pm.organization.model.Charge;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper
@DecoratedWith(ChargeMapperDecorator.class)
public interface ChargeMapper {
     Charge map(ChargeDto chargeDto);

    ChargeDto map(Charge charge);
}
