package andiag.coru.es.welegends.DTOs.championsDTOs;

import java.io.Serializable;

/**
 * Created by Iago on 25/07/2015.
 */
public class PassiveDto implements Serializable {
    private String description;
    private ImageDto image;
    private String name;
    private String sanitizedDescription;

    public PassiveDto() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSanitizedDescription() {
        return sanitizedDescription;
    }

    public void setSanitizedDescription(String sanitizedDescription) {
        this.sanitizedDescription = sanitizedDescription;
    }
}
