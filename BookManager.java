import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class BookManager extends JFrame {
    private JPanel historyPanel;
    private Map<String, String> wishListStorage = new HashMap<>();
    private Map<String, String> buttonStorage = new HashMap<>();

    public BookManager() {
        setTitle("Books");
        setSize(1360, 720);
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        buildInterface();
    }

    private void buildInterface() {
        createTitlePanel();
        createHistoryPanel();
        createToolbar();
    }

    private void createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBounds(500, 40, 380, 100);
        titlePanel.setBackground(Color.black);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel titleLabel = new JLabel("Book Manager");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 45));
        titleLabel.setForeground(Color.white);
        titlePanel.add(titleLabel);
        add(titlePanel);
    }

    private void createHistoryPanel() {
        historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setBounds(10, 160, 250, 500);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(scrollPane);
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0, 0, 1360, 40);
        toolBar.setBackground(Color.black);
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(toolBar);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.black);
        toolBar.add(menuBar);

        // Menu section
        JMenu menu = new JMenu("Menu");
        menu.setForeground(Color.black);
        menu.setBackground(Color.white);
        menu.setOpaque(true);
        menu.setFont(new Font("Arial", Font.BOLD, 20));

        JMenuItem background = new JMenuItem("Change Background");
        background.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color",
                    getContentPane().getBackground());
            if (newColor != null)
                getContentPane().setBackground(newColor);
        });
        menu.add(background);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        menu.add(exit);

        JMenuItem wishlist = new JMenuItem("Wish List");
        wishlist.addActionListener(e -> createWishlist());
        menu.add(wishlist);

        menuBar.add(menu);

        // New Entry menu
        JMenu newMenu = new JMenu("+ New");
        newMenu.setForeground(Color.black);
        newMenu.setBackground(Color.YELLOW);
        newMenu.setOpaque(true);
        newMenu.setFont(new Font("Arial", Font.BOLD, 20));
        menuBar.add(newMenu);

        JMenuItem newDay = new JMenuItem("New Day");
        newDay.addActionListener(e -> createNewDayEntry());
        newMenu.add(newDay);
    }

    private void createWishlist() {
        if (JOptionPane.showConfirmDialog(this, "Do you want to create a wish list?") == JOptionPane.YES_OPTION) {
            String wishListName = JOptionPane.showInputDialog(this, "Enter Wish List Name:");
            if (wishListName != null && !wishListName.trim().isEmpty()) {
                JButton wishlistButton = new JButton("Wish List: " + wishListName);
                wishlistButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                wishlistButton.setBackground(Color.WHITE);
                wishlistButton.setForeground(Color.BLACK);
                wishlistButton.setFont(new Font("Arial", Font.PLAIN, 14));
                wishlistButton.setToolTipText("Wish List: " + wishListName);

                wishlistButton.addActionListener(
                        e -> editContent(wishListStorage, wishListName, "Edit Wishlist: " + wishListName));
                historyPanel.add(Box.createVerticalStrut(5));
                historyPanel.add(wishlistButton);
                historyPanel.revalidate();
                historyPanel.repaint();

                JOptionPane.showMessageDialog(this, "Wish List '" + wishListName + "' created successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Wish List creation cancelled.");
            }
        }
    }

    private void createNewDayEntry() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = today.format(dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = LocalTime.now().format(timeFormatter);

        String timestamp = "Read on: " + formattedDate + " at " + formattedTime;

        JButton dayButton = new JButton(timestamp);
        dayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayButton.setBackground(Color.WHITE);
        dayButton.setForeground(Color.BLACK);
        dayButton.setFont(new Font("Arial", Font.PLAIN, 14));
        dayButton.setToolTipText("Created at " + timestamp);

        // Attach editing listener directly
        dayButton.addActionListener(e -> editContent(buttonStorage, timestamp, "Edit Entry: " + timestamp));

        historyPanel.add(Box.createVerticalStrut(5));
        historyPanel.add(dayButton);
        historyPanel.revalidate();
        historyPanel.repaint();

        JOptionPane.showMessageDialog(this, "New Day Created!\nDate: " + formattedDate + "\nTime: " + formattedTime);
    }

    private void editContent(Map<String, String> storage, String key, String title) {
        String content = storage.getOrDefault(key, "");

        JTextArea textArea = new JTextArea(content, 20, 30);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, title, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String updatedContent = textArea.getText().trim();
            if (!updatedContent.isEmpty()) {
                storage.put(key, updatedContent);
                JOptionPane.showMessageDialog(this, "Saved!");
            } else {
                JOptionPane.showMessageDialog(this, "Content is empty.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookManager app = new BookManager();
            app.setVisible(true);
        });
    }
}
