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
	Map<String, Socket> sockets = new HashMap<String, Socket>();
	Vector<client> Clientlist = new Vector<client>();
	String[][] Message = new String[1000][1000];
	Map<String, int[]> ID = new HashMap<String, int[]>();
	Vector<client> OnlineClient = new Vector<client>();

	public Server() throws SQLException, IOException {
		ss = new ServerSocket(1134);

		while (true) {
			// int index = 0;

			try {

				Socket sc = ss.accept();
				System.out.println(sc);

				{
					client t = new client(sc);

					t.start();
				}
			} catch (IOException e) {

				// e.printStackTrace();
			}
		}

	}

	public boolean kiemtra(Vector<client> clientlist2, Socket gettk, int i1) {
		if (gettk == null)
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
						while (loop) {

							try {
								DataInputStream dis1 = new DataInputStream(sc.getInputStream());
								System.out.println("Hello  World");
								String st = dis1.readUTF();
								System.out.println(st);
								String tr = dis1.readUTF();
								System.out.println(tr);
								String name = dis1.readUTF();
								System.out.println(name);
								int idd = 0;
								for (int i = 0; i < Clientlist.size(); i++) {
									if (Clientlist.get(i).gettk().equals(name)) {
										idd += i;
										break;
									}
								}
								String message = (String) Message[Clientlist.get(b).getid()][Clientlist.get(idd)
										.getid()];
								if (message != null)
									message += "\r\n\r\n" + tr;
								else
									message = tr;
								Message[Clientlist.get(b).getid()][Clientlist.get(idd).getid()] = message;
								System.out.println(Message[Clientlist.get(b).getid()][Clientlist.get(idd).getid()]);
								update(Clientlist.get(b).gettk(), Clientlist.get(idd).gettk(), message);

								String tk1 = Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()];
								if (tk1 == null) {
									tk1 = "";
									Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()] = st;
								} else
									Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()] = tk1 + "\r\n\r\n"
											+ st;

								//System.out.println(Clientlist.get(idd).sc.getOutputStream());
								update(Clientlist.get(idd).gettk(), Clientlist.get(b).gettk(),
										Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
								if (kiemtra(Clientlist, Clientlist.get(idd).sc, idd)) {
									DataOutputStream dos1 = new DataOutputStream(
											Clientlist.get(idd).sc.getOutputStream());
									dos1.writeUTF(Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
									// System.out.println(
									// Message[Clientlist.get(idd).getid()][Clientlist.get(b).getid()]);
									dos1.writeUTF(Clientlist.get(b).gettk());
									// System.out.println(Clientlist.get(b).gettk());
								}

							} catch (IOException e) {

								loop = false;
								OnlineClient.remove(this);
							}

						}

					} catch (IOException | SQLException e) {
						// TODO Auto-generated catch block

					}
				} catch (IOException | SQLException e) {

				}

			}
		}

	}

}
