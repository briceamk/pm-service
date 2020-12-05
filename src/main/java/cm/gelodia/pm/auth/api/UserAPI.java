package cm.gelodia.pm.auth.api;


import cm.gelodia.pm.auth.dto.UserDto;
import cm.gelodia.pm.auth.dto.UserDtoPage;
import cm.gelodia.pm.auth.mapper.UserMapper;
import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.auth.payload.AddRoleToUserRequest;
import cm.gelodia.pm.auth.security.CurrentPrincipal;
import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.auth.service.UserService;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.payload.ResponseApi;
import cm.gelodia.pm.commons.service.ValidationErrorService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/users")
@Api(value = "User", tags = "User End Point")
public class UserAPI {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ValidationErrorService validationErrorService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody UserDto userDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        userDto = userMapper.map(userService.create(principal, userMapper.map(userDto)));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/v1/auth/users/{id}")
                .buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> updateUser(@CurrentPrincipal UserPrincipal principal, @Valid @RequestBody UserDto userDto, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;

        return ResponseEntity.ok().body(userMapper.map(userService.update(principal, userMapper.map(userDto))));
    }

    @PutMapping("/add-rule")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<?> addRoleToUser(@CurrentPrincipal UserPrincipal principal,
                                           @Valid @RequestBody AddRoleToUserRequest addRoleToUserRequest, BindingResult result) {
        ResponseEntity<?> errors = validationErrorService.process(result);
        if(errors != null)
            return errors;
        userService.addRoleToUser(principal,  addRoleToUserRequest.getUserId(), addRoleToUserRequest.getRoles());
        return ResponseEntity.ok().body(new ResponseApi(true, "roles successfully added to user!"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    public @ResponseBody ResponseEntity<?> findAll( @CurrentPrincipal UserPrincipal principal,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile) {
        if (pageNumber == null || pageNumber < 0){
            pageNumber = CommonConstantType.DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = CommonConstantType.DEFAULT_PAGE_SIZE;
        }
        Page<User> userPage = userService.findAll(principal, firstName, lastName, username, email, mobile,
                PageRequest.of(pageNumber, pageSize));
        UserDtoPage userDtoPage = new UserDtoPage(
                userPage.getContent().stream().map(userMapper::map).collect(Collectors.toList()),
                PageRequest.of(userPage.getPageable().getPageNumber(),
                        userPage.getPageable().getPageSize()),
                userPage.getTotalElements()
        );

        return ResponseEntity.ok(userDtoPage);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER', 'ROLE_USER')")
    public ResponseEntity<?> getUserById(@CurrentPrincipal UserPrincipal principal,
            @PathVariable String id) {
        return ResponseEntity.ok(userMapper.map(userService.findById(principal, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@CurrentPrincipal UserPrincipal principal, @PathVariable String id) {
        userService.deleteById(principal, id);
        return ResponseEntity.ok().body(new ResponseApi(true, "user deleted successfully!"));
    }

}
