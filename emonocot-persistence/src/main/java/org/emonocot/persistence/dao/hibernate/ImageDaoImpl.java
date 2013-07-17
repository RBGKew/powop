package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Image;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.ImageDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class ImageDaoImpl extends DaoImpl<Image> implements ImageDao {

   /**
    *
    */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("image-page", new Fetch[] {
                new Fetch("taxon", FetchMode.JOIN),
                new Fetch("authority", FetchMode.JOIN),
                new Fetch("comments", FetchMode.SELECT)
                });
        FETCH_PROFILES.put("image-taxon", new Fetch[] {
                new Fetch("taxon", FetchMode.JOIN)
                });

    }

    /**
     *
     */
    public ImageDaoImpl() {
        super(Image.class);
    }

    /**
     * @param profile
     *            Set the profile name
     * @return an array of related objects to fetch
     */
    @Override
    protected final Fetch[] getProfile(final String profile) {
        return ImageDaoImpl.FETCH_PROFILES.get(profile);
    }
}
