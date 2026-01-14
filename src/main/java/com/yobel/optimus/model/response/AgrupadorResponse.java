package com.yobel.optimus.model.response;

import com.yobel.optimus.model.entity.Agrupador;

import java.util.List;

public class AgrupadorResponse {
    private List<Agrupador> data;
    private boolean success;
    public List<Agrupador> getData() { return data; }
}