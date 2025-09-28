package za.co.capitec.capitecdisputeportalserver.responses;

import lombok.Data;

@Data
public class CustomerResponse {
    private String idNumber;
    private String name;
    private String surname;
    private String email;
}
