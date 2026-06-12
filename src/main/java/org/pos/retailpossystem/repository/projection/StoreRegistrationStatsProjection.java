package org.pos.retailpossystem.repository.projection;

import java.time.LocalDate;

public interface StoreRegistrationStatsProjection {
    LocalDate getDate();
    Long getCount();
}