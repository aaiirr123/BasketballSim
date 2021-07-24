package basketballSim;

import java.util.*;
import java.io.*;

public class TeamReader {

    public static Roster getTeam(String fileName) throws FileNotFoundException {
        String splitBy = ",";

        try {
            //parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String teamLine = br.readLine();
            System.out.println(teamLine);
            String[] teamInfo = teamLine.split(splitBy);
            String coachLine = br.readLine();
            String[] coachInfo = coachLine.split(splitBy);
            int numPlayers = Integer.parseInt(teamInfo[1]);
            Player players[] = new Player[numPlayers];
            String nextPlayer;
            // Throw away comment readLine
            br.readLine();
            // Player Info

            for (int i = 0; i < numPlayers; i++) {
                nextPlayer = br.readLine();
                String[] playerInfo = nextPlayer.split(splitBy);
                for (String out : playerInfo) System.out.print(out + " ");
                System.out.println();

                int[] physical = new int[3];
                int[] athletic = new int[4];
                int[] skill = new int[11];

                int total = 2;

                for (int j = 0; j < 3; j++, total++) physical[j] = Integer.parseInt(playerInfo[total]);
                for (int j = 0; j < 4; j++, total++) athletic[j] = Integer.parseInt(playerInfo[total]);
                for (int j = 0; j < 11; j++, total++) skill[j] = Integer.parseInt(playerInfo[total]);

                players[i] = new Player(playerInfo[0], physical, athletic, skill, getParsedPos(playerInfo[1]));

            }


            Roster newTeam = new Roster(Roster.teamSide.HOME, Integer.parseInt(teamInfo[1]), teamInfo[0], new Coach(coachInfo[0]), players);

            return newTeam;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Roster(Roster.teamSide.HOME, 0, "Null", null);
    }

//    public static Roster writeTeam(String fileName) throws IOException {
//
//        List<List<String>> rows = Arrays.asList(
//                Arrays.asList("Jean", "author", "Java"),
//                Arrays.asList("David", "editor", "Python"),
//                Arrays.asList("Scott", "editor", "Node.js")
//        );
//
//        FileWriter csvWriter = new FileWriter("new.csv");
//        csvWriter.append("Name");
//        csvWriter.append(",");
//        csvWriter.append("Role");
//        csvWriter.append(",");
//        csvWriter.append("Topic");
//        csvWriter.append("\n");
//
//        for (List<String> rowData : rows) {
//            csvWriter.append(String.join(",", rowData));
//            csvWriter.append("\n");
//        }
//
//        csvWriter.flush();
//        csvWriter.close();
//    }

    private static Player.Position getParsedPos(String parsedPos) {
        if (parsedPos == "PG") return Player.Position.PG;
        if (parsedPos == "SG") return Player.Position.SG;
        if (parsedPos == "SF") return Player.Position.SF;
        if (parsedPos == "PF") return Player.Position.PF;
        else return Player.Position.C;
    }

}
