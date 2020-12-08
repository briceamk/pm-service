package cm.gelodia.pm.organization.mapper;

import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.organization.dto.ChargeDto;
import cm.gelodia.pm.organization.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ChargeMapperDecorator implements ChargeMapper {

    private ChargeMapper chargeMapper;
    private CompanyMapper companyMapper;

    @Autowired
    public void setChargeMapper(ChargeMapper chargeMapper) {
        this.chargeMapper = chargeMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }


    @Override
    public Charge map(ChargeDto chargeDto) {
        Charge charge = chargeMapper.map(chargeDto);
        if(chargeDto.getCompanyDto() != null)
            charge.setCompany(companyMapper.map(chargeDto.getCompanyDto()));
        return charge;
    }

    @Override
    public ChargeDto map(Charge charge) {
        ChargeDto chargeDto = chargeMapper.map(charge);
        if(charge.getCompany() != null)
            chargeDto.setCompanyDto(companyMapper.map(charge.getCompany()));
        return chargeDto;
    }
}
