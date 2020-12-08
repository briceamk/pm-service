package cm.gelodia.pm.organization.mapper;

import cm.gelodia.pm.company.mapper.CompanyMapper;
import cm.gelodia.pm.organization.dto.AddressDto;
import cm.gelodia.pm.organization.model.Address;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AddressMapperDecorator implements AddressMapper {

    private AddressMapper addressMapper;
    private CompanyMapper companyMapper;
    private PartnerMapper partnerMapper;
    private DepartmentMapper departmentMapper;

    @Autowired
    public void setAddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Autowired
    public void setPartnerMapper(PartnerMapper partnerMapper) {
        this.partnerMapper = partnerMapper;
    }

    @Autowired
    public void setDepartmentMapper(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Address map(AddressDto addressDto) {
        Address address = addressMapper.map(addressDto);
        if(addressDto.getCompanyDto() != null)
            address.setCompany(companyMapper.map(addressDto.getCompanyDto()));
        if(addressDto.getPartnerDto() != null)
            address.setPartner(partnerMapper.map(addressDto.getPartnerDto()));
        if(addressDto.getDepartmentDto() != null)
            address.setDepartment(departmentMapper.map(addressDto.getDepartmentDto()));
        return address;
    }

    @Override
    public AddressDto map(Address address) {
        AddressDto addressDto = addressMapper.map(address);
        if(address.getCompany() != null)
            addressDto.setCompanyDto(companyMapper.map(address.getCompany()));
        if(address.getDepartment() != null)
            addressDto.setDepartmentDto(departmentMapper.map(address.getDepartment()));
        if(address.getPartner() != null)
            addressDto.setPartnerDto(partnerMapper.map(address.getPartner()));
        return addressDto;
    }
}
