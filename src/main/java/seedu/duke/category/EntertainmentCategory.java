package seedu.duke.category;

/**
 * Represents the ENTERTAINMENT expense category.
 *
 * <p>Assigned sort order {@code 3}, meaning entertainment expenses appear
 * third when the expense list is sorted by category.</p>
 */
public class EntertainmentCategory extends Category {

    /**
     * {@inheritDoc}
     *
     * @return {@code "ENTERTAINMENT"}
     */
    @Override public String getName() {
        return "ENTERTAINMENT";
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code 3}
     */
    @Override public int getSortOrder() {
        return 3;
    }
}
