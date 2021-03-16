package games.transformers.transformersapi.service;

import games.transformers.transformersapi.domain.FightResponse;
import games.transformers.transformersapi.domain.Transformer;
import games.transformers.transformersapi.domain.TransformerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BattleServiceImplTest {
    BattleServiceImpl battleService;

    @BeforeEach
    public void setup() {
        battleService = new BattleServiceImpl();
    }

    @Test
    void shouldBeWonByAutoBotBecauseThereIsNoDecepticon() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);

        FightResponse response = battleService.fight(Arrays.asList(autobot));
        assertEquals(0, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Auto1", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByAutoBotBecauseDecepticonRanAway() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        autobot.setCourage(decepticon.getCourage() + 4);
        autobot.setStrength(decepticon.getStrength() + 3);

        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Auto1", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByAutoBotBecauseDecepticonIsLessSkilled() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        autobot.setSkill(decepticon.getSkill() + 4);

        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Auto1", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByDecepticonBecauseAutoBotIsLessSkilled() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        decepticon.setSkill(autobot.getSkill() + 4);

        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.D, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Dec", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByAutoBotBecauseDecepticonHasLowRating() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        autobot.setFirepower(decepticon.getFirepower() + 1);

        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Auto1", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByDecepticonBecauseAutoBotHasLowRating() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        decepticon.setFirepower(autobot.getFirepower() + 1);

        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.D, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Dec", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeATie() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);

        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(0, response.getWinners().size());
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByAutobotWithoutDecepticonSurvivor() throws EndOfGameException {
        Transformer autobot1 = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        autobot1.setRank(1);
        autobot1.setStrength(10);
        Transformer autobot2 = TransformerTest.buildTransformer("Auto2", Transformer.Type.A);
        autobot2.setRank(2);
        autobot2.setStrength(3);
        Transformer decepticon1 = TransformerTest.buildTransformer("Dec1", Transformer.Type.D);
        decepticon1.setRank(2);
        decepticon1.setStrength(2);
        Transformer decepticon2 = TransformerTest.buildTransformer("Dec2", Transformer.Type.D);
        decepticon2.setRank(1);
        decepticon2.setStrength(8);

        FightResponse response = battleService.fight(Arrays.asList(autobot1, autobot2, decepticon1, decepticon2));
        assertEquals(2, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(2, response.getWinners().size());
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByAutobotWithDecepticonSurvivor() throws EndOfGameException {
        Transformer autobot1 = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        autobot1.setRank(1);
        autobot1.setStrength(10);
        Transformer autobot2 = TransformerTest.buildTransformer("Auto2", Transformer.Type.A);
        autobot2.setRank(2);
        autobot2.setStrength(3);
        Transformer decepticon1 = TransformerTest.buildTransformer("Dec1", Transformer.Type.D);
        decepticon1.setRank(2);
        decepticon1.setStrength(2);
        Transformer decepticon2 = TransformerTest.buildTransformer("Dec2", Transformer.Type.D);
        decepticon2.setRank(1);
        decepticon2.setStrength(8);
        Transformer decepticon3 = TransformerTest.buildTransformer("DecSurvivor", Transformer.Type.D);
        decepticon3.setRank(11);
        decepticon3.setStrength(8);

        FightResponse response = battleService.fight(Arrays.asList(autobot1, autobot2, decepticon1, decepticon2, decepticon3));
        assertEquals(2, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(2, response.getWinners().size());
        assertEquals(1, response.getSurvivors().size());
        assertEquals("DecSurvivor", response.getSurvivors().get(0));
    }

    @Test
    void shouldBeWonByAutobotWithDecepticonSurvivorAutobotsMoreInNumber() throws EndOfGameException {
        //winning auto
        Transformer autobot1 = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        autobot1.setRank(1);
        autobot1.setEndurance(10);
        //loosing auto
        Transformer autobot2 = TransformerTest.buildTransformer("Auto2", Transformer.Type.A);
        autobot2.setRank(2);
        autobot2.setEndurance(3);
        Transformer autobot3 = TransformerTest.buildTransformer("Auto3", Transformer.Type.A);
        autobot3.setRank(3);
        autobot3.setEndurance(13);
        Transformer decepticon1 = TransformerTest.buildTransformer("Dec1", Transformer.Type.D);
        decepticon1.setRank(2);
        decepticon1.setEndurance(2);
        Transformer decepticon2 = TransformerTest.buildTransformer("Dec2", Transformer.Type.D);
        decepticon2.setRank(1);
        decepticon2.setEndurance(8);

        FightResponse response = battleService.fight(Arrays.asList(autobot1, autobot2, autobot3, decepticon1, decepticon2));
        assertEquals(2, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(3, response.getWinners().size());
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByDecepticonAutobotRanAwaySurvivorAutobotsMoreInNumber() throws EndOfGameException {
        //This runs away
        Transformer autobot1 = TransformerTest.buildTransformer("Auto1", Transformer.Type.A);
        autobot1.setRank(1);
        autobot1.setEndurance(5);
        //This shall win
        Transformer autobot2 = TransformerTest.buildTransformer("Auto2", Transformer.Type.A);
        autobot2.setRank(2);
        autobot2.setEndurance(13);
        //This shall loose
        Transformer autobot3 = TransformerTest.buildTransformer("Auto3", Transformer.Type.A);
        autobot3.setRank(3);
        autobot3.setEndurance(3);
        //This shall loose
        Transformer autobot4 = TransformerTest.buildTransformer("Auto4", Transformer.Type.A);
        autobot4.setRank(4);
        autobot4.setEndurance(3);
        Transformer autobot5 = TransformerTest.buildTransformer("AutoSurvivor", Transformer.Type.A);
        autobot5.setRank(5);
        autobot5.setEndurance(3);
        Transformer decepticon1 = TransformerTest.buildTransformer("Dec1", Transformer.Type.D);
        decepticon1.setRank(1);
        decepticon1.setEndurance(12);
        decepticon1.setCourage(autobot1.getCourage() + 5);
        decepticon1.setStrength(autobot1.getStrength() + 5);
        Transformer decepticon2 = TransformerTest.buildTransformer("Dec2", Transformer.Type.D);
        decepticon2.setRank(2);
        decepticon2.setEndurance(8);
        Transformer decepticon3 = TransformerTest.buildTransformer("Dec3", Transformer.Type.D);
        decepticon3.setRank(3);
        decepticon3.setEndurance(8);
        Transformer decepticon4 = TransformerTest.buildTransformer("Dec4", Transformer.Type.D);
        decepticon4.setRank(4);
        decepticon4.setEndurance(8);

        FightResponse response = battleService.fight(Arrays.asList(autobot1, autobot2, autobot3, autobot4, autobot5, decepticon1, decepticon2, decepticon3, decepticon4));
        assertEquals(4, response.getBattleCount());
        assertEquals(Transformer.Type.D, response.getWinningTeam());
        assertEquals(3, response.getWinners().size());
        assertEquals(2, response.getSurvivors().size());
        assertEquals("Auto2", response.getSurvivors().get(0));
        assertEquals("AutoSurvivor", response.getSurvivors().get(1));
    }

    @Test
    void shouldBeWonByOptimusPrime() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Optimus Prime", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Dec", Transformer.Type.D);
        decepticon.setCourage(autobot.getCourage() + 4);
        decepticon.setStrength(autobot.getStrength() + 3);
        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.A, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Optimus Prime", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldBeWonByPredaking() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Auto", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Predaking", Transformer.Type.D);
        decepticon.setCourage(autobot.getCourage() + 4);
        decepticon.setStrength(autobot.getStrength() + 3);
        FightResponse response = battleService.fight(Arrays.asList(autobot, decepticon));
        assertEquals(1, response.getBattleCount());
        assertEquals(Transformer.Type.D, response.getWinningTeam());
        assertEquals(1, response.getWinners().size());
        assertEquals("Predaking", response.getWinners().get(0));
        assertEquals(0, response.getSurvivors().size());
    }

    @Test
    void shouldGetException() throws EndOfGameException {
        Transformer autobot = TransformerTest.buildTransformer("Optimus Prime", Transformer.Type.A);
        Transformer decepticon = TransformerTest.buildTransformer("Predaking", Transformer.Type.D);
        assertThrows(EndOfGameException.class, () -> battleService.fight(Arrays.asList(autobot, decepticon)));
    }
}