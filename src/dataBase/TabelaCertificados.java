package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class TabelaCertificados {

	int id;
	String nome;
	String cpf;
	String empresa;
	String dataHora;
	Button btEditar;
	Button btExcluir;

	public static void criarTabela() {

		String sql = "CREATE TABLE IF NOT EXISTS tbCertificados (\n" + "	id integer PRIMARY KEY,\n"
				+ "	nome varchar(50) NOT NULL,\n" + "	cpf varchar(14) NOT NULL,\n"
				+ " empresa varchar(20) NOT NULL,\n" + " dataHora datetime NOT NULL\n" + ");";

		try (Connection conn = DriverManager.getConnection(Banco.url); Statement stmt = conn.createStatement()) {
			System.out.println(Banco.url);
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TabelaCertificados(int id, String nome, String cpf, String empresa, String dataHora, Button btEditar,
			Button btExcluir) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.empresa = empresa;
		this.dataHora = dataHora;
		this.btEditar = btEditar;
		this.btExcluir = btExcluir;

		btEditar.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				Banco.editar(id);
			}
		});

		btExcluir.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				Banco.excluir(id);
				Banco.buscar();
			}
		});
	}

	public TabelaCertificados() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

	public Button getBtEditar() {
		return btEditar;
	}

	public void setBtEditar(Button btEditar) {
		this.btEditar = btEditar;
	}

	public Button getBtExcluir() {
		return btExcluir;
	}

	public void setBtExcluir(Button btExcluir) {
		this.btExcluir = btExcluir;
	}
}
