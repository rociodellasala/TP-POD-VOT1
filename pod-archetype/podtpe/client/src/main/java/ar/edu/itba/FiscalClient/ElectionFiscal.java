package ar.edu.itba.FiscalClient;

import ar.edu.itba.Detector;
import ar.edu.itba.utils.Party;
import ar.edu.itba.Vote;

public class ElectionFiscal implements Detector {
    private Party party;

    public ElectionFiscal(Party party, int tableId) {
        this.party = party;
    }

    @Override
    public void detect(Vote vote) {
        System.out.println("New vote for " + party.name() + " on polling place " + vote.getTableId());
    }
}