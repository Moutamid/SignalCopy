package com.moutamid.signalcopy.listeners;


import com.moutamid.signalcopy.model.MessageModel;

public interface DeleteListener {
    void onHoldClick(MessageModel messageModel);

    void onEdit(MessageModel messageModel);
}
