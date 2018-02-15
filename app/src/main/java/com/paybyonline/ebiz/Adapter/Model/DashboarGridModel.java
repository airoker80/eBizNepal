package com.paybyonline.ebiz.Adapter.Model;

/**
 * Created by hp on 5/31/2017.
 */

public class DashboarGridModel {
    private Long id;
    private int imageId;
    private String iconName;

    public DashboarGridModel() {
    }

    public DashboarGridModel(Long id, int imageId, String iconName) {
        this.id = id;
        this.imageId = imageId;
        this.iconName = iconName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
