package games.transformers.transformersapi.service;

import games.transformers.transformersapi.domain.FightResponse;
import games.transformers.transformersapi.domain.Transformer;

import java.util.List;

public interface BattleService {
    FightResponse fight(List<Transformer> transformers) throws EndOfGameException;
}
