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

    public static void writeTeam(String teamName, String coachName, ArrayList<Player> players) throws IOException {

        FileWriter csvWriter = new FileWriter(teamName);
        csvWriter.append(teamName + "," + players.size() + "\n");
        csvWriter.append(coachName + "\n");
        csvWriter.append("// Name : Position : Height : Weight : WingSpan : Vertical : Speed : Strength : midRange : pullupMidRange : threePoint : pullupThreePoint : drivingLayup : dunkingAbility : freethrowShooting : passing : ballHandling : rebound\n");


        for (Player player : players) {
            List playerLine = Arrays.asList(player.getName() + player.getPosistion() + player.getHeight(),
                    player.getWeight(), player.getWingSpan(), player.getVertical(), player.getSpeed(),
                    player.getStrength(), player.getShooting(gameSim.shotType.SET_MID_RANGE),
                    player.getShooting(gameSim.shotType.PULLUP_MID_RANGE), player.getShooting(gameSim.shotType.SET_THREE),
                    player.getShooting(gameSim.shotType.PULLUP_THREE), player.getShooting(gameSim.shotType.LAYUP),
                    player.getShooting(gameSim.shotType.DUNK), player.getShooting(gameSim.shotType.FREE_THROW),
                    player.getPassingAbility(), player.getBallHandling() ,player.getReboundAbility()
                    );
            csvWriter.append(String.join(",",playerLine));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    public static void writeRandomTeam(String teamName, String coachName, int numPlayers) throws IOException {

        FileWriter csvWriter = new FileWriter("src/teams/" + teamName);
        csvWriter.append(teamName + "," + numPlayers + "\n");
        csvWriter.append(coachName + "\n");
        csvWriter.append("// Name : Position : Height : Weight : WingSpan : Fatigue : Max Fatigue : Vertical : Speed : Strength : midRange : pullupMidRange : threePoint : pullupThreePoint : drivingLayup : dunkingAbility : freethrowShooting : passing : ballHandling : Defense : rebound\n");
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(Person.randomName());
            List playerLine = Arrays.asList(player.getName(), player.getPosistion().toString(), Integer.toString(player.getHeight()),
                    Integer.toString(player.getWeight()), Integer.toString(player.getWingSpan()), Integer.toString(player.getReboundAbility()),
                    Integer.toString(player.getMaxFatigue()), Integer.toString(player.getMaxFatigue()), Integer.toString(player.getVertical()),
                    Integer.toString(player.getSpeed()), Integer.toString(player.getStrength()), Integer.toString(player.getShooting(gameSim.shotType.SET_MID_RANGE)),
                    Integer.toString(player.getShooting(gameSim.shotType.PULLUP_MID_RANGE)), Integer.toString(player.getShooting(gameSim.shotType.SET_THREE)),
                    Integer.toString(player.getShooting(gameSim.shotType.PULLUP_THREE)), Integer.toString(player.getShooting(gameSim.shotType.LAYUP)),
                    Integer.toString(player.getShooting(gameSim.shotType.DUNK)), Integer.toString(player.getShooting(gameSim.shotType.FREE_THROW)),
                    Integer.toString(player.getPassingAbility()), Integer.toString(player.getBallHandling()), Integer.toString(player.getDefense()) ,Integer.toString(player.getReboundAbility())
            );
            csvWriter.append(String.join(",",playerLine));
            csvWriter.append("\n");
        }

        csvWriter.flush();
        csvWriter.close();
    }

    private static Player.Position getParsedPos(String parsedPos) {
        if (parsedPos == "PG") return Player.Position.PG;
        if (parsedPos == "SG") return Player.Position.SG;
        if (parsedPos == "SF") return Player.Position.SF;
        if (parsedPos == "PF") return Player.Position.PF;
        else return Player.Position.C;
    }

}
