package fbp.image.processing;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * A ComboBox Cell factory that produces special font-rendering cells
 */
public class ComboBoxCellFactory implements Callback<ListView<String>,ListCell<String>> {


    /**
     * This method returns a cell for some rendering operation.
     * Note that there is no one-to-one mapping for items in the combobox.
     * The cells you return here are recycled for rendering purposes
     *
     * @param stringListView the listview for which to return a cell
     * @return a rendered for a cell
     */
    @Override
    public ListCell<String> call(ListView<String> stringListView) {
        return new FontListCell();
    }
}
