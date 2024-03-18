
package MyProjects.Accede.services;

import MyProjects.Accede.dto.location.LocationDTO;
import MyProjects.Accede.entities.Location;
import MyProjects.Accede.map.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MyProjects.Accede.repositories.LocationRepository;

import java.util.ArrayList;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationMapper locationMapper;

    public void newLocation(LocationDTO locationDTO) //service for creating new locations
    {
        Location location = locationMapper.locationDTOToLocation(locationDTO);
        this.locationRepository.save(location);
    }

    public Location getLocation(Integer id) //service for getting a location. Returns a location object
    {
        return this.locationRepository.getLocation(id);
    }

    public ArrayList<LocationDTO> getAllLocations() //service for getting all locations. Returns a list of location objects
    {
        ArrayList<Location> locations = locationRepository.getAllLocations();
        return locationMapper.locationsToLocationsDTO(locations);
    }
}
