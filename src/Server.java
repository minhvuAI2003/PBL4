import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Server {
	private static String jdbcURL = "jdbc:mysql://localhost:3306/pbl4?useSSL=false";
	private static String jdbcUsername = "root";
	private static String jdbcPassword = "root";
	ServerSocket ss;
	Map<String, Socket> socketss = new HashMap<String, Socket>();

	Map<String, Socket> sockets = new HashMap<String, Socket>();
	Vector<client> Clientlist = new Vector<client>();
	String[][] Message = new String[1000][1000];
	String[][] block = new String[1000][1000];
	Map<String, int[]> ID = new HashMap<String, int[]>();
	Vector<client> OnlineClient = new Vector<client>();

	public Server() throws SQLException, IOException {
		ss = new ServerSocket(1134);

		while (true) {
			// int index = 0;

			try {
				Socket sc = ss.accept();
				System.out.println(sc);
				DataInputStream dis = new DataInputStream(sc.getInputStream());
				String receive = dis.readUTF();
				if (receive.equals("block")) {
					new Thread(() -> {
						String ten1 = "";

						try {
							String status = dis.readUTF();
							String ten = dis.readUTF();
							ten1 = dis.readUTF();

							System.out.println(ten + " " + ten1);
							Vector<client> vector1 = fetchalluser(sockets, ID);
							Clientlist.clear();
							socketss.put(ten1, sc);
							Clientlist = vector1;
							int i1 = 0, i2 = 0;
							for (int i = 0; i < Clientlist.size(); i++) {
								if (Clientlist.get(i).gettk().equals(ten))
									i1 = i;
								if (Clientlist.get(i).gettk().equals(ten1))
									i2 = i;
							}
							if (checkblock(Clientlist.get(i2).gettk(), Clientlist.get(i1).gettk())) {
								this.updateblock(ten1, ten, status);
								block[Clientlist.get(i2).getid()][Clientlist.get(i1).getid()] = status;
							} else {
								this.insert_block(ten1, ten);
								this.updateblock(ten1, ten, status);
								block[Clientlist.get(i2).getid()][Clientlist.get(i1).getid()] = status;

							}
							if (sockets.get(Clientlist.get(i1).gettk()) != null)
								if (kiemtra(Clientlist, Clientlist.get(i1).sc, i1)) {

									DataOutputStream dos1 = new DataOutputStream(
											Clientlist.get(i1).sc.getOutputStream());
									dos1.writeUTF(Message[Clientlist.get(i1).getid()][Clientlist.get(i2).getid()]);
									// System.out.println(
									// Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
									dos1.writeUTF(Clientlist.get(i2).gettk());
									if (status.equals("true"))
										dos1.writeUTF("block");
									else
										dos1.writeUTF("unblock");

									// System.out.println(Clientlist.get(b).gettk());

								}
							
						} catch (IOException e) {
							socketss.put(ten1, null);
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}).start();
				}
				if (receive.equals("dndk")) {
					client t = new client(sc);

					t.start();
				}
				if (receive.equals("status")) {
					new Thread(() -> {

						boolean loop = true;
						while (loop) {
							try {
								DataOutputStream dos = new DataOutputStream(sc.getOutputStream());

								String ten;

								ten = dis.readUTF();
								Socket temp_soc = sockets.get(ten);
								if (temp_soc == null) {
									dos.writeUTF("");
								} else {
									dos.writeUTF("hello");
								}

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								loop = false;
							}
						}

					}).start();

				}
			} catch (IOException e) {

				// e.printStackTrace();
			}
		}

	}

	public boolean kiemtra(Vector<client> clientlist2, Socket gettk, int i1) throws IOException {
		if (gettk == null || gettk.getOutputStream() == null)
			return false;
		for (int i = 0; i < clientlist2.size(); i++)
			if (clientlist2.get(i).sc != null)
				if (clientlist2.get(i).sc.equals(gettk) && i != i1) {
					return false;
				}
		return true;
	}

	public void update(String sender, String receiver, String message) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("update message set content=?  where sender=? and receiver=? ");
		preparedStatement.setString(1, message);
		preparedStatement.setString(2, sender);
		preparedStatement.setString(3, receiver);
		preparedStatement.executeUpdate();
	}

	public void updateblock(String sender, String receiver, String message) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("update block set status=?  where sender=? and receiver=? ");
		preparedStatement.setString(1, message);
		preparedStatement.setString(2, sender);
		preparedStatement.setString(3, receiver);
		preparedStatement.executeUpdate();
	}

	protected static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return connection;
	}

	public boolean check(String tk, String mk) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select * from user where taikhoan=? and matkhau=?");
		preparedStatement.setString(1, tk);
		preparedStatement.setString(2, mk);
		ResultSet rs = preparedStatement.executeQuery();
		return rs.next();

	}

	public void insert(String tk, String mk) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into user(taikhoan,matkhau) values(?,?) ");
		preparedStatement.setString(1, tk);
		preparedStatement.setString(2, mk);

		preparedStatement.executeUpdate();

	}

	public boolean checktk1(String tk) throws SQLException {
		Connection connection = getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement("select * from user where taikhoan=?");
		preparedStatement.setString(1, tk);
		ResultSet rs = preparedStatement.executeQuery();
		return rs.next();
	}

	public Vector<client> fetchalluser(Map<String, Socket> Sockets, Map<String, int[]> ID) throws SQLException {
		Connection connection = getConnection();
		Vector<client> t1 = new Vector<client>();
		PreparedStatement preparedStatement = connection.prepareStatement("select * from user");
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			String taikhoan = rs.getString("taikhoan");
			String matkhau = rs.getString("matkhau");
			client t = new client(Sockets.get(taikhoan));
			t.setid(rs.getInt("ID") - 1);
			t.settk(taikhoan);
			System.out.println(taikhoan);
			t.setmk(matkhau);
			t1.add(t);
		}
		return t1;
	}

	private void insert_couple(String gettk, String gettk2) throws SQLException {

		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("insert into message values(?,?,'')");
		preparedStatement.setString(1, gettk);
		preparedStatement.setString(2, gettk2);
		preparedStatement.execute();
	}

	public int layID(String tk) throws SQLException {
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("select ID from user where taikhoan=?");
		preparedStatement.setString(1, tk);
		ResultSet rs = preparedStatement.executeQuery();
		int id = 0;
		while (rs.next())
			id += rs.getInt("ID") - 1;
		return id;
	}

	private boolean checkcouple(String gettk, String gettk2) throws SQLException {

		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select * from message where sender=? and receiver=?");
		preparedStatement.setString(1, gettk);
		preparedStatement.setString(2, gettk2);
		ResultSet rs = preparedStatement.executeQuery();

		return rs.next();
	}

	public boolean checkblock(String gettk, String gettk2) throws SQLException {
		// TODO Auto-generated method stub

		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select * from block where sender=? and receiver=?");
		preparedStatement.setString(1, gettk);
		preparedStatement.setString(2, gettk2);
		ResultSet rs = preparedStatement.executeQuery();

		return rs.next();
	}

	public void insert_block(String gettk, String gettk2) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("insert into block values(?,?,'false')");
		preparedStatement.setString(1, gettk);
		preparedStatement.setString(2, gettk2);
		preparedStatement.execute();
	}

	private String mess(String gettk, String gettk2) throws SQLException {

		String message = "";
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select content from message where sender=? and receiver=?");
		preparedStatement.setString(1, gettk);
		preparedStatement.setString(2, gettk2);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			message += rs.getString("content");
		}
		return message;
	}

	public String getblockstatus(String client, String client2) throws SQLException {
		// TODO Auto-generated method stub
		String message = "";
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("select status from block where sender=? and receiver=?");
		preparedStatement.setString(1, client);
		preparedStatement.setString(2, client2);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			message += rs.getString("status");
		}
		return message;
	}

	public static void main(String[] args) throws IOException, SQLException {
		new Server();
	}

	class client extends Thread {
		int id;
		private String tk;
		private String mk;

		Socket sc;

		Vector<client> onlineClient = new Vector<client>();

		public void setid(int id) {
			this.id = id;
		}

		int getid() {
			return this.id;
		}

		public void settk(String tk) {
			this.tk = tk;
		}

		String gettk() {
			return this.tk;
		}

		public void setmk(String mk) {
			this.mk = mk;
		}

		public client(Socket sc) {

			this.sc = sc;

		}

		public boolean checktk(String tk) {
			for (int i = 0; i < OnlineClient.size(); i++) {
				if (OnlineClient.get(i).gettk().equals(tk)) {
					return false;
				}
			}
			return true;
		}

		public void run() {
			{
				DataOutputStream dos;
				try {
					int ck = 0;
					dos = new DataOutputStream(sc.getOutputStream());
					System.out.println(sc.getOutputStream());
					DataInputStream dis = new DataInputStream(sc.getInputStream());

					String tk = "", mk = "";
					String dnordk = dis.readUTF();
					if (dnordk.equals("dang nhap")) {
						tk = dis.readUTF();
						mk = dis.readUTF();
						boolean check;
						if (!checktk(tk))
							dos.writeUTF("out");
						else {
							try {
								dos.writeUTF("ot");
								check = check(tk, mk);
								if (check == true) {
									ck = 1;
									dos.writeUTF("ok");
									System.out.println("we");
									this.settk(tk);
									this.setmk(mk);
									this.setid(layID(tk));
									sockets.put(tk, sc);
									OnlineClient.add(this);
								} else
									dos.writeUTF("not ok");
							} catch (SQLException e) {

								e.printStackTrace();
							}

						}
					}
					if (dnordk.equals("dang ky")) {
						tk = dis.readUTF();
						mk = dis.readUTF();

						if (checktk1(tk)) {
							dos.writeUTF("not OK");
						} else {
							ck = 1;
							dos.writeUTF("OK");
							this.settk(tk);
							this.setmk(mk);

							insert(tk, mk);
							this.setid(layID(tk));
							sockets.put(tk, sc);

							OnlineClient.add(this);
						}

					}

					try {
						int b = 0;
						Vector<client> vector1 = fetchalluser(sockets, ID);
						Clientlist.clear();
						Clientlist = vector1;
						dos.writeUTF((Clientlist.size() - 1) + "");
						for (int i = 0; i < Clientlist.size(); i++) {
							if (!Clientlist.get(i).gettk().equals(tk)) {

								dos.writeUTF(Clientlist.get(i).gettk());
							} else
								b += i;
						}
						for (int i = 0; i < Clientlist.size(); i++)
							for (int j = 0; j < Clientlist.size(); j++)
								if (i != j) {
									if (checkblock(Clientlist.get(i).gettk(), Clientlist.get(j).gettk()))
										block[Clientlist.get(i).getid()][Clientlist.get(j).getid()] = getblockstatus(
												Clientlist.get(i).gettk(), Clientlist.get(j).gettk());
									else {
										block[Clientlist.get(i).getid()][Clientlist.get(j).getid()] = "false";
										insert_block(Clientlist.get(i).gettk(), Clientlist.get(j).gettk());
									}
								}
						for (int i = 0; i < Clientlist.size(); i++) {
							if (!Clientlist.get(i).gettk().equals(tk)) {
								if (!checkcouple(tk, Clientlist.get(i).gettk())) {
									insert_couple(tk, Clientlist.get(i).gettk());
									Message[Clientlist.get(b).getid()][Clientlist.get(i).getid()] = "";
									dos.writeUTF("");
								} else {
									Message[Clientlist.get(b).getid()][Clientlist.get(i).getid()] = mess(tk,
											Clientlist.get(i).gettk());
									dos.writeUTF(Message[Clientlist.get(b).getid()][Clientlist.get(i).getid()]);
								}
							}
						}
						for (int i = 0; i < Clientlist.size(); i++) {
							if (!Clientlist.get(i).gettk().equals(tk)) {
								dos.writeUTF(block[Clientlist.get(b).getid()][Clientlist.get(i).getid()]);
							}
						}
						for (int i = 0; i < Clientlist.size(); i++) {
							if (!Clientlist.get(i).gettk().equals(tk)) {
								dos.writeUTF(block[Clientlist.get(i).getid()][Clientlist.get(b).getid()]);
							}
						}
						for (int i = 0; i < Clientlist.size(); i++)
							for (int j = 0; j < Clientlist.size(); j++) {
								if (i != b && i != j) {
									if (!checkcouple(Clientlist.get(i).gettk(), Clientlist.get(j).gettk())) {
										insert_couple(Clientlist.get(i).gettk(), Clientlist.get(j).gettk());
										Message[Clientlist.get(i).getid()][Clientlist.get(j).getid()] = "";

									} else {
										Message[Clientlist.get(i).getid()][Clientlist.get(j).getid()] = mess(
												Clientlist.get(i).gettk(), Clientlist.get(j).gettk());
									}
								}
							}
						boolean loop = true;
						int idd = 1;
						if (ck == 1)

							while (loop) {
								Vector<client> vector11 = fetchalluser(sockets, ID);
								Clientlist.clear();
								Clientlist = vector11;
								try {
									DataInputStream dis1 = new DataInputStream(sc.getInputStream());
									System.out.println("Hello  World");
									String st = dis1.readUTF();
									System.out.println(st);

									String tr = dis1.readUTF();
									System.out.println(tr);
									String name = dis1.readUTF();
									System.out.println(name);

									for (int i = 0; i < Clientlist.size(); i++) {
										if (Clientlist.get(i).gettk().equals(name)) {
											idd = i;
											break;
										}
									}

									String message = (String) Message[Clientlist.get(b).getid()][Clientlist.get(idd)
											.getid()];
									if (!message.equals(""))
										message += "\r\n\r\n" + tr;
									else
										message = tr;
									// System.out.println(Message[Clientlist.get(b).getid()][Clientlist.get(idd).getid()]);
									if (block[Clientlist.get(b).getid()][Clientlist.get(idd).getid()].equals("false")
											&& block[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]
													.equals("false")) {
										Message[Clientlist.get(b).getid()][Clientlist.get(idd).getid()] = message;
										update(Clientlist.get(b).gettk(), Clientlist.get(idd).gettk(), message);

										String tk1 = Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()];
										if (tk1.equals("")) {
											tk1 = "";
											Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()] = st;
										} else
											Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()] = tk1
													+ "\r\n\r\n" + st;
										// System.out.println(Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
										// System.out.println(Clientlist.get(idd).sc.getOutputStream());

										update(Clientlist.get(idd).gettk(), Clientlist.get(b).gettk(),
												Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
										// System.out.println("1234");
									}
									if (sockets.get(Clientlist.get(idd).gettk()) != null)
										if (kiemtra(Clientlist, Clientlist.get(idd).sc, idd)) {

											DataOutputStream dos1 = new DataOutputStream(
													Clientlist.get(idd).sc.getOutputStream());
											dos1.writeUTF(
													Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
											// System.out.println(
											// Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
											dos1.writeUTF(Clientlist.get(b).gettk());

											dos1.writeUTF("chua end");
											// System.out.println(Clientlist.get(b).gettk());

										}
									System.out.println(loop);
								} catch (IOException e) {
									Vector<client> vector111 = fetchalluser(sockets, ID);
									Clientlist.clear();
									Clientlist = vector111;
									loop = false;
									OnlineClient.remove(this);
									sockets.put(Clientlist.get(b).gettk(), null);

								}

							}

					} catch (IOException | SQLException e) {

						OnlineClient.remove(this);
					}
				} catch (IOException | SQLException e) {
					OnlineClient.remove(this);
				}

			}
		}

	}

}