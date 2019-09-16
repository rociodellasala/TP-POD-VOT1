//package ar.edu.itba.test;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import ar.edu.itba.Vote;
//import ar.edu.itba.administrationclient.AdministrationClient;
//import ar.edu.itba.fiscalclient.FiscalClient;
//import ar.edu.itba.queryclient.QueryClient;
//import ar.edu.itba.votingclient.VotingClient;
//import ar.edu.itba.utils.Party;
//import ar.edu.itba.utils.Province;
//
//
//public class TestClient {
//
//
//    public static void main(String[] args) throws IOException {
//    	generateCSV(1000,"client/src/main/resources/new.csv");
//        System.setProperty("serverAddress", "127.0.0.1");
//        administration("open");
//        vote("client/src/main/resources/new.csv");
//        query("client/src/main/resources/national_parcial.csv", null, null);
//        query("client/src/main/resources/provincial_parcial.csv", "JUNGLE", null);
//        query("client/src/main/resources/table_parcial.csv", null, "1");
//        administration("close");
//        query("client/src/main/resources/national_final.csv", null, null);
//        query("client/src/main/resources/provincial_final.csv", "JUNGLE", null);
//        query("client/src/main/resources/table_final.csv", null, "0");
//    }
//
//
//    public static void administration(String actionName) {
//        System.setProperty("actionName", actionName);
//        AdministrationClient.main(new String[]{});
//    }
//
//    public static void fiscal(Party p, int id) {
//        System.setProperty("party", p.toString());
//        System.setProperty("id", ""+id);
//        FiscalClient.main(new String[]{});
//    }
//
//    public static void query(String outPath, String province, String table) {
//        System.setProperty("outPath", outPath);
//
//        if(province != null) {
//            System.setProperty("state", province);
//        } else {
//            System.clearProperty("state");
//        }
//        if (table != null) {
//            System.setProperty("id", table);
//        } else {
//            System.clearProperty("id");
//        }
//
//        QueryClient.main(new String[]{});
//    }
//
//    public static void vote(String filePath) {
//        System.setProperty("votesPath", filePath);
//
//        VotingClient.main(new String[]{});
//    }
//
//
//
//    private static List<Vote> generateVotes(int amount, int ballotBox, Province province, Party... parties) {
//        List<Vote> votes = new ArrayList<>();
//        for (int i = 0; i < amount; i++) {
//            votes.add(new Vote(ballotBox, province, Arrays.asList(parties)));
//        }
//
//        return votes;
//    }
//
//
//    private static void generateCSV(int multiplier, String path) throws IOException {
//        Province p = Province.JUNGLE;
//        List<Vote> votes = new ArrayList<>();
//        votes.addAll(generateVotes(multiplier * 15, 0, p, Party.TARSIER, Party.WHITE_GORILLA));
//        votes.addAll(generateVotes(multiplier * 32, 0, p, Party.GORILLA, Party.TARSIER, Party.WHITE_GORILLA));
//        votes.addAll(generateVotes(multiplier * 64, 0, p, Party.GORILLA, Party.WHITE_GORILLA));
//        votes.addAll(generateVotes(multiplier * 9, 0, p, Party.WHITE_GORILLA));
//        votes.addAll(generateVotes(multiplier * 99, 0, p, Party.OWL, Party.TURTLE));
//        votes.addAll(generateVotes(multiplier * 3, 0, p, Party.TURTLE));
//        votes.addAll(generateVotes(multiplier * 3, 0, p, Party.SNAKE, Party.TURTLE));
//        votes.addAll(generateVotes(multiplier * 48, 0, p, Party.TIGER));
//        votes.addAll(generateVotes(multiplier * 12, 0, p, Party.LYNX, Party.TIGER));
//        votes.addAll(generateVotes(multiplier * 6, 0, p, Party.JACKALOPE));
//        votes.addAll(generateVotes(multiplier * 6, 0, p, Party.TIGER, Party.JACKALOPE));
//        votes.addAll(generateVotes(multiplier * 3, 0, p, Party.MONKEY, Party.TURTLE));
//
//        FileWriter csvWriter = new FileWriter(path);
//        for(Vote v: votes) {
//            csvWriter.append("" + v.getBallotBox());
//            csvWriter.append(";");
//            csvWriter.append(v.getProvince().toString());
//            csvWriter.append(";");
//            csvWriter.append(v.getRanking().get(0).toString());
//            for(int i = 1; i < v.getRanking().size();i++) {
//                csvWriter.append(",");
//                csvWriter.append(v.getRanking().get(i).toString());
//            }
//            csvWriter.append("\n");
//        }
//        csvWriter.close();
//    }
//
//}