package com.example.cameltask.other;

import com.example.cameltask.model.IncomingOrder;

public class OnlineSalesChannelFilter {
    public boolean isOnline(IncomingOrder order) {
        return order.getSalesChannel().equalsIgnoreCase("online");
    }
}
