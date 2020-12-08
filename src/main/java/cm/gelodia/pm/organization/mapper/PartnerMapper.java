package cm.gelodia.pm.organization.mapper;

import cm.gelodia.pm.organization.dto.PartnerDto;
import cm.gelodia.pm.organization.model.Partner;
import org.mapstruct.Mapper;

@Mapper
public interface PartnerMapper {
    Partner map(PartnerDto partnerDto);

    PartnerDto map(Partner partner);
}
