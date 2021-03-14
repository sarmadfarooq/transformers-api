package games.transformers.transformersapi.service;

import games.transformers.transformersapi.domain.Transformer;

import java.util.List;
import java.util.Optional;

public interface TransformerService {
    /**
     * Provides a lis of all available transformers
     *
     * @return  A transformer
     */
    Optional<Transformer> getTransformer(int id);

    /**
     * Provides a lis of all available transformers
     *
     * @return A list of transformers
     */
    List<Transformer> getAllTransformers();

    /**
     * Inserts a new transformer in the existing collection of transformers
     *
     * @param transformer A new transformer to be created
     * @return id of newly created transformer
     */
    int insertTransformer(Transformer transformer);

    /**
     * Updates an existing transformer
     *
     * @param transformer Details of transformer that needs an update
     * @return true if successfully updated
     */
    boolean updateTransformer(Transformer transformer);

    /**
     * Deletes a transformer that is already present
     *
     * @param id
     * @return true if successfully deleted
     */
    boolean deleteTransformer(long id);
}
