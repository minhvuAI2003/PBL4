import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import java.awt.BorderLayout;

public class TCPchat extends JFrame {
	String ten = "";
	String text = "";
	Socket sc;
	Map<String, Socket> sockets = new HashMap<String, Socket>();
	Map<String, String> message = new HashMap<String, String>();
	String tai = "";
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_5;
	private JTabbedPane tabbedPane;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TCPchat frame = new TCPchat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public TCPchat() throws UnknownHostException, IOException {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		CardLayout cl = new CardLayout(0, 0);
		contentPane.setLayout(cl);

		JPanel panel = new JPanel();
		contentPane.add(panel, "name_28176892286400");
		panel.setLayout(null);

		textField = new JTextField();
		textField.setBounds(115, 76, 217, 20);
		panel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Tên đăng nhập:");
		lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		lblNewLabel.setBounds(16, 80, 85, 14);
		panel.add(lblNewLabel);
		JLabel lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setForeground(Color.RED);
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(127, 226, 175, 14);

		JLabel lblMtKhu = new JLabel("Mật khẩu:");
		lblMtKhu.setFont(new Font("Arial", Font.PLAIN, 11));
		lblMtKhu.setBounds(16, 137, 85, 14);
		panel.add(lblMtKhu);

		JLabel lblNewLabel_1 = new JLabel("Đăng nhập");
		lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel_1.setBounds(159, 23, 114, 29);
		panel.add(lblNewLabel_1);

		JButton btnNewButton = new JButton("Gửi");

		btnNewButton.setBounds(115, 183, 89, 23);
		panel.add(btnNewButton);

		panel.add(lblNewLabel_6);

		JButton btnNewButton_2 = new JButton("Đăng ký");

		btnNewButton_2.setBounds(243, 183, 89, 23);
		panel.add(btnNewButton_2);

		passwordField = new JPasswordField();
		passwordField.setBounds(115, 134, 217, 20);
		panel.add(passwordField);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "name_29338460984700");
		panel_1.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("Đăng ký");
		lblNewLabel_2.setFont(new Font("Arial", Font.BOLD, 17));
		lblNewLabel_2.setBounds(171, 31, 103, 30);
		panel_1.add(lblNewLabel_2);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cl.show(contentPane, "name_29338460984700");
			}
		});
		JLabel lblNewLabel_3 = new JLabel("Tài khoản:");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel_3.setBounds(21, 92, 69, 14);
		panel_1.add(lblNewLabel_3);

		textField_2 = new JTextField();
		textField_2.setBounds(135, 89, 205, 20);
		panel_1.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblNewLabel_3_1 = new JLabel("Mật khẩu:");
		lblNewLabel_3_1.setFont(new Font("Arial", Font.PLAIN, 13));
		lblNewLabel_3_1.setBounds(21, 123, 69, 14);
		panel_1.add(lblNewLabel_3_1);

		JLabel lblNewLabel_4 = new JLabel("Nhập lại mật khẩu:");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(22, 153, 103, 14);
		panel_1.add(lblNewLabel_4);
		JLabel lblNewLabel_7 = new JLabel("");
		lblNewLabel_7.setForeground(Color.RED);
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(100, 64, 258, 14);
		panel_1.add(lblNewLabel_7);

		JButton btnNewButton_1 = new JButton("Gửi");

		btnNewButton_1.setBounds(171, 214, 89, 23);
		panel_1.add(btnNewButton_1);

		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(135, 120, 205, 20);
		panel_1.add(passwordField_1);

		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(135, 151, 205, 20);
		panel_1.add(passwordField_2);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, "name_55589903713200");
		panel_2.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 36, 424, 164);
		panel_2.add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		JButton btnNewButton_3 = new JButton("Gửi");

		btnNewButton_3.setBounds(335, 211, 89, 34);
		panel_2.add(btnNewButton_3);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 211, 325, 34);
		panel_2.add(scrollPane_1);

		JTextArea textArea_1 = new JTextArea();
		scrollPane_1.setViewportView(textArea_1);

		JLabel lblNewLabel_8 = new JLabel("");
		lblNewLabel_8.setBounds(10, 11, 202, 14);
		panel_2.add(lblNewLabel_8);

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, "name_36411562135800");
		panel_3.setLayout(null);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(33, 112, 283, 22);
		panel_3.add(comboBox);

		JLabel lblNewLabel_5 = new JLabel("Chọn người để nhắn tin");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(0, 65, 424, 14);
		panel_3.add(lblNewLabel_5);

		JButton btnNewButton_4 = new JButton("Chọn");
		btnNewButton_4.setBounds(325, 112, 89, 23);
		panel_3.add(btnNewButton_4);
		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					sc = new Socket("169.254.165.83", 1134);
					DataOutputStream dos = new DataOutputStream(sc.getOutputStream());
					DataInputStream dis = new DataInputStream(sc.getInputStream());

					String tk = textField_2.getText();
					char[] b = passwordField_1.getPassword();
					char[] b1 = passwordField_2.getPassword();
					String mk = String.valueOf(b);
					String mk_1 = String.valueOf(b1);

					if (tk.equals("")) {
						lblNewLabel_7.setText("Hãy nhập tên đăng nhập!!!");
						textField_2.setText("");
						passwordField_1.setText("");
						passwordField_2.setText("");

					} else if (mk.equals("")) {
						lblNewLabel_7.setText("Hãy nhập mật khẩu!!!");
						textField_2.setText("");
						passwordField_1.setText("");
						passwordField_2.setText("");
					} else if (mk_1.equals("")) {
						lblNewLabel_7.setText("Hãy nhập lại mật khẩu!!!");
						textField_2.setText("");
						passwordField_1.setText("");
						passwordField_2.setText("");
					} else if (!tk.contains("@gmail.com")) {
						textField_2.setText("");
						passwordField_1.setText("");
						passwordField_2.setText("");
						lblNewLabel_7.setText("Khong dung dinh dang gmail!!!");

					} else if (!mk.equals(mk_1)) {
						lblNewLabel_7.setText("Mật khẩu không trùng nhau!!!");
						textField_2.setText("");
						passwordField_1.setText("");
						passwordField_2.setText("");

					} else {
						dos.writeUTF("dang ky");
						dos.writeUTF(tk);
						dos.writeUTF(mk);
						String dangky = dis.readUTF();
						if (dangky.equals("not OK")) {
							JOptionPane.showMessageDialog(contentPane, "Gmail da ton tai!!!");
							Thread.sleep(1000);
							System.exit(ABORT);
						}
						if (dangky.equals("OK")) {
							tai = tk;
							cl.show(contentPane, "name_36411562135800");

							DefaultComboBoxModel<String> name_list = new DefaultComboBoxModel<String>();
							int soluong_1 = Integer.parseInt(dis.readUTF());
							for (int i = 0; i < soluong_1; i++) {
								name_list.addElement(dis.readUTF());
							}
							comboBox.setModel(name_list);
							for (int i = 0; i < soluong_1; i++) {
								message.put(comboBox.getItemAt(i), dis.readUTF());
							}
						}

					}
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tk = textField.getText();
				char[] b = passwordField.getPassword();

				String mk = String.valueOf(b);
				System.out.println(tk + " " + mk);
				try {
					sc = new Socket("169.254.165.83", 1134);
					DataInputStream dis = new DataInputStream(sc.getInputStream());
					DataOutputStream dos = new DataOutputStream(sc.getOutputStream());

					if (tk.equals("")) {
						lblNewLabel_6.setText("Hãy nhập tên tài khoản!!!");
						passwordField.setText("");

					} else if (mk.equals("")) {
						lblNewLabel_6.setText("Hãy nhập mật khẩu!!!");
						textField.setText("");
					} else if (!tk.contains("@gmail.com")) {
						passwordField.setText("");
						textField.setText("");
						lblNewLabel_6.setText("Khong dung dinh dang gmail!!!");

					} else {
						dos.writeUTF("dang nhap");
						dos.writeUTF(tk);

						dos.writeUTF(mk);
						String kq = dis.readUTF();
						System.out.println(kq);
						if (kq.equals("out")) {
							JOptionPane.showMessageDialog(contentPane,
									"Ban da dang nhap roi,khong duoc dang nhap lai!!!");
							Thread.sleep(1000);
							System.exit(ABORT);
						}
						String reply = dis.readUTF();
						System.out.println(reply);
						if (reply.equals("ok")) {
							tai = tk;
							cl.show(contentPane, "name_36411562135800");
							DefaultComboBoxModel<String> name_list = new DefaultComboBoxModel<String>();
							int soluong_1 = Integer.parseInt(dis.readUTF());
							for (int i = 0; i < soluong_1; i++) {
								name_list.addElement(dis.readUTF());
							}
							comboBox.setModel(name_list);
							for (int i = 0; i < soluong_1; i++) {
								message.put(comboBox.getItemAt(i), dis.readUTF());
							}

						} else {
							textField.setText("");
							passwordField.setText("");
							JOptionPane.showMessageDialog(contentPane, "Sai ten gmail hoac mat khau!!!");
							Thread.sleep(1000);
							System.exit(ABORT);

						}
					}
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				DataInputStream dis;
				try {
					dis = new DataInputStream(sc.getInputStream());

					DataOutputStream dos = new DataOutputStream(sc.getOutputStream());
					ten = (String) comboBox.getSelectedItem();
					cl.show(contentPane, "name_55589903713200");

					Vector<String> list = new Vector<String>();

					textArea.setText(message.get(ten));
					text = message.get(ten);
					lblNewLabel_8.setText(ten);
					new Thread(() -> {
						boolean loop = true;
						while (loop)
							try {

								String message1 = dis.readUTF();
								String nm = dis.readUTF();
								String tb = dis.readUTF();
								System.out.println(message1);
								System.out.println(nm);
								System.out.println(tb);
								if (tb.equals("end")) {
									JOptionPane.showMessageDialog(contentPane, nm + " da thoat");
									Thread.sleep(1000);
									System.exit(1);
								}
								if (nm.equals(ten)) {
									textArea.setText(message1);
									text = message1;
								}

								comboBox.setEditable(false);
								if (!nm.equals(ten)) {
									JOptionPane.showMessageDialog(contentPane, "Ban co tin nhan moi tu " + nm);
									if (message.get(nm).length() != 0)
										JOptionPane.showMessageDialog(contentPane, "Noi dung tin nhan:\n"
												+ message1.substring(message.get(nm).length() + 2));
									else
										JOptionPane.showMessageDialog(contentPane,
												"Noi dung tin nhan:\n" + message1.substring(0));
									message.put(nm, message1);
								}

							} catch (IOException | InterruptedException e1) {
								loop = false;
							}

					}).start();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textArea_1.getText();
				;
				textArea_1.setText("");
				String[] mes = message.split("\\n");
				String tr = "Me:";
				String[] mes2 = message.split("\\n");
				String st = "Guest:";
				String bs = "";
				for (int i = 0; i < 12; i++)
					bs += " ";
				for (int i = 0; i < mes2.length; i++) {

					if (i == 0) {
						if (mes2.length != 1)
							mes2[i] += "\r\n";

					} else if (i != mes2.length - 1)
						mes2[i] = bs + mes2[i] + "\r\n";
					else
						mes2[i] = bs + mes2[i];
					st += mes2[i];
				}

				for (int i = 0; i < mes.length; i++) {
					if (i == 0) {
						if (mes.length != 1)
							mes[i] += "\r\n";

					} else if (i != mes.length - 1)
						mes[i] = "      " + mes[i] + "\r\n";
					else
						mes[i] = "      " + mes[i];
					tr += mes[i];
				}

				System.out.println(tr);
				if (text.equals(""))
					text = tr;
				else
					text += "\r\n\r\n" + tr;
				textArea.setText(text);

				{
					try {
						DataOutputStream dos = new DataOutputStream(sc.getOutputStream());
						DataInputStream dis = new DataInputStream(sc.getInputStream());

						dos.writeUTF(st);

						dos.writeUTF(tr);
						dos.writeUTF(ten);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				;

			}
		});

	}
}