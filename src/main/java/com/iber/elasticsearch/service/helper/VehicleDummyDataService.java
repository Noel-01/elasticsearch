package com.iber.elasticsearch.service.helper;

import com.iber.elasticsearch.document.Vehicle;
import com.iber.elasticsearch.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class VehicleDummyDataService {

    private static final Logger           LOG         = LoggerFactory.getLogger(VehicleDummyDataService.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final VehicleService vehicleService;


    public VehicleDummyDataService(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    public void insertDummyData() {
        vehicleService.index(buildVehicle("1", "Audi A1", "AAB-123", "2010-01-01"));
        vehicleService.index(buildVehicle("2", "Audi A2", "AAC-123", "2011-02-02"));
        vehicleService.index(buildVehicle("3", "Audi A3", "AAD-123", "2012-03-03"));

        vehicleService.index(buildVehicle("4", "Audi A4", "AAE-123", "2013-04-04"));
        vehicleService.index(buildVehicle("5", "Audi A5", "AAF-123", "2014-05-05"));
        vehicleService.index(buildVehicle("6", "Audi A6", "AAG-123", "2015-06-06"));

        vehicleService.index(buildVehicle("7", "Audi A7", "AAH-123", "2016-07-07"));
        vehicleService.index(buildVehicle("8", "Audi A8", "AAI-123", "2017-08-08"));

        vehicleService.index(buildVehicle("9", "Audi A9", "AAJ-123", "2018-09-09"));
        vehicleService.index(buildVehicle("10", "Audi A10", "AAK-123", "2019-10-10"));
    }

    private static Vehicle buildVehicle(final String id,
                                        final String name,
                                        final String number,
                                        final String date) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(id);
        vehicle.setName(name);
        vehicle.setNumber(number);

        try {
            vehicle.setCreated(DATE_FORMAT.parse(date));
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return vehicle;
    }
}
