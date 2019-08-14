package com.wetrade.eprest;

import java.util.Collection;

import com.wetrade.assets.PurchaseOrder;

import org.json.JSONObject;

public interface PurchaseOrderService {
    public Collection<PurchaseOrder> getPurchaseOrders() throws Exception;

    public PurchaseOrder getPurchaseOrder(String id) throws Exception;

    public PurchaseOrder getPurchaseOrderByHash(String hash);

    public PurchaseOrder createPurchaseOrder(JSONObject purchaseOrder) throws Exception;

    public void acceptPurchaseOrder(String id) throws Exception;

    public void closePurchaseOrder(String id) throws Exception;
}
