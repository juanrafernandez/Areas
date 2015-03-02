package com.areas.jr.areas;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by 108001 on 02/03/2015.
 */
public class AreaInfo {
    int id;
    String name;
    int situation; //0:pie de carretera, 1:desvio corto, 2:desvio largo
    int gas_station; //0:no, 1:si
    int garage; //0:no, 1:si
    int restaurant; //0:no 1:si
    LatLng position; //latitud y longitud del area de servicio
}
