package games.transformers.transformersapi.dao;

import games.transformers.transformersapi.domain.Transformer;

import java.util.List;

/**
 * Interface to provide primary features of a DAO
 */
public interface TransformerDAO {
    /**
     * Returns a transformer if it exists
     * @return A transformer or null;
     */
    Transformer getTransformer(int id);

    /**
     * Provides a list of all available transformers
     * @return A list of transformers
     */
    List<Transformer> getAllTransformers();

    /**
     * Inserts a new transformer in the existing collection of transformers
     * @param transformer   A new transformer to be created
     * @return id of newly created transformer
     */
    int insertTransformer(Transformer transformer);

    /**
     * Updates an existing transformer
     * @param transformer Details of transformer that needs an update
     * @return  true if successfully updated
     */
    boolean updateTransformer(Transformer transformer);

    /**
     * Deletes a transformer that is already present
     * @param id
     * @return  true if successfully deleted
     */
    boolean deleteTransformer(int id);
}
