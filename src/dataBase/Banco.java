package dataBase;

import java.text.DateFormatSymbols;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Main;

public class Banco {

	public static String url = "jdbc:sqlite:D:/Projetos/Eclipse/Certificados/db/banco.db";
	private static ObservableList<TabelaCertificados> dados;

	public static Node criarPagina(int ano) {
		return null;
	}

	public static Connection conectar() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);

			System.out.println("Conexão bem sucedida!");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
		return conn;
	}

	public static void inserir(String nome, String string, String empresa, String dataHora) {
		String sql = "INSERT INTO tbCertificados(nome,cpf,empresa,dataHora) VALUES(?,?,?,?)";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, nome);
			pstmt.setString(2, string);
			pstmt.setString(3, empresa);
			pstmt.setString(4, dataHora);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static void buscar() {

		String sql = "SELECT id, nome, cpf, empresa, dataHora FROM tbCertificados WHERE dataHora LIKE ?";
		dados = FXCollections.observableArrayList();

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, String.valueOf(Main.paginacao.getCurrentPageIndex() + 1) + "%");
			ResultSet rs = pstmt.executeQuery();

			InputStream input = Banco.class.getResourceAsStream("Edit.png");
			Image image = new Image(input);

			while (rs.next()) {
				ImageView imEditar = new ImageView(image);

				imEditar.setFitWidth(24);
				imEditar.setFitHeight(24);

				dados.add(new TabelaCertificados(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
						rs.getString("empresa"), rs.getString("dataHora"), new Button("", imEditar),
						new Button("Excluir")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		Main.tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		Main.tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		Main.tcCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
		Main.tcEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
		Main.tcDataHora.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
		Main.tcEditar.setCellValueFactory(new PropertyValueFactory<>("btEditar"));
		Main.tcExcluir.setCellValueFactory(new PropertyValueFactory<>("btExcluir"));

		Main.tvCadastros.setItems(dados);
	}

	@SuppressWarnings("unchecked")
	public static void pesquisar(String pesquisa, int ano) {

		String escolha = Main.cbEscolha.getValue().toString();
		String sql = "";

		if (escolha == "Nome") {

			sql = "SELECT id, nome, cpf, empresa, dataHora FROM tbCertificados WHERE nome LIKE ? AND dataHora LIKE ?";
			dados = FXCollections.observableArrayList();

			try (Connection conn = DriverManager.getConnection(url);
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, pesquisa + "%");
				pstmt.setString(2, String.valueOf(ano + 1) + "%");
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					dados.add(new TabelaCertificados(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
							rs.getString("empresa"), rs.getString("dataHora"), new Button("Editar"),
							new Button("Excluir")));

				}

				Main.tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
				Main.tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
				Main.tcCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
				Main.tcEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
				Main.tcDataHora.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
				Main.tcEditar.setCellValueFactory(new PropertyValueFactory<>("btEditar"));
				Main.tcExcluir.setCellValueFactory(new PropertyValueFactory<>("btExcluir"));

				Main.tvCadastros.setItems(dados);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (escolha == "CPF") {

			sql = "SELECT id, nome, cpf, empresa, dataHora FROM tbCertificados WHERE cpf LIKE ? AND dataHora LIKE ?";
			dados = FXCollections.observableArrayList();

			try (Connection conn = DriverManager.getConnection(url);
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, pesquisa + "%");
				pstmt.setString(2, String.valueOf(ano + 1) + "%");
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					dados.add(new TabelaCertificados(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
							rs.getString("empresa"), rs.getString("dataHora"), new Button("Editar"),
							new Button("Excluir")));
				}

				Main.tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
				Main.tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
				Main.tcCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
				Main.tcEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
				Main.tcDataHora.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
				Main.tcEditar.setCellValueFactory(new PropertyValueFactory<>("btEditar"));
				Main.tcExcluir.setCellValueFactory(new PropertyValueFactory<>("btExcluir"));

				Main.tvCadastros.setItems(dados);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else if (escolha == "Empresa") {

			sql = "SELECT id, nome, cpf, empresa, dataHora FROM tbCertificados WHERE empresa LIKE ? AND dataHora LIKE ?";
			dados = FXCollections.observableArrayList();

			try (Connection conn = DriverManager.getConnection(url);
					PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, pesquisa + "%");
				pstmt.setString(2, String.valueOf(ano + 1) + "%");
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {
					dados.add(new TabelaCertificados(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
							rs.getString("empresa"), rs.getString("dataHora"), new Button("Editar"),
							new Button("Excluir")));
				}

				Main.tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
				Main.tcNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
				Main.tcCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
				Main.tcEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
				Main.tcDataHora.setCellValueFactory(new PropertyValueFactory<>("dataHora"));
				Main.tcEditar.setCellValueFactory(new PropertyValueFactory<>("btEditar"));
				Main.tcExcluir.setCellValueFactory(new PropertyValueFactory<>("btExcluir"));

				Main.tvCadastros.setItems(dados);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void editar(int id) {
		String sql = "SELECT id, nome, cpf, empresa, dataHora FROM tbCertificados WHERE id = ?";
		dados = FXCollections.observableArrayList();

		

				try (Connection conn = DriverManager.getConnection(url);
						PreparedStatement pstmt = conn.prepareStatement(sql)) {
					pstmt.setInt(1, id);
					ResultSet rs = pstmt.executeQuery();

					Main.tabPane.getSelectionModel().select(Main.tbCertificados);

					while (rs.next()) {

						Main.lbCadastroEditado.setText("\n " + rs.getString("id") + " - " + rs.getString("nome") + " - "
								+ rs.getString("cpf") + " - " + rs.getString("empresa") + " \n ");

						Main.tfNome.setText(rs.getString("nome"));
						Main.tfCPF.setPlainText(rs.getString("cpf"));
						Main.tfEmpresa.setText(rs.getString("empresa"));

						trazerData(rs.getString("dataHora"));
						
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		Main.webEngine.reload();
	}
	
	public static void trazerData(String dataHora) {
		
		DateFormatSymbols dateFormat = new DateFormatSymbols();
		
		Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				Main.webEngine.executeScript("document.getElementById('data').innerHTML = '"
						+ dataHora.substring(8, 10) + " de "
						+ dateFormat.getMonths()[Integer.valueOf(dataHora.substring(5, 7)) - 1]
						+ " de " + dataHora.substring(0, 4) + "'");
				Main.webEngine.executeScript("document.getElementById('data2').innerHTML = '"
						+ dataHora.substring(8, 10) + " de "
						+ dateFormat.getMonths()[Integer.valueOf(dataHora.substring(5, 7)) - 1]
						+ " de " + dataHora.substring(0, 4) + "'");
			}
		});
	}

	public static void excluir(int id) {
		String sql = "DELETE FROM tbCertificados WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void atualizar(int id, String nome, String string, String empresa) {
		String sql = "UPDATE tbCertificados SET nome = ? , " + "cpf = ?, " + "empresa = ? " + "WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, nome);
			pstmt.setString(2, string);
			pstmt.setString(3, empresa);
			pstmt.setInt(4, id);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
