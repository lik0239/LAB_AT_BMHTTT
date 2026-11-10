package week_01;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class frm_KetNoiSQL extends javax.swing.JFrame {

    private final JTextField txt_server = new JTextField("127.0.0.1", 20);
    private final JTextField txt_port = new JTextField("14333", 8);
    private final JTextField txt_database = new JTextField("LAB1_MaHoaCoDien", 20);
    private final JTextField txt_user = new JTextField("lab1_user", 20);
    private final JPasswordField txt_password = new JPasswordField(20);
    private final JLabel lbl_status = new JLabel("Chưa kết nối.");

    public frm_KetNoiSQL() {
        setTitle("Kết nối Microsoft SQL Server");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("KẾT NỐI MICROSOFT SQL SERVER");
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(header);
        root.add(Box.createVerticalStrut(10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;
        addRow(form, c, 0, "Server:", txt_server);
        addRow(form, c, 1, "Port:", txt_port);
        addRow(form, c, 2, "Database:", txt_database);
        addRow(form, c, 3, "User:", txt_user);
        addRow(form, c, 4, "Password:", txt_password);
        root.add(form);
        root.add(Box.createVerticalStrut(10));

        JButton btn_test = new JButton("Test Connection");
        JButton btn_history = new JButton("Xem lịch sử");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buttons.add(btn_test);
        buttons.add(btn_history);
        root.add(buttons);
        root.add(Box.createVerticalStrut(8));

        lbl_status.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(lbl_status);

        setContentPane(root);

        btn_test.addActionListener(e -> {
            try {
                int port = Integer.parseInt(txt_port.getText().trim());
                DbConfig.set(txt_server.getText().trim(), port, txt_database.getText().trim(),
                        txt_user.getText().trim(), txt_password.getPassword());
                lbl_status.setText("<html>" + DbConfig.test() + "</html>");
            } catch (Exception ex) {
                DbConfig.clear();
                lbl_status.setText("<html>Kết nối thất bại: " + ex.getMessage() + "</html>");
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Không kết nối được SQL Server", JOptionPane.ERROR_MESSAGE);
            }
        });

        btn_history.addActionListener(e -> new frm_LichSu().setVisible(true));

        pack();
        setMinimumSize(new Dimension(480, getHeight()));
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel form, GridBagConstraints c, int row, String label, Component field) {
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        form.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, c);
        c.fill = GridBagConstraints.NONE;
    }
}
