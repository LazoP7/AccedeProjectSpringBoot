
package MyProjects.Accede.controllers;
import MyProjects.Accede.entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import MyProjects.Accede.services.LocationService;

@CrossOrigin
@RestController
@RequestMapping({"location"})
public class LocationController {
    @Autowired
    LocationService locationService;

    @PostMapping(
            consumes = {"application/json", "application/xml"}
    )
    public ResponseEntity<Void> newLocation(@RequestBody Location location) {
        this.locationService.newLocation(location);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping({"{id}"})
    public ResponseEntity<Location> getLocation(@PathVariable Integer id) {
        Location location = this.locationService.getLocation(id);
        return location == null ? new ResponseEntity(HttpStatus.NOT_FOUND) : new ResponseEntity(location, HttpStatus.OK);
    }
}