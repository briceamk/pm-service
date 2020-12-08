package cm.gelodia.pm.organization.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.organization.constant.OrganizationConstantTypes;
import cm.gelodia.pm.organization.model.Address;
import cm.gelodia.pm.organization.repository.AddressRepository;
import cm.gelodia.pm.organization.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service("addressService")
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public Address create(UserPrincipal principal, Address address) {
        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }
        address.setCompany(principal.getCompany());
        return addressRepository.save(address);
    }

    @Override
    public Address update(UserPrincipal principal, Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Page<Address> findAll(UserPrincipal principal, String name, String vat, String trn, 
                                 String firstName, String lastName, String email, String phone, 
                                 String mobile, String city, String country, String fax, String zip, 
                                 String street, PageRequest pageRequest) {
        Page<Address> addressPage;

        if(!StringUtils.isEmpty(name)) {
            addressPage = addressRepository.findByNameContainsIgnoreCase(name, pageRequest);
        }
        else if(!StringUtils.isEmpty(vat)) {
            addressPage = addressRepository.findByVatContainsIgnoreCase(vat, pageRequest);
        }
        else if(!StringUtils.isEmpty(trn)) {
            addressPage = addressRepository.findByTrnContainsIgnoreCase(trn, pageRequest);
        }
        else if(!StringUtils.isEmpty(firstName)) {
            addressPage = addressRepository.findByFirstNameContainsIgnoreCase(firstName, pageRequest);
        }
        else if(!StringUtils.isEmpty(lastName)) {
            addressPage = addressRepository.findByLastNameContainsIgnoreCase(lastName, pageRequest);
        }
        else if(!StringUtils.isEmpty(email)) {
            addressPage = addressRepository.findByEmailContainsIgnoreCase(email, pageRequest);
        }
        else if(!StringUtils.isEmpty(phone)) {
            addressPage = addressRepository.findByPhoneContainsIgnoreCase(phone, pageRequest);
        }
        else if(!StringUtils.isEmpty(mobile)) {
            addressPage = addressRepository.findByMobileContainsIgnoreCase(mobile, pageRequest);
        }
        else if(!StringUtils.isEmpty(city)) {
            addressPage = addressRepository.findByCityContainsIgnoreCase(city, pageRequest);
        }
        else if(!StringUtils.isEmpty(country)) {
            addressPage = addressRepository.findByCountryContainsIgnoreCase(country, pageRequest);
        }
        else if(!StringUtils.isEmpty(fax)) {
            addressPage = addressRepository.findByFaxContainsIgnoreCase(fax, pageRequest);
        }
        else if(!StringUtils.isEmpty(zip)) {
            addressPage = addressRepository.findByZipContainsIgnoreCase(zip, pageRequest);
        }
        else if(!StringUtils.isEmpty(street)) {
            addressPage = addressRepository.findByStreetContainsIgnoreCase(street, pageRequest);
        }
        else {
            addressPage = addressRepository.findAll(pageRequest);
        }
        return addressPage;
    }

    @Override
    public void delete(UserPrincipal principal, String id) {
        addressRepository.delete(findById(principal, id));
    }

    @Override
    public Address findById(UserPrincipal principal, String id) {
        return addressRepository.findById(id).orElseThrow(() -> {
            log.error("address with id {} not found", id);
            throw new ResourceNotFoundException(String.format("address with id %s not found!", id));
        });
    }

    @Override
    public void deleteMany(UserPrincipal principal, List<String> ids) {
        List<Address> addresses = addressRepository.findAllById(ids);
        if(addresses.size() != ids.size()) {
            log.error("some addresses does not exits!");
            throw new ResourceNotFoundException("some addresses does not exits!");
        }
        addressRepository.deleteInBatch(addresses);
    }

    @Override
    public Address storeImage(UserPrincipal principal, String id, ImageResource imageResource, String field) {
        Address address = findById(principal, id);
        if(imageResource != null) {
            if(imageResource.getImageName() != null && !imageResource.getImageName().isEmpty()) {
                if(OrganizationConstantTypes.FOOTER_FIELD.equals(field))
                    address.setImageFooterName(imageResource.getImageName());
                else
                    address.setImageHeaderName(imageResource.getImageName());
            }
            if(imageResource.getImageType() != null && !imageResource.getImageType().isEmpty()) {
                if(OrganizationConstantTypes.FOOTER_FIELD.equals(field))
                    address.setImageFooterType(imageResource.getImageType());
                else
                    address.setImageHeaderType(imageResource.getImageType());
            }
        }
        return address;
    }
}
