package com.paybyonline.ebiz.Adapter.Model;

/**
 * Created by Sameer on 1/11/2018.
 */

public class FavouriteModel {

    private String name, id;
    private String logo;
    private boolean isPreferred, isUnpreferred, isRemaining, isSelected;

    public FavouriteModel(String name, String id, String logo, boolean isPreferred, boolean isUnpreferred, boolean isRemaining) {
        this.name = name;
        this.id = id;
        this.logo = logo;
        this.isPreferred = isPreferred;
        this.isUnpreferred = isUnpreferred;
        this.isRemaining = isRemaining;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public boolean isPreferred() {
        return isPreferred;
    }

    public boolean isUnpreferred() {
        return isUnpreferred;
    }

    public boolean isRemaining() {
        return isRemaining;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
