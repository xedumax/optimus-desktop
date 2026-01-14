package com.yobel.optimus.model.response;

import com.yobel.optimus.model.entity.SystemItem;

import java.util.List;

public class SystemResponse {
    private List<SystemItem> systems; // Debe llamarse igual que en el JSON de Postman
    public List<SystemItem> getSystems() { return systems; }
}