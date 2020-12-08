package cm.gelodia.pm.organization.service.impl;

import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.exception.BadRequestException;
import cm.gelodia.pm.commons.exception.ConflictException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.organization.model.Charge;
import cm.gelodia.pm.organization.repository.ChargeRepository;
import cm.gelodia.pm.organization.service.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service("chargeService")
@RequiredArgsConstructor
public class ChargeServiceImpl implements ChargeService {

    private final ChargeRepository chargeRepository;

    @Override
    public Charge create(UserPrincipal principal, Charge charge) {
        //We set company
        if(principal.getCompany() == null) {
            log.error("Connected user has no company. contact your administrator");
            throw new BadRequestException("Connected user has no company. contact your administrator");
        }
        if(charge != null && chargeRepository.existsByCode(charge.getCode())) {
            log.error("A charge with code {} already exists", charge.getCode());
            throw new ConflictException(String.format("A charge with code %s already exists", charge.getCode()));
        }
        if(charge != null && chargeRepository.existsByName(charge.getName())) {
            log.error("A charge with name {} already exists", charge.getName());
            throw new ConflictException(String.format("A charge with name %s already exists", charge.getName()));
        }
        charge.setCompany(principal.getCompany());
        return chargeRepository.save(charge);
    }

    @Override
    public Charge update(UserPrincipal principal, Charge charge) {
        //TODO check unique value
        return chargeRepository.save(charge);
    }

    @Override
    public Page<Charge> findAll(UserPrincipal principal, String name, String code, PageRequest pageRequest) {
        Page<Charge> chargePage;

        if(!StringUtils.isEmpty(name)) {
            chargePage = chargeRepository.findByNameContainsIgnoreCase(name, pageRequest);
        }
        else if(!StringUtils.isEmpty(code)) {
            chargePage = chargeRepository.findByCodeContainsIgnoreCase(code, pageRequest);
        }

        else {
            chargePage = chargeRepository.findAll(pageRequest);
        }
        return chargePage;
    }

    @Override
    public Charge findById(UserPrincipal principal, String id) {
        return chargeRepository.findById(id).orElseThrow(() -> {
            log.error("charge with id {} not found", id);
            throw  new ResourceNotFoundException(String.format("charge with id %s not found", id));
        });
    }

    @Override
    public void delete(UserPrincipal principal, String id) {
        chargeRepository.delete(findById(principal, id));
    }

    @Override
    public void deleteMany(UserPrincipal principal, List<String> ids) {
        List<Charge> charges = chargeRepository.findAllById(ids);
        if(charges.size() != ids.size()) {
            log.error("some charges does not exits!");
            throw new ResourceNotFoundException("some charge does not exits!");
        }
        chargeRepository.deleteInBatch(charges);
    }
}
