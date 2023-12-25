
package MyProjects.Accede.controllers;
import MyProjects.Accede.dto.location.LocationDTO;
import MyProjects.Accede.entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import MyProjects.Accede.services.LocationService;

import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping({"location"})
public class LocationController {
    @Autowired
    LocationService locationService;

    @PostMapping("newLocation")
    public ResponseEntity<Void> newLocation(@RequestBody LocationDTO locationDTO) {
        this.locationService.newLocation(locationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<Location> getLocation(@PathVariable Integer id) {
        Location location = this.locationService.getLocation(id);
        return location == null ? new ResponseEntity(HttpStatus.NOT_FOUND) : new ResponseEntity(location, HttpStatus.OK);
    }

    @GetMapping("getAllLocations")
    public ResponseEntity<ArrayList<LocationDTO>> getAllLocations(){
        ArrayList<LocationDTO> locations = this.locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
}