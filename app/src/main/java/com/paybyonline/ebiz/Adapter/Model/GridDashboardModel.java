package com.paybyonline.ebiz.Adapter.Model;

import android.graphics.Bitmap;


public class GridDashboardModel {
    public String label;
    public Bitmap icons;

    public GridDashboardModel(String label, Bitmap icons) {
        this.label = label;
        this.icons = icons;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Bitmap getIcons() {
        return icons;
    }

    public void setIcons(Bitmap icons) {
        this.icons = icons;
    }
}
