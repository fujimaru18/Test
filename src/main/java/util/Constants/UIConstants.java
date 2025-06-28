package util.Constants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.EmptyBorder;

public class UIConstants {

    public static final String APP_TITLE = "Quản lý sản phẩm";

    // Font
    public static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);

    // Màu sắc
    public static final Color TABLE_HEADER_BG = new Color(230, 240, 255);
    public static final Color TITLE_COLOR_BG = Color.CYAN;
    public static final Color BUTTON_COLOR_BG = Color.CYAN;
    public static final Color TEXT_COLOR = Color.BLACK;

    // Kích thước
    public static final Dimension BUTTON_SIZE = new Dimension(100, 35);
    public static final Dimension TEXTFIELD_SIZE = new Dimension(430, 30);
    public static final int TABLE_ROW_HEIGHT = 24;

    //padding cho các button
    public static final int DEFAULT_PADDING = 10;

    //padding cho nội dung
    public static final EmptyBorder PANEL_PADDING = new EmptyBorder(10, 10, 10, 10);
}
