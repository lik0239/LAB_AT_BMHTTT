package week_01;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class frm_Menu extends javax.swing.JFrame {

    public frm_Menu() {
        setTitle("LAB 1 - Mã hóa đối xứng cổ điển");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel header = new JLabel("LAB 1 - MÃ HÓA ĐỐI XỨNG CỔ ĐIỂN");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(header);

        JLabel sub = new JLabel("Chọn một bài để mở form tương ứng:");
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(sub);
        root.add(Box.createVerticalStrut(10));

        JPanel grid = new JPanel(new GridLayout(0, 2, 8, 8));
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.add(menuButton("1.1 Caesar", () -> new frm_Caesar().setVisible(true)));
        grid.add(menuButton("1.2 Vigenère", () -> new frm_Vigenere().setVisible(true)));
        grid.add(menuButton("1.3 Rail Fence", () -> new frm_RailFence().setVisible(true)));
        grid.add(menuButton("1.4 Playfair", () -> new frm_Playfair().setVisible(true)));
        grid.add(menuButton("1.5 Transposition", () -> new frm_Transposition().setVisible(true)));
        grid.add(menuButton("1.6 Autokey", () -> new frm_Autokey().setVisible(true)));
        grid.add(menuButton("Nâng cao 01 - Tổng hợp", () -> new frm_TongHop().setVisible(true)));
        grid.add(menuButton("Nâng cao 02 - Tấn công", () -> new frm_TanCong().setVisible(true)));
        grid.add(menuButton("Kết nối SQL Server", () -> new frm_KetNoiSQL().setVisible(true)));
        root.add(grid);
        root.add(Box.createVerticalStrut(10));

        JLabel foot = new JLabel("Mỗi form mở ra là một cửa sổ riêng; đóng form con không làm tắt chương trình.");
        foot.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(foot);

        setContentPane(root);
        pack();
        setMinimumSize(new Dimension(520, getHeight()));
        setLocationRelativeTo(null);
    }

    private JButton menuButton(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(220, 32));
        b.addActionListener(e -> action.run());
        return b;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new frm_Menu().setVisible(true));
    }
}
