package com.iber.elasticsearch.controller;

import com.iber.elasticsearch.document.Vehicle;
import com.iber.elasticsearch.search.SearchRequestDTO;
import com.iber.elasticsearch.service.helper.VehicleDummyDataService;
import com.iber.elasticsearch.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService          service;
    private final VehicleDummyDataService dummyDataService;

    @Autowired
    public VehicleController(VehicleService service,
                             VehicleDummyDataService dummyDataService) {
        this.service          = service;
        this.dummyDataService = dummyDataService;
    }

    @PostMapping
    public void index(@RequestBody final Vehicle vehicle) {
        service.index(vehicle);
    }

    @PostMapping("/insertDummyData")
    public void insertDummyData() {
        dummyDataService.insertDummyData();
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable final String id) {
        return service.getById(id);
    }

    @PostMapping("/search")
    public List<Vehicle> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }

    @GetMapping("/search/{date}")
    public List<Vehicle> getAllVehicleCreatedSince(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd") final Date date) {
        return service.getAllVehicleCreatedSince(date);
    }

    @PostMapping("/searchCreatedSince/{date}")
    public List<Vehicle> searchCreatedSince(@RequestBody final SearchRequestDTO dto,
                                            @PathVariable
                                            @DateTimeFormat(pattern = "yyyy-MM-dd")
                                            final Date date) {
        return service.searchCreatedSince(dto, date);
    }

}
