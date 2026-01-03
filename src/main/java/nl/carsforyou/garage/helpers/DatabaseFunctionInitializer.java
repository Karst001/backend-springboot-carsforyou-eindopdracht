package nl.carsforyou.garage.helpers;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

//this helper creates a stored function that is called to report the number of visits for a customer
//instead of having this inline SQL inside the repository I personally find this cleaner coding practise
@Configuration
public class DatabaseFunctionInitializer {

    //source Stackoverflow
    //Creates/updates the PostgreSQL reporting function at app startup.
    //This runs AFTER Hibernate created the tables, so it works with ddl-auto=create.
    @Bean
    ApplicationRunner createCustomerVisitCostsFunction(JdbcTemplate jdbcTemplate) {
        return args -> {
            String sql = """
                CREATE OR REPLACE FUNCTION get_customer_visit_costs(p_customer_id bigint)
                RETURNS TABLE (
                    service_order_id bigint,
                    vehicle_id bigint,
                    service_completed_date timestamp,
                    total_cost numeric
                )
                LANGUAGE sql
                AS $$
                    SELECT
                        so.service_order_id,
                        so.vehicle_id,
                        so.service_completed_date,
                        COALESCE(SUM(sop.qty_used * sop.unit_price), 0) AS total_cost
                    FROM service_orders so
                    JOIN vehicles v ON v.vehicle_id = so.vehicle_id
                    JOIN customers c ON c.customer_id = v.customer_id
                    LEFT JOIN service_order_parts sop
                           ON sop.service_order_id = so.service_order_id
                    WHERE c.customer_id = p_customer_id
                        AND so.service_completed_date IS NOT NULL
                    GROUP BY so.service_order_id, so.vehicle_id, so.service_completed_date
                    ORDER BY so.service_completed_date DESC NULLS LAST,
                             so.service_order_id DESC
                $$;
                """;

            jdbcTemplate.execute(sql);
        };
    }
}

