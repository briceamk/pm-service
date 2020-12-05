package cm.gelodia.pm.commons.service;


import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.payload.ImageResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    ImageResource fileSystemStoreImage(UserPrincipal principal, ImageResource imageResource, MultipartFile image);
    MultipartFile fileSystemDefaultImage(UserPrincipal principal);
    List<String> fileSystemGetImageAsString(UserPrincipal principal, ImageResource imageResource);
    List<String> dbGetImageAsString(UserPrincipal principal, ImageResource imageResource);
    Resource fileSystemGetImageAsResource(UserPrincipal principal, ImageResource imageResource);
    ImageResource dbStoreImage(UserPrincipal principal, String objectId, MultipartFile image);

    MultipartFile dbDefaultImage(UserPrincipal principal);


}
