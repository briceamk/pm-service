package cm.gelodia.pm.commons.payload;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResource implements Serializable {

    public static final long serialVersionUID = -2721638500393397147L;

    private String id;
    private String imageName;
    private String imageType;
    private byte[] image;
}
