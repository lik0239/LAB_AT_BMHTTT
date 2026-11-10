package week_01;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Lớp nền dùng chung cho 6 form thuật toán cơ bản (frm_Caesar, frm_Vigenere, ...).
 * Trong tài liệu các form được dựng bằng NetBeans GUI Builder; ở project tham chiếu
 * chạy được này, giao diện được viết tay nhưng GIỮ NGUYÊN tên biến và hành vi nút.
 */
public abstract class AbstractCipherFrame extends javax.swing.JFrame {

    protected final JTextArea txt_plaintext = new JTextArea(3, 50);
    protected final JTextArea txt_ciphertext = new JTextArea(3, 50);
    protected final JTextField txt_key = new JTextField(12);
    protected final JLabel jLabel4 = new JLabel();     // nhãn gợi ý cạnh ô Key
    protected final JLabel lbl_status = new JLabel("Sẵn sàng.");
    protected final JButton btn_encrypt = new JButton("Encrypt");
    protected final JButton btn_decrypt = new JButton("Decrypt");
    protected final JButton btn_open = new JButton("Open Ciphertext File");
    protected final JButton btn_save = new JButton("Save Ciphertext File");

    protected AbstractCipherFrame(String title, String headerText, String hintText, String defaultKey) {
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel(headerText);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(header);
        root.add(Box.createVerticalStrut(10));

        root.add(leftLabel("Plaintext:"));
        root.add(wrapArea(txt_plaintext));
        root.add(Box.createVerticalStrut(8));

        JPanel keyRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        keyRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        keyRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        keyRow.add(new JLabel("Key:"));
        txt_key.setText(defaultKey);
        txt_key.setPreferredSize(new Dimension(150, 26));
        keyRow.add(txt_key);
        jLabel4.setText(hintText);
        keyRow.add(jLabel4);
        root.add(keyRow);
        root.add(Box.createVerticalStrut(4));

        addExtras(root);   // Playfair chèn thêm ô Key Matrix ở đây

        root.add(leftLabel("Ciphertext:"));
        root.add(wrapArea(txt_ciphertext));
        root.add(Box.createVerticalStrut(8));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        buttons.add(btn_open);
        buttons.add(btn_save);
        root.add(buttons);
        root.add(Box.createVerticalStrut(8));

        lbl_status.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(lbl_status);

        setContentPane(root);

        btn_encrypt.addActionListener(e -> onEncrypt());
        btn_decrypt.addActionListener(e -> onDecrypt());
        btn_open.addActionListener(e -> {
            String path = FileTextIO.openText(this, txt_ciphertext);
            if (path != null) {
                lbl_status.setText("Đã mở tệp: " + path);
            }
        });
        btn_save.addActionListener(e -> {
            String path = FileTextIO.saveText(this, txt_ciphertext, saveFileName());
            if (path != null) {
                lbl_status.setText("Đã lưu tệp: " + path);
            }
        });

        pack();
        setMinimumSize(new Dimension(640, getHeight()));
        setLocationRelativeTo(null);
    }

    private JLabel leftLabel(String text) {
        JLabel l = new JLabel(text);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JScrollPane wrapArea(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JScrollPane sp = new JScrollPane(area);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(600, 70));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        return sp;
    }

    /** Form con chèn thêm điều khiển (mặc định không có). */
    protected void addExtras(JPanel root) {
    }

    protected abstract void onEncrypt();

    protected abstract void onDecrypt();

    protected abstract String saveFileName();
}
