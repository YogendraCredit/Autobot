package pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PartnerCallback {
    String partnerLoanId;
    List<Callback> callbacks;
}
