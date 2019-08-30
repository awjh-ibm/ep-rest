package com.wetrade.eprest.controllers;

import java.text.DateFormat;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wetrade.assets.Shipment;
import com.wetrade.common.FabricProxyException;
import com.wetrade.utils.BaseResponse;
import com.wetrade.utils.ResponseStatus;
import com.wetrade.eprest.ShipmentService;

import org.json.JSONObject;

import spark.Spark;

public class ShipmentController {
    public ShipmentController(ShipmentService service) {
        String format = "EEE MMM d HH:mm:ss Z yyy";
        Gson gson = new GsonBuilder().setDateFormat(format).create();


        Spark.get("/api/shipments", (req, res) -> {
            BaseResponse response;
            Collection<Shipment> shipments;
            String user = new JSONObject(req.body()).getString("user");

            try {
                shipments = (Collection<Shipment>) service.getShipments(user);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(shipments));
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });

        Spark.get("/api/shipments/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params(":id");
            String user = new JSONObject(req.body()).getString("user");

            Shipment shipment = service.getShipment(id);

            BaseResponse response;
            if (shipment == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Shipment not found with ID: " + id);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(shipment));
            }
            return gson.toJson(response);
        });

        Spark.get("/api/shipments/hash/:hash", (req, res) -> {
            res.type("application/json");
            String hash = req.params(":hash");
            String user = new JSONObject(req.body()).getString("user");

            Shipment shipment = service.getShipmentByHash(hash);
            BaseResponse response;
            if (shipment == null) {
                response = new BaseResponse(ResponseStatus.ERROR, "Shipment not found with hash: " + hash);
            } else {
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(shipment));
            }
            return gson.toJson(response);
        });

        Spark.post("/api/shipments", (req, res) -> {
            res.type("application/json");
            String body = req.body();
            String user = new JSONObject(req.body()).getString("user");

            BaseResponse response;
            JSONObject shipmentJson = new JSONObject(body);
            try {
                Shipment shipment = service.createShipment(shipmentJson);
                response = new BaseResponse(ResponseStatus.SUCCESS, gson.toJsonTree(shipment));
            } catch (FabricProxyException exception) {
                exception.printStackTrace();
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });

        Spark.put("/api/shipment/:id/deliver", (req, res) -> {
            res.type("application/json");
            String shipmentId = req.params(":id");
            String user = new JSONObject(req.body()).getString("user");

            BaseResponse response;
            try {
                service.deliverShipment(shipmentId);
                response = new BaseResponse(ResponseStatus.SUCCESS);
            } catch (FabricProxyException exception) {
                response = new BaseResponse(ResponseStatus.ERROR, gson.toJsonTree(exception));
            }
            return gson.toJson(response);
        });
    }
}
