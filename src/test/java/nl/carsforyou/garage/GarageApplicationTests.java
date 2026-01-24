package nl.carsforyou.garage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ActiveProfiles("test")
@SpringBootTest
@ComponentScan(
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = nl.carsforyou.garage.config.SecurityConfigOauth2.class
    )
)

class GarageApplicationTests {
    @Test
    void contextLoads() {}
}
