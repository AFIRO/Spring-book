package be.hogent.springbook.book.mapper;

import be.hogent.springbook.book.entity.Location;
import be.hogent.springbook.book.entity.dto.LocationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static be.hogent.springbook.TestData.getLocation;
import static be.hogent.springbook.TestData.getLocationDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
class LocationMapperTest {
    @InjectMocks
    private LocationMapper mapper;


    @Test
    public void ToDto() {
        LocationDto expected = getLocationDto();
        LocationDto actual = mapper.toDto(getLocation());
        assertThat(actual.getLocationId()).isEqualTo(expected.getLocationId());
        assertThat(actual.getLocationCode1()).isEqualTo(expected.getLocationCode1());
        assertThat(actual.getLocationCode2()).isEqualTo(expected.getLocationCode2());
        assertThat(actual.getLocationName()).isEqualTo(expected.getLocationName());
    }


    @Test
    public void ToEntity() {
        Location expected = getLocation();
        Location actual = mapper.toEntity(getLocationDto());
        assertThat(actual.getLocationId()).isEqualTo(expected.getLocationId());
        assertThat(actual.getLocationCode1()).isEqualTo(expected.getLocationCode1());
        assertThat(actual.getLocationCode2()).isEqualTo(expected.getLocationCode2());
        assertThat(actual.getLocationName()).isEqualTo(expected.getLocationName());
    }
}