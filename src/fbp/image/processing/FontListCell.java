package fbp.image.processing;

import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

/**
 * A ListCell implementation that renders fontnames
 * with the font itself.
 */
public class FontListCell extends ListCell<String> {

    private final double defaultFontSize;

    public FontListCell() {
        // The default font size is cached here
        //defaultFontSize = Font.getDefault().getSize();
    	defaultFontSize = 16;
    }

    @Override
    protected void updateItem(String value, boolean empty) {
        super.updateItem(value, empty);
        if(!empty) {
            setText(value);
            // Now set the font to the font corresponding
            // to the value of the combobox item.
            Font font = new Font(value, defaultFontSize);
            setFont(font);
        }
    }
}