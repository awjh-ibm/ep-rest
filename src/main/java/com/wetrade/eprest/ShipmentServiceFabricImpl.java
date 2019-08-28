package com.wetrade.eprest;

import java.util.Arrays;
import java.util.Collection;

import com.google.gson.Gson;
import com.wetrade.assets.Shipment;
import com.wetrade.common.FabricProxy;
import com.wetrade.common.FabricProxyConfig;
import com.wetrade.common.FabricProxyException;

import org.json.JSONObject;

public class ShipmentServiceFabricImpl implements ShipmentService {
    private String subContractName = "ShipmentContract";
    private FabricProxy proxy;

    public ShipmentServiceFabricImpl(FabricProxyConfig config) throws FabricProxyException {
        this.proxy = new FabricProxy(config);
    }

    @Override
    public Shipment createShipment(JSONObject shipment) throws FabricProxyException{
        String purchaseOrderId = shipment.getString("purchaseOrderId");
        String units = String.valueOf(shipment.getInt("units"));
        String senderId = shipment.getString("senderId");
        String reveiverId = shipment.getString("receiverId");

        Gson gson = new Gson();
        String fcn = "createShipment";
        String response = this.proxy.submitTransaction("admin", subContractName, fcn, purchaseOrderId, units, senderId, reveiverId);
        Shipment newShipment = gson.fromJson(response, Shipment.class);
        return newShipment;
    }

    @Override
    public void deliverShipment(String id) throws FabricProxyException {
        String fcn = "deliveredShipment";
        this.proxy.evaluateTransaction("admin", subContractName, fcn, id);
    }

    @Override
    public Shipment getShipment(String id) throws FabricProxyException {
        Gson gson = new Gson();
        String fcn = "getShipment";
        String response = this.proxy.evaluateTransaction("admin", subContractName, fcn, id);
        Shipment shipment = gson.fromJson(response, Shipment.class);
        return shipment;
    }

    @Override
    public Shipment getShipmentByHash(String hash) throws FabricProxyException {
        Gson gson = new Gson();
        String fcn = "getShipmentByHash";
        String response = this.proxy.evaluateTransaction("admin", subContractName, fcn, hash);
        Shipment shipment = gson.fromJson(response, Shipment.class);
        return shipment;
    }

    @Override
    public Collection<Shipment> getShipments(String behalfOfId) throws FabricProxyException {
        Gson gson = new Gson();
        String fcn = "getShipments";
        String response = this.proxy.evaluateTransaction("admin", subContractName, fcn, behalfOfId);
        Shipment[] shipments = gson.fromJson(response, Shipment[].class);
        return Arrays.asList(shipments);
    }
}
