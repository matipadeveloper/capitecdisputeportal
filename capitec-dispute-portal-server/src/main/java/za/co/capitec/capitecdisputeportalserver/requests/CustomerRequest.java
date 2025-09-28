package za.co.capitec.capitecdisputeportalserver.requests;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String surname;
    private String idNumber;
    private String email;
}
