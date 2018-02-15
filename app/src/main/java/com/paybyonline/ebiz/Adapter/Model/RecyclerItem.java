package com.paybyonline.ebiz.Adapter.Model;

import java.io.Serializable;
import java.util.List;

/**
 * @author anish
 *         Common interface for both type of items which are using in current project
 */
public interface RecyclerItem extends Serializable {
    List<Integer> getGenreIds();
}
