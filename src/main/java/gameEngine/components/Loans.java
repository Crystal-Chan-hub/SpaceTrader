package gameEngine.components;

import java.util.List;
import java.util.ArrayList;

public interface Loans {

    public List<String> getAvailableLoans();

    public List<String> requestLoans(String loanType);

    public List<String> getYourLoans();

    public List<String> getYourLoansId();

    public List<String> payOffLoans(String loanId);

}
