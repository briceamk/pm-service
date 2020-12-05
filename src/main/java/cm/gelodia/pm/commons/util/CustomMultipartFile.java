package cm.gelodia.pm.commons.util;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@NoArgsConstructor
@Setter
public class CustomMultipartFile implements MultipartFile {
    private String name;
    private String originalName;
    private String type;
    private byte[] content;

    public CustomMultipartFile(String name, String originalName, String type, byte[] content) {
        this.name = name;
        this.originalName = originalName;
        this.type = type;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalName;
    }

    @Override
    public String getContentType() {
        return type;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(content);
    }
}
