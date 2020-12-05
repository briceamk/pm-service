package cm.gelodia.pm.auth.mapper;

import cm.gelodia.pm.auth.dto.UserDto;
import cm.gelodia.pm.auth.model.User;
import cm.gelodia.pm.company.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class UserMapperDecorator implements UserMapper{

    private  UserMapper userMapper;
    private  PermissionMapper permissionMapper;
    private CompanyMapper companyMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setPermissionMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Autowired
    public void setCompanyMapper(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public User map(UserDto userDto) {
        User user = userMapper.map(userDto);
        if(userDto.getPermissionDtos() != null)
            user.setPermissions(permissionMapper.mapToListEntity(userDto.getPermissionDtos()));
        if(userDto.getCompanyDto() != null)
            user.setCompany(companyMapper.map(userDto.getCompanyDto()));
        return user;
    }

    @Override
    public UserDto map(User user) {
        UserDto userDto = userMapper.map(user);
        if(user.getPermissions() != null )
            userDto.setPermissionDtos(permissionMapper.mapToListDto(user.getPermissions()));
        if(user.getCompany() != null)
            userDto.setCompanyDto(companyMapper.map(user.getCompany()));
        return userDto;
    }
}
