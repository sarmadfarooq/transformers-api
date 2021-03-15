package games.transformers.transformersapi.service;

import games.transformers.transformersapi.domain.BattleResponse;
import games.transformers.transformersapi.domain.Transformer;
import games.transformers.transformersapi.domain.Transformer.Type;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.min;

@Service
public class BattleServiceImpl implements BattleService {
    public static final Set<String> SPECIAL_TRANSFORMERS = Stream.of("Optimus Prime", "Predaking").collect(Collectors.toCollection(HashSet::new));

    @Override
    public BattleResponse fight(List<Transformer> transformers) throws EndOfGameException {
        //sorting by rank, lower ranks fight first
        List<Transformer> autobots = transformers.stream()
                .filter(transformer -> transformer.getType() == Transformer.Type.A)
                .sorted(Comparator.comparing(Transformer::getRank, Comparator.naturalOrder()))
                .collect(Collectors.toList());
        List<Transformer> decepticons = transformers.stream()
                .filter(transformer -> transformer.getType() == Transformer.Type.D)
                .sorted(Comparator.comparing(Transformer::getRank, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        List<Transformer> autobotWinners = new LinkedList<>();
        List<Transformer> decepticonWinners = new LinkedList<>();
        int maxBattles = min(autobots.size(), decepticons.size());
        for (int i = 0; i < maxBattles; i++) {
            Type winner = fight(autobots.get(i), decepticons.get(i));
            if (winner == Type.A) {
                autobotWinners.add(autobots.get(i));
            } else if (winner == Type.D) {
                decepticonWinners.add(decepticons.get(i));
            }
        }
        BattleResponse battleResponse = prepareWinnerAndSurvivers(autobots, decepticons, autobotWinners, decepticonWinners, maxBattles);

        return battleResponse;
    }

    private Type fight(Transformer transformer1, Transformer transformer2) throws EndOfGameException {

        //check for special cases
        if (SPECIAL_TRANSFORMERS.contains(transformer1.getName()) && SPECIAL_TRANSFORMERS.contains(transformer2.getName())) {
            throw new EndOfGameException("Game End");
        }
        if (SPECIAL_TRANSFORMERS.contains(transformer1.getName())) {
            return transformer1.getType();
        }
        if (SPECIAL_TRANSFORMERS.contains(transformer2.getName())) {
            return transformer2.getType();
        }
        //check if one runs away
        int courageDiff = transformer1.getCourage() - transformer2.getCourage();
        int strengthDiff = transformer1.getStrength() - transformer2.getStrength();
        if (courageDiff >= 4 && strengthDiff >= 3) {
            return transformer1.getType();
        } else if (courageDiff <= -4 && strengthDiff <= -3) {
            return transformer2.getType();
        }
        // more skilled wins
        int skillDiff = transformer1.getSkill() - transformer2.getSkill();
        if (skillDiff >= 3) {
            return transformer1.getType();
        } else if (skillDiff <= -3) {
            return transformer2.getType();
        }
        // winner based on overall rating
        int overallDiff = transformer1.getOverallRating() - transformer2.getOverallRating();
        if (overallDiff > 0) {
            return transformer1.getType();
        } else if (overallDiff < 0) {
            return transformer2.getType();
        }

        return null;
    }

    private BattleResponse prepareWinnerAndSurvivers(List<Transformer> autobots, List<Transformer> decepticons,
                                                     List<Transformer> autobotWinners, List<Transformer> decepticonWinners,
                                                     int maxBattles) {
        List<String> winnerNames;
        List<String> survivorNames;
        List<Transformer> neverFought = new LinkedList<>();
        Type thoseWhoNeverFoughtType = getThoseWhoNeverFought(autobots, decepticons, maxBattles, neverFought);
        List<String> namesOfThoseWhoNeverFought = getNames(neverFought);

        Type winningTeam;
        // decepticons win if they have won more fights otherwise autobots are declared winner
        if (autobotWinners.size() < decepticonWinners.size()) {
            winnerNames = getNames(decepticonWinners);
            survivorNames = getNames(autobotWinners);
            //Decepticons have won but they also have few who did not fight. We will treat them as winners
            appendWinnersOrSurvivers(winnerNames, thoseWhoNeverFoughtType, namesOfThoseWhoNeverFought, survivorNames, Type.D);
            winningTeam = Type.D;
        } else {
            winnerNames = getNames(autobotWinners);
            survivorNames = getNames(decepticonWinners);
            //Autobots have won but they also have few who did not fight. We will treat them as winners
            appendWinnersOrSurvivers(winnerNames, thoseWhoNeverFoughtType, namesOfThoseWhoNeverFought, survivorNames, Type.A);
            winningTeam = Type.A;
        }
        return BattleResponse.builder().battleCount(maxBattles).winningTeam(winningTeam).winners(winnerNames).survivors(survivorNames).build();
    }

    private void appendWinnersOrSurvivers(List<String> winnerNames, Type thoseWhoNeverFoughtType, List<String> namesOfThoseWhoNeverFought, List<String> survivorNames, Type winnerType) {
        if (thoseWhoNeverFoughtType == winnerType) {
            winnerNames.addAll(namesOfThoseWhoNeverFought);
        } else {
            survivorNames.addAll(namesOfThoseWhoNeverFought);
        }
    }

    private Type getThoseWhoNeverFought(List<Transformer> autobots, List<Transformer> decepticons, int maxBattles, List<Transformer> neverFought) {
        if (autobots.size() > decepticons.size()) {
            neverFought.addAll(autobots.subList(maxBattles, autobots.size() ));
            return Type.A;
        } else {
            neverFought.addAll(decepticons.subList(maxBattles, decepticons.size()));
            return Type.D;
        }
    }

    private List<String> getNames(List<Transformer> decepticonWinners) {
        return decepticonWinners.stream().map(transformer -> transformer.getName()).collect(Collectors.toList());
    }
}
