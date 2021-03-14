package games.transformers.transformersapi.service;

import games.transformers.transformersapi.domain.BattleResponse;
import games.transformers.transformersapi.domain.Transformer;

import java.util.List;

public interface BattleService {
    BattleResponse fight(List<Transformer> transformers) throws EndOfGameException;
}
