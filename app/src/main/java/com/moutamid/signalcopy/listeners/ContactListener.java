package com.moutamid.signalcopy.listeners;

import com.moutamid.signalcopy.model.ContactsModel;

public interface ContactListener {
    void onCLick(ContactsModel model);

    void onDelete(ContactsModel model, int pos);

}
