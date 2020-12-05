package cm.gelodia.pm.commons.service.impl;


import cm.gelodia.pm.auth.security.UserPrincipal;
import cm.gelodia.pm.commons.configuration.FileStorageConfig;
import cm.gelodia.pm.commons.constant.CommonConstantType;
import cm.gelodia.pm.commons.exception.FileStorageException;
import cm.gelodia.pm.commons.exception.ResourceNotFoundException;
import cm.gelodia.pm.commons.payload.ImageResource;
import cm.gelodia.pm.commons.service.FileStorageService;
import cm.gelodia.pm.commons.util.CustomMultipartFile;
import cm.gelodia.pm.company.constant.CompanyConstantType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {

    private final Path storagePath;

    public FileStorageServiceImpl(FileStorageConfig fileStorageConfig) {
        this.storagePath = Paths.get(fileStorageConfig.getStorageLocation())
                .toAbsolutePath()
                .normalize();
        try{
            Files.createDirectories(this.storagePath);
        }catch (Exception ex){
            throw new FileStorageException("We can't create directory where file will be saved");
        }
    }

    @Override
    public ImageResource fileSystemStoreImage(UserPrincipal principal, ImageResource imageResource, MultipartFile image) {

        //Normalize image name
        String imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));


        try{
            //check if filename content invalid characters
            if(imageName.contains("..")){
                throw new FileStorageException("image contains invalid characters!"+imageName);
            }

            //copy file to the target location (Replace existing image with the same image name
            Path targetLocation;
            targetLocation = this.storagePath.resolve(imageResource.getId() + "_" + imageName);

            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            //if product have image, we delete it
            if(( imageResource.getImageName() != null && !imageResource.getImageName().equals(CommonConstantType.DEFAULT_PRODUCT_IMAGE_NAME)) &&
                    Files.exists(this.storagePath.resolve(imageResource.getImageName())))
            {
                Files.deleteIfExists(this.storagePath.resolve(imageResource.getImageName()));
            }

            //we update employee in database with the image name and image path
            imageResource.setImageName(imageResource.getId() + "_" + imageName);
            imageResource.setImageType(image.getContentType());
            return imageResource;
        }catch (IOException ex){
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!");
        }
    }

    @Override
    public MultipartFile fileSystemDefaultImage(UserPrincipal userPrincipal) {
        try {
            Path path = this.storagePath.resolve(CommonConstantType.DEFAULT_PRODUCT_IMAGE_NAME).normalize();
            byte[] content = Files.readAllBytes(path);
            return new CustomMultipartFile(CommonConstantType.DEFAULT_PRODUCT_IMAGE_NAME,
                    CommonConstantType.DEFAULT_PRODUCT_IMAGE_NAME, CommonConstantType.DEFAULT_IMAGE_TYPE, content);
        } catch (IOException ioe) {
            throw new FileStorageException("can't find default product image!");
        }
    }

    @Override
    public List<String> fileSystemGetImageAsString(UserPrincipal principal, ImageResource imageResource) {
        String imageName = imageResource.getImageName();

        Resource resource;
        List<String> images = new ArrayList<>();

        if(imageName.isEmpty())
            throw new ResourceNotFoundException("Image not found " + imageName);
        try {
            Path imagePath = this.storagePath.resolve(imageName).normalize();
            resource = new UrlResource(imagePath.toUri());
            String extension = FilenameUtils.getExtension(resource.getFilename());
            FileInputStream fileInputStream = new FileInputStream(resource.getFile());
            byte[] bytes = new byte[(int) resource.getFile().length()];
            fileInputStream.read(bytes);
            String encoded64 = Base64.getEncoder().encodeToString(bytes);
            images.add("data:image/"+extension+";base64,"+encoded64);
            fileInputStream.close();

        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Image not found " + imageName);
        }catch (IOException ex) {
            throw new ResourceNotFoundException("Could not determine file type");
        }
        return images;
    }

    @Override
    public List<String> dbGetImageAsString(UserPrincipal principal, ImageResource imageResource) {
        String imageName = imageResource.getImageName();

        List<String> images = new ArrayList<>();

        if(imageName.isEmpty())
            throw new ResourceNotFoundException("Image not found " + imageName);
        String extension = FilenameUtils.getExtension(imageName);
        String encoded64 = Base64.getEncoder().encodeToString(imageResource.getImage());
        images.add("data:image/"+extension+";base64,"+encoded64);
        return images;
    }

    @Override
    public Resource fileSystemGetImageAsResource(UserPrincipal principal, ImageResource imageResource) {
        String imageName = imageResource.getImageName();

        Resource resource;

        if(imageName.isEmpty())
            throw new ResourceNotFoundException("Image not found " + imageName);
        try {
            Path imagePath = this.storagePath.resolve(imageName).normalize();
            resource = new UrlResource(imagePath.toUri());
            if(!resource.exists()) {
                throw new ResourceNotFoundException("Image not found " + imageName);
            }
            return resource;
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("Image not found " + imageName);
        }
    }

    @Override
    public ImageResource dbStoreImage(UserPrincipal principal, String objectId, MultipartFile image) {
        // normalize file name
        String imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        ImageResource.ImageResourceBuilder builder = ImageResource.builder();
        try {
            // check if file name container invalid character
            if(imageName.contains("..")) {
                throw new FileStorageException("Sorry! image name contains invalid characters sequence");
            }
            builder.image(image.getBytes());
            builder.imageName(objectId + "_" + imageName);
            builder.imageType(image.getContentType());
            return builder.build();
        } catch (IOException ioe) {
            throw new FileStorageException("Could not store file " + imageName + ". Please try again!");
        }
    }

    @Override
    public MultipartFile dbDefaultImage(UserPrincipal principal) {
        try {
            Path path = this.storagePath.resolve(CompanyConstantType.DEFAULT_COMPANY_LOGO_FILE_NAME).normalize();
            byte[] content = Files.readAllBytes(path);
            return  new CustomMultipartFile(CompanyConstantType.DEFAULT_COMPANY_LOGO_FILE_NAME,
                    CompanyConstantType.DEFAULT_COMPANY_LOGO_FILE_NAME, CompanyConstantType.DEFAULT_COMPANY_LOGO_IMAGE_TYPE, content);
        } catch (IOException ioe) {
            throw new FileStorageException("can't find default company logo image!");
        }
    }

}
