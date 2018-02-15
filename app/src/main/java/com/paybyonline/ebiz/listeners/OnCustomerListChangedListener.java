package com.paybyonline.ebiz.listeners;



import com.paybyonline.ebiz.Adapter.Model.DashboarGridModel;

import java.util.List;


public interface OnCustomerListChangedListener {
    void onNoteListChanged(List<DashboarGridModel> dashboarGridModels);
}