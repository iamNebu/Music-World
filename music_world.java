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
        frame = new JFrame("GUI_PROJECT"); [cite: 291]
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500); [cite: 294]
        frame.setLayout(new BorderLayout()); [cite: 295]
        frame.setVisible(true);
        frame.add(Jpanel_1, BorderLayout.CENTER); [cite: 297]

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); [cite: 299]
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, username, password); [cite: 303]
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Action Listener for the Delete Button
        deleteButton.addActionListener(e -> { [cite: 306]
            Integer id = Integer.valueOf(textField1.getText()); [cite: 307]
            String sql = "DELETE FROM music WHERE id = ?";
            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setInt(1, id); [cite: 310]
                int affectedRows = prst.executeUpdate(); [cite: 311]
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully deleted", "Success", JOptionPane.INFORMATION_MESSAGE); [cite: 313]
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete", "Failure", JOptionPane.INFORMATION_MESSAGE); [cite: 315]
                }
            } catch (SQLException er) {
                er.printStackTrace();
            } finally {
                textField1.setText(""); [cite: 319]
                textField4.setText(""); [cite: 320]
                textField6.setText(""); [cite: 321]
                textField5.setText(""); [cite: 322]
            }
        });

        // Action Listener for the Insert Button
        Insert_Button.addActionListener(e -> { [cite: 323]
            String name = textField4.getText(); [cite: 324]
            String artist = textField6.getText(); [cite: 325]
            String genre = textField5.getText(); [cite: 326]
            String sql = "INSERT INTO music (name, genre, artist) VALUES (?, ?, ?);"; [cite: 327]
            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setString(1, name); [cite: 329]
                prst.setString(2, genre); [cite: 330]
                prst.setString(3, artist); [cite: 331]
                int affectedRows = prst.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully Inserted", "Success", JOptionPane.INFORMATION_MESSAGE); [cite: 334]
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to Insert", "Failure", JOptionPane.INFORMATION_MESSAGE); [cite: 336]
                }
            } catch (SQLException er) {
                er.printStackTrace();
            } finally {
                textField1.setText(""); [cite: 340]
                textField4.setText(""); [cite: 342]
                textField6.setText(""); [cite: 344]
                textField5.setText(""); [cite: 345]
            }
        });

        // Action Listener for the Update Button
        updateButton.addActionListener(e -> { [cite: 346]
            Integer id = Integer.valueOf(textField1.getText()); [cite: 347]
            String name = textField4.getText(); [cite: 348]
            String artist = textField6.getText(); [cite: 349]
            String genre = textField5.getText(); [cite: 350]
            String sql = "UPDATE music SET name = ?, genre = ?, artist = ? WHERE id = ?";
            try (PreparedStatement prst = con.prepareStatement(sql)) {
                prst.setString(1, name); [cite: 353]
                prst.setString(2, genre); [cite: 354]
                prst.setString(3, artist); [cite: 355]
                prst.setInt(4, id); [cite: 356]
                int affectedRows = prst.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(null, "Successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE); [cite: 359]
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to Update", "Failure", JOptionPane.INFORMATION_MESSAGE); [cite: 360]
                }
            } catch (SQLException er) {
                er.printStackTrace();
            } finally {
                textField1.setText(""); [cite: 364]
                textField4.setText(""); [cite: 365]
                textField6.setText(""); [cite: 366]
                textField5.setText(""); [cite: 368]
            }
        });

        // Action Listener for the View Button
        viewButton.addActionListener(e -> { [cite: 369]
            try {
                Statement st = con.createStatement(); [cite: 370]
                String query = "SELECT * FROM music;"; [cite: 371]
                ResultSet rs = st.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                DefaultTableModel model = (DefaultTableModel) tblData.getModel();
                
                int columnCount = rsmd.getColumnCount(); [cite: 372]
                String[] columnNames = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    columnNames[i] = rsmd.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(columnNames);

                model.setRowCount(0); // Clear existing data

                while (rs.next()) { [cite: 376]
                    Object[] rowData = new Object[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        rowData[i] = rs.getObject(i + 1); [cite: 379]
                    }
                    model.addRow(rowData); [cite: 380]
                }
                st.close();
                rs.close();
            } catch (SQLException er) {
                er.printStackTrace();
            }
        });

        // Action Listener for the Clear Button
        clr_button.addActionListener(new ActionListener() { [cite: 386]
            public void actionPerformed(ActionEvent e) {
                textField1.setText(""); [cite: 388]
                textField4.setText(""); [cite: 389]
                textField6.setText(""); [cite: 391]
                textField5.setText(""); [cite: 392]
                tblData.setModel(new DefaultTableModel()); [cite: 393]
            }
        });
    }

    public static void main(String[] args) { [cite: 394]
        SwingUtilities.invokeLater(new Runnable() { [cite: 395]
            public void run() {
                new GUI(); [cite: 396]
            }
        });
    }
}