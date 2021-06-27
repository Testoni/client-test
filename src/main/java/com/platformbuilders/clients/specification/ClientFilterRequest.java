package com.platformbuilders.clients.specification;

import com.platformbuilders.clients.model.Client;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Data
public class ClientFilterRequest extends SpecificationFilter<Client> {

    @ApiModelProperty(value = "Search a name")
    private String searchName;

    @ApiModelProperty(value = "Search from date")
    private String searchFromDate;

    @ApiModelProperty(value = "Search end date")
    private String searchEndDate;

    @ApiModelProperty(value = "Search an email")
    private String searchEmail;

    public Specification<Client> buildSpecification() {
        Specification<Client> specs = Specification.where(null);
        specs = this.filterTerm(specs);
        specs = this.fromDateFilter(specs);
        specs = this.endDateFilter(specs);
        return specs;
    }

    private Specification<Client> filterTerm(Specification<Client> specs) {
        if (this.searchName != null)
            specs = specs.and(containsString(this.searchName, "name"));
        if (this.searchEmail != null)
            specs = specs.and(containsString(this.searchEmail, "email"));
        return specs;
    }

    private Specification<Client> fromDateFilter(Specification<Client> specs) {
        if (this.searchFromDate != null)
            specs = specs.and(greaterThanOrEqualTo(LocalDate.parse(this.searchFromDate), "dateOfBirth"));
        return specs;
    }

    private Specification<Client> endDateFilter(Specification<Client> specs) {
        if (this.searchEndDate != null)
            specs = specs.and(lessThanOrEqualTo(LocalDate.parse(this.searchEndDate), "dateOfBirth"));
        return specs;
    }

}
