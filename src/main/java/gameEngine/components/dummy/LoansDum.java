package gameEngine.components.dummy;

import gameEngine.components.User;
import gameEngine.components.Loans;

import java.util.List;
import java.util.ArrayList;

public class LoansDum implements Loans {

    private List<String> avaliableLoans,requestedLoans,yourLoans,yourLoansId,paidLoans;

    public LoansDum() {
        this.yourLoans = new ArrayList<>();
    }

    @Override
    public List<String> getAvailableLoans() {
        this.avaliableLoans = new ArrayList<>();
        avaliableLoans.add("-----------------------");
        avaliableLoans.add("Amount : 200000");
        avaliableLoans.add("Collateral required : No");
        avaliableLoans.add("Rate : 40");
        avaliableLoans.add("Term in days : 2");
        avaliableLoans.add("Loan type : STARTUP");
        return avaliableLoans;
    }

    @Override
    public List<String> requestLoans(String loanType) {
        this.request();
        this.requestedLoans = new ArrayList<>();
        requestedLoans.add("Credits : 200000");
        requestedLoans.add("Due : 2021-05-30 07:01:42.868");
        requestedLoans.add("Loan id : ckno92mhi0049iiopc2kmylkz");
        requestedLoans.add("Repayment Amount : 280000");
        requestedLoans.add("Loan status : CURRENT");
        requestedLoans.add("Loan type : STARTUP");
        return requestedLoans;
    }

    @Override
    public List<String> getYourLoans() {
        return yourLoans;
    }

    @Override
    public List<String> payOffLoans(String loanId) {
        this.paidLoans = new ArrayList<>();
        paidLoans.add("Paid loans");
        return paidLoans;
    }

    @Override
    public List<String> getYourLoansId() {
        this.yourLoansId = new ArrayList<>();
        this.yourLoansId.add("id_of_your_loans");
        return this.yourLoansId;
    }

    private void request() {
        yourLoans.add("--------------LOAN-----------------");
        yourLoans.add("Loan id : ckno92mhi0049iiopc2kmylkz");
        yourLoans.add("Due : 2021-05-30 07:01:42.868");
        yourLoans.add("Repayment Amount : 280000");
        yourLoans.add("Loan status : CURRENT");
        yourLoans.add("Loan type : STARTUP");
    }

}
