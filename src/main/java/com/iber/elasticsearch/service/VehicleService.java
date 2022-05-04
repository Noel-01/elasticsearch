package com.iber.elasticsearch.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.iber.elasticsearch.document.Vehicle;
import com.iber.elasticsearch.helper.Indices;
import com.iber.elasticsearch.search.SearchRequestDTO;
import com.iber.elasticsearch.search.util.SearchUtil;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class VehicleService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger       LOG    = LoggerFactory.getLogger(VehicleService.class);

    private final RestHighLevelClient client;

    public VehicleService(RestHighLevelClient client) {
        this.client = client;
    }


    public List<Vehicle> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                dto
        );

        return searchInternal(request);
    }

    public List<Vehicle> getAllVehicleCreatedSince(final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                "created",
                date
        );

        return searchInternal(request);
    }

    private List<Vehicle> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[]   searchHits = response.getHits().getHits();
            final List<Vehicle> vehicles   = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                vehicles.add(
                        MAPPER.readValue(hit.getSourceAsString(), Vehicle.class)
                );
            }
            return vehicles;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Boolean index(final Vehicle vehicle) {
        try {
            final String vehicleAsString = MAPPER.writeValueAsString(vehicle); // mapea el vehiculo a un string

            final IndexRequest request = new IndexRequest(Indices.VEHICLE_INDEX); // Crear una request vacia par guardar con los parametros de tipo vehicle
            request.id(vehicle.getId()); //añade el id de tipo string
            request.source(vehicleAsString, XContentType.JSON); // añade el objeto vehicle en formato string y de type json

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT); // lo guarda y devuelve una respusta para saber si es OK

            return response != null && response.status().equals(RestStatus.OK);

        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public Vehicle getById(final String vehicleId) {
        try {
            // busca el vehicle por id pasandoloe el v_index y el id  y creando un request, hace un get y consigue el vehiculo
            final GetResponse documentFields = client.get(new GetRequest(Indices.VEHICLE_INDEX, vehicleId), RequestOptions.DEFAULT);

            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            // convierte el vehiculo a formato string y luego lo mapera a un objeto vehiculo
            return MAPPER.readValue(documentFields.getSourceAsString(), Vehicle.class);

        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public List<Vehicle> searchCreatedSince(final SearchRequestDTO dto,
                                            final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.VEHICLE_INDEX,
                dto,
                date
        );
        return searchInternal(request);
    }
}
