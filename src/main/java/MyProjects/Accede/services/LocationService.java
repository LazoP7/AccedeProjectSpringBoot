
package MyProjects.Accede.services;

import MyProjects.Accede.entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MyProjects.Accede.repositories.LocationRepository;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;

    public LocationService() {
    }

    public void newLocation(Location location) {
        this.locationRepository.save(location);
    }

    public Location getLocation(Integer id) {
        return this.locationRepository.getLocation(id);
    }
}
