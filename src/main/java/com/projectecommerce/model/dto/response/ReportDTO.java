package com.projectecommerce.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Integer timeUnit;
    private Number totalAmount;
}