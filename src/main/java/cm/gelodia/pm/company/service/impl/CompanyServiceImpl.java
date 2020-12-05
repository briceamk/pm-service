package cm.gelodia.pm.company.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.commons.service.FileStorageService;
import cm.gelodia.pm.company.model.Company;
import cm.gelodia.pm.company.repository.CompanyRepository;
import cm.gelodia.pm.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service("companyService")
public class CompanyServiceImpl implements CompanyService {


    private final CompanyRepository companyRepository;
    private final FileStorageService fileStorageService;


    @Override
    @Transactional
    public Company create(UserPrincipal principal, Company company) {
        // we check if code, name, phoneNumber, mobileNumber, email, vatNumber, tradeRegister exist
        if(companyRepository.existsByCode(company.getCode())) {
            log.warn("Company with code {} exist!", company.getCode());
            throw new ConflictException(String.format("Company with code %s exist!", company.getCode()));
        }
        if(companyRepository.existsByName(company.getName())) {
            log.warn("Company with name {} exist!", company.getName());
            throw new ConflictException(String.format("Company with name %s exist!", company.getName()));
        }
        if(company.getPhoneNumber() != null && !company.getPhoneNumber().isEmpty() && companyRepository.existsByPhoneNumber(company.getPhoneNumber())) {
            log.warn("Company with phone number {} exist!", company.getPhoneNumber());
            throw new ConflictException(String.format("Company with phone number %s exist!", company.getPhoneNumber()));
        }
        if(company.getMobileNumber() != null && !company.getMobileNumber().isEmpty() && companyRepository.existsByMobileNumber(company.getMobileNumber())) {
            log.warn("Company with mobile number {} exist!", company.getMobileNumber());
            throw new ConflictException(String.format("Company with mobile number %s exist!", company.getMobileNumber()));
        }
        if(company.getEmail() != null && !company.getEmail().isEmpty() && companyRepository.existsByEmail(company.getEmail())) {
            log.warn("Company with email {} exist!", company.getEmail());
            throw new ConflictException(String.format("Company with email %s exist!", company.getEmail()));
        }
        if(company.getVat() != null && !company.getVat().isEmpty() && companyRepository.existsByVat(company.getVat())) {
            log.warn("Company with vat number {} exist!", company.getVat());
            throw new ConflictException(String.format("Company with vat number %s exist!", company.getVat()));
        }
        if(company.getTrn() != null && !company.getTrn().isEmpty() && companyRepository.existsByTrn(company.getTrn())) {
            log.warn("Company with trade register number {} exist!", company.getTrn());
            throw new ConflictException(String.format("Company with vat trade register number %s exist!", company.getTrn()));
        }
        company = companyRepository.save(company);
        // we set and save default image
        MultipartFile image = fileStorageService.dbDefaultImage(principal);
        ImageResource imageResource = fileStorageService.dbStoreImage(principal, company.getId(), image);
        company = storeLogo(principal, company.getId(), imageResource);
        return companyRepository.save(company);
    }

    @Override
    public Company update(UserPrincipal principal, Company company) {
        // TODO validate unique fields
        //we check if logo is not in payload and set it before saving
        if(company.getImage() == null || company.getImage().length == 0)
            company.setImage(findById(principal, company.getId()).getImage());
        return companyRepository.save(company);
    }

    @Override
    public Company findById(UserPrincipal principal, String id) {
        return companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("company with id %s not found!", id))
        );
    }

    @Override
    public Company findByCode(String code) {
        return companyRepository.findByCodeIgnoreCase(code).orElseThrow(
                () -> new ResourceNotFoundException(String.format("company with code %s not found!", code))
        );
    }

    @Override
    public Page<Company> findAll(UserPrincipal principal, String code, String name, String email, String phoneNumber,
                                 String mobileNumber, String vat, String trn, String street, String city, PageRequest pageRequest) {

        Page<Company> companyPage;

        if(!StringUtils.isEmpty(code))
            companyPage = companyRepository.findByCodeContainsIgnoreCase(code, pageRequest);
        else if(!StringUtils.isEmpty(name))
            companyPage = companyRepository.findByNameContainsIgnoreCase(name, pageRequest);
        else if(!StringUtils.isEmpty(email))
            companyPage = companyRepository.findByEmailContainsIgnoreCase(email, pageRequest);
        else if(!StringUtils.isEmpty(phoneNumber))
            companyPage = companyRepository.findByPhoneNumberIgnoreCase(phoneNumber, pageRequest);
        else if(!StringUtils.isEmpty(mobileNumber))
            companyPage = companyRepository.findByMobileNumberIgnoreCase(mobileNumber, pageRequest);
        else if(!StringUtils.isEmpty(vat))
            companyPage = companyRepository.findByVatContainsIgnoreCase(vat, pageRequest);
        else if(!StringUtils.isEmpty(trn))
            companyPage = companyRepository.findByTrnContainsIgnoreCase(trn, pageRequest);
        else if(!StringUtils.isEmpty(city))
            companyPage = companyRepository.findByCityContainsIgnoreCase(city, pageRequest);
        else if(!StringUtils.isEmpty(street))
            companyPage = companyRepository.findByStreetContainsIgnoreCase(street, pageRequest);
        else
            companyPage = companyRepository.findAll(pageRequest);

        return companyPage;
    }

    @Override
    public void deleteById(UserPrincipal principal, String id) {
        companyRepository.delete(findById(principal, id));
    }

    @Override
    public void deleteByIds(UserPrincipal principal, List<String> ids) {
        ids.forEach(id -> deleteById(principal, id));
    }

    @Override
    public Company storeLogo(UserPrincipal principal, String id, ImageResource imageResource) {
        Company company = findById(principal, id);
        if(imageResource != null) {
            if(imageResource.getImage() != null) {
                company.setImage(imageResource.getImage());
            }
            if(imageResource.getImageName() != null) {
                company.setImageName(imageResource.getImageName());
            }
            if(imageResource.getImageType() != null) {
                company.setImageType(imageResource.getImageType());
            }
        }
        return company;
    }

    @Override
    public Boolean containSwearWords(String comment) {
        if(comment == null || comment.isEmpty())
            throw  new BadRequestException("comment should not be null");
        if(comment.contains("shit"))
            throw new BadRequestException("This is unacceptable comment");
        return false;
    }


}
