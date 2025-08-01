import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class GUI {
    private static JFrame frame;
    private JPanel Jpanel_1;
    private JLabel label_1;
    private JTable tblData;
    private JButton Insert_Button;
    private JTextField textField4; // Corresponds to Song Name
    private JTextField textField5; // Corresponds to Genre
    private JTextField textField6; // Corresponds to Artist
    private JLabel label_sng_name;
    private JLabel label_artst;
    private JLabel label_gen;
    private JButton updateButton;
    private JButton viewButton;
    private JButton deleteButton;
    private JLabel id_text;
    private JTextField textField1; // Corresponds to Song ID
    private JButton clr_button;

    private static final String url = "jdbc:mysql://localhost:3306/dbmsproject";
    private static final String username = "root";
    private static final String password = "******"; // Password redacted in original doc
    private static Connection con = null;

    GUI() {
        frame = new JFrame("GUI_PROJECT");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.add(Jpanel_1, BorderLayout.CENTER);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Action Listener for the Delete Button
        deleteButton.addActionListener(e -> {
            Integer id = Integer.valueOf(textField1.getText());
            String sql = "DELETE FROM music WHERE id = ?";
            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setInt(1, id);
                int affectedRows = prst.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete", "Failure", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException er) {
                er.printStackTrace();
            } finally {
                textField1.setText("");
                textField4.setText("");
                textField6.setText("");
                textField5.setText("");
            }
        });

        // Action Listener for the Insert Button
        Insert_Button.addActionListener(e -> {
            String name = textField4.getText();
            String artist = textField6.getText();
            String genre = textField5.getText();
            String sql = "INSERT INTO music (name, genre, artist) VALUES (?, ?, ?);";
            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setString(1, name);
                prst.setString(2, genre);
                prst.setString(3, artist);
                int affectedRows = prst.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully Inserted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to Insert", "Failure", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException er) {
                er.printStackTrace();
            } finally {
                textField1.setText("");
                textField4.setText("");
                textField6.setText("");
                textField5.setText("");
            }
        });

        // Action Listener for the Update Button
        updateButton.addActionListener(e -> {
            Integer id = Integer.valueOf(textField1.getText());
            String name = textField4.getText();
            String artist = textField6.getText();
            String genre = textField5.getText();
            String sql = "UPDATE music SET name = ?, genre = ?, artist = ? WHERE id = ?";
            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setString(1, name);
                prst.setString(2, genre);
                prst.setString(3, artist);
                prst.setInt(4, id);
                int affectedRows = prst.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to Update", "Failure", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException er) {
                er.printStackTrace();
            } finally {
                textField1.setText("");
                textField4.setText("");
                textField6.setText("");
                textField5.setText("");
            }
        });

        // Action Listener for the View Button
        viewButton.addActionListener(e -> {
            try {
                Statement st = con.createStatement();
                String query = "SELECT * FROM music;";
                ResultSet rs = st.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                DefaultTableModel model = (DefaultTableModel) tblData.getModel();
                
                int columnCount = rsmd.getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnNames[i] = rsmd.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(columnNames);

                model.setRowCount(0); // Clear existing data

                while (rs.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = rs.getObject(i + 1);
                    }
                    model.addRow(rowData);
                }
                st.close();
                rs.close();
            } catch (SQLException er) {
                er.printStackTrace();
            }
        });

        // Action Listener for the Clear Button
        clr_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField1.setText("");
                textField4.setText("");
                textField6.setText("");
                textField5.setText("");
                tblData.setModel(new DefaultTableModel());
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}
