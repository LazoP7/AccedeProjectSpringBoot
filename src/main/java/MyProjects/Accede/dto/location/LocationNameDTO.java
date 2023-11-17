
package MyProjects.Accede.dto.location;

import lombok.Data;

@Data
public class LocationNameDTO {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}

