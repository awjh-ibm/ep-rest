package com.wetrade.eprest;

import java.util.Collection;

import com.wetrade.assets.Shipment;

import org.json.JSONObject;

public interface ShipmentService {
    public Collection<Shipment> getShipments(String behalfOfId) throws Exception;

    public Shipment getShipment(String id) throws Exception;

    public Shipment getShipmentByHash(String id) throws Exception;

    public Shipment createShipment(JSONObject shipment) throws Exception;

    public void deliverShipment(String id) throws Exception;
}
