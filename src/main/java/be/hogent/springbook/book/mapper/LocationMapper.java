package be.hogent.springbook.book.mapper;

import be.hogent.springbook.book.entity.Location;
import be.hogent.springbook.book.entity.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {
    public LocationDto toDto(Location data){
        return LocationDto.builder()
                .locationId(data.getLocationId())
                .locationCode1(data.getLocationCode1())
                .locationCode2(data.getLocationCode2())
                .locationName(data.getLocationName())
                .build();
    }

    public Location toEntity(LocationDto data){
        return Location.builder()
                .locationId(data.getLocationId())
                .locationCode1(data.getLocationCode1())
                .locationCode2(data.getLocationCode2())
                .locationName(data.getLocationName())
                .build();
    }
}
