package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import controle.Controle;
import controle.MaskedTextField;
import dataBase.Banco;
import dataBase.TabelaCertificados;
import javafx.application.Application;
import javafx.concurrent.Worker.State;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	public static Stage janela = new Stage();
	public static TabPane tabPane = new TabPane();
	public static Tab tbCertificados = new Tab("Certificados");
	public static SplitPane spCertificados = new SplitPane();
	public static VBox vbCadastro = new VBox();
	public static Label lbEditando = new Label(" Editando:");
	public static Button btNovo = new Button("+");
	public static Label lbCadastroEditado = new Label();
	public static HBox hbEditando = new HBox();
	public static Label lbNome = new Label(" Nome:");
	public static TextField tfNome = new TextField();
	public static Label lbCPF = new Label(" CPF:");
	public static MaskedTextField tfCPF = new MaskedTextField("###.###.###-##");
	public static Label lbEmpresa = new Label(" Empresa:");
	public static TextField tfEmpresa = new TextField();
	public static Button btImprimir = new Button("Imprimir");
	public static Button btSalvar = new Button("Salvar");
	public static WebView wvCertificado = new WebView();
	public static WebEngine webEngine = new WebEngine();
	public static ScrollPane scrollPane = new ScrollPane();
	public static VBox vbVisualizacao = new VBox();
	public static Label lbAutor = new Label("	© RProgramações");
	public static Region regiaoL = new Region();
	public static Label lbEmail = new Label("rprogramacoes@gmail.com");
	public static Region regiaoR = new Region();
	public static Label lbPhone = new Label("(19) 99830-5732	");
	public static HBox hbRodape = new HBox();
	public static Tab tbCadastros = new Tab("Cadastros");
	public static VBox vbCadastros = new VBox();
	public static TextField tfPesquisa = new TextField();
	@SuppressWarnings("rawtypes")
	public static ChoiceBox cbEscolha = new ChoiceBox();
	public static VBox vbTabela = new VBox();
	@SuppressWarnings("rawtypes")
	public static TableView tvCadastros = new TableView();
	public static TableColumn<Integer, TabelaCertificados> tcId = new TableColumn<Integer, TabelaCertificados>("ID");
	public static TableColumn<String, TabelaCertificados> tcNome = new TableColumn<String, TabelaCertificados>("Nome");
	public static TableColumn<Double, TabelaCertificados> tcCpf = new TableColumn<Double, TabelaCertificados>("CPF");
	public static TableColumn<String, TabelaCertificados> tcEmpresa = new TableColumn<String, TabelaCertificados>(
			"Empresa");
	public static TableColumn<String, TabelaCertificados> tcDataHora = new TableColumn<String, TabelaCertificados>(
			"Data/Hora \n(aaaa/MM/dd HH:mm)");
	public static TableColumn<String, TabelaCertificados> tcEditar = new TableColumn<String, TabelaCertificados>(
			"Editar/Imprimir");
	public static TableColumn<String, TabelaCertificados> tcExcluir = new TableColumn<String, TabelaCertificados>(
			"Excluir");
	public static Pagination paginacao = new Pagination(Integer.MAX_VALUE, LocalDateTime.now().getYear() - 1);
	public static VBox vBox = new VBox();
	public static Scene scene = new Scene(vBox);
	public static FileInputStream input;
	public static Image imagem;
	public static TextInputDialog dialogo = new TextInputDialog();

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage janela) throws Exception {

		Main.janela = janela;

		new Controle();

		janela.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
		janela.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
		janela.setTitle("Cadastro de Certificados");
		janela.setMaximized(true);
		
		InputStream input = Main.class.getResourceAsStream("Icon.png");
		Image image = new Image(input);
		
		janela.getIcons().add(image);

		tbCertificados.setClosable(false);

		// ---

		vbCadastro.minWidthProperty().bind(janela.widthProperty().divide(4));
		vbCadastro.maxWidthProperty().bind(janela.widthProperty().divide(4));
		vbCadastro.maxHeightProperty().bind(tabPane.heightProperty());

		lbEditando.translateYProperty().bind(tabPane.heightProperty().divide(400));

		btNovo.translateXProperty().bind(vbCadastro.maxWidthProperty().subtract(35));
		btNovo.translateYProperty().bind(lbEditando.translateYProperty().subtract(20));

		lbCadastroEditado.setText("\n [novo] \n ");
		hbEditando.getChildren().add(lbCadastroEditado);

		hbEditando.translateYProperty().bind(lbEditando.translateYProperty().subtract(10));
		String cssLayout = "-fx-border-insets: 5;\n" + "-fx-border-width: 3;\n" + "-fx-border-style: dashed;\n";
		hbEditando.setStyle(cssLayout);
		hbEditando.setAlignment(Pos.CENTER);

		lbNome.translateYProperty().bind(hbEditando.translateYProperty().add(20));

		tfNome.translateYProperty().bind(lbNome.translateYProperty().add(10));
		tfNome.setTranslateX(10);
		tfNome.maxWidthProperty().bind(vbCadastro.widthProperty().subtract(20));
		tfNome.setFocusTraversable(false);

		lbCPF.translateYProperty().bind(tfNome.translateYProperty().add(20));

		tfCPF.translateYProperty().bind(lbCPF.translateYProperty().add(10));
		tfCPF.setTranslateX(10);
		tfCPF.maxWidthProperty().bind(vbCadastro.widthProperty().subtract(20));
		tfCPF.setFocusTraversable(false);

		lbEmpresa.translateYProperty().bind(tfCPF.translateYProperty().add(20));

		tfEmpresa.translateYProperty().bind(lbEmpresa.translateYProperty().add(10));
		tfEmpresa.setTranslateX(10);
		tfEmpresa.maxWidthProperty().bind(vbCadastro.widthProperty().subtract(20));
		tfEmpresa.setFocusTraversable(false);

		btImprimir.translateYProperty().bind(tfEmpresa.translateYProperty().add(40));
		btImprimir.maxWidthProperty().bind(vbCadastro.maxWidthProperty());

		btSalvar.translateYProperty().bind(btImprimir.translateYProperty().add(20));
		btSalvar.translateXProperty()
				.bind(vbCadastro.widthProperty().divide(2).subtract(btSalvar.widthProperty().divide(2)));

		vbCadastro.getChildren().addAll(lbEditando, btNovo, hbEditando, lbNome, tfNome, lbCPF, tfCPF, lbEmpresa,
				tfEmpresa, btImprimir, btSalvar);

		// ---

		vbVisualizacao.setMinWidth(0);
		vbVisualizacao.minHeightProperty().bind(janela.heightProperty());

		webEngine = wvCertificado.getEngine();

		URL url = this.getClass().getResource("/main/Certificado.html");

		webEngine.load(url.toString());

		webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue == State.SUCCEEDED) {
				
				Main.webEngine.executeScript(
						"document.getElementById('cert').style.width = '" + 100 + "%'");
				Main.webEngine.executeScript(
						"document.getElementById('cert').style.height = '" + 92 + "%'");
				
				Main.webEngine
						.executeScript("document.getElementById('nome').innerHTML = '" + "____________________" + "'");
				Main.webEngine.executeScript("document.getElementById('cpf').innerHTML = '" + tfCPF.getText() + "'");
				Main.webEngine
						.executeScript("document.getElementById('data').innerHTML = '"
								+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
										.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
								+ " de " + LocalDateTime.now().getYear() + "'");
				Main.webEngine
						.executeScript("document.getElementById('data2').innerHTML = '"
								+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
										.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
								+ " de " + LocalDateTime.now().getYear() + "'");
			}
		});

		wvCertificado.setTranslateX(10);
		wvCertificado.setMinWidth(Controle.defaultLayout.getPrintableWidth() - Controle.defaultLayout.getPrintableWidth() * 100 / 2800);
		wvCertificado.setTranslateY(10);
		wvCertificado.setMinHeight(Controle.defaultLayout.getPrintableHeight() - Controle.defaultLayout.getPrintableWidth() * 100 / 2800);
		
		scrollPane.maxWidthProperty().bind(vbVisualizacao.widthProperty());
		scrollPane.maxHeightProperty().bind(vbVisualizacao.heightProperty().divide(1.2));
		scrollPane.setContent(wvCertificado);

		vbVisualizacao.maxHeightProperty().bind(tabPane.heightProperty());
		vbVisualizacao.getChildren().addAll(scrollPane);

		// ---

		spCertificados.getItems().addAll(vbCadastro, vbVisualizacao);

		tbCertificados.setContent(spCertificados);

		tbCadastros.setClosable(false);

		// ---

		tfPesquisa.setTranslateX(10);
		tfPesquisa.translateYProperty().bind(vBox.heightProperty().divide(50));
		tfPesquisa.maxWidthProperty().bind(vBox.widthProperty().subtract(100));

		cbEscolha.getItems().add("Nome");
		cbEscolha.getItems().add("CPF");
		cbEscolha.getItems().add("Empresa");
		cbEscolha.getSelectionModel().selectFirst();
		cbEscolha.translateXProperty().bind(tfPesquisa.maxWidthProperty().add(15));
		cbEscolha.setTranslateY(-11);

		tcEditar.setStyle("-fx-alignment: CENTER;");
		tcExcluir.setStyle("-fx-alignment: CENTER;");

		tvCadastros.minHeightProperty().bind(vBox.heightProperty().divide(1.5));
		tvCadastros.maxHeightProperty().bind(vBox.heightProperty().divide(1.5));
		tvCadastros.getColumns().addAll(tcId, tcNome, tcCpf, tcEmpresa, tcDataHora, tcEditar, tcExcluir);

		vbTabela.getChildren().add(tvCadastros);

		paginacao.translateYProperty().bind(tfPesquisa.translateYProperty().subtract(15));
		paginacao.getStylesheets().add(getClass().getResource("paginacao.css").toExternalForm());

		vbCadastros.getChildren().addAll(tfPesquisa, cbEscolha, tvCadastros, paginacao);

		// ---

		tbCadastros.setContent(vbCadastros);

		tabPane.getTabs().addAll(tbCertificados, tbCadastros);

		lbAutor.setFont(new Font(16));
		lbEmail.setFont(new Font(16));
		lbPhone.setFont(new Font(16));

		hbRodape.setStyle("-fx-border-insets: 0;\r\n" + "    -fx-border-width: 3px;\r\n"
				+ "    -fx-border-color: darkgray black  black darkgray;");
		hbRodape.setPrefHeight(25);
		HBox.setHgrow(regiaoL, Priority.ALWAYS);
		HBox.setHgrow(regiaoR, Priority.ALWAYS);

		hbRodape.getChildren().addAll(lbAutor, regiaoL, lbEmail, regiaoR, lbPhone);

		vBox.getChildren().addAll(tabPane, hbRodape);

		dialogo.setTitle("Local do Banco de Dados");
		dialogo.setHeaderText("Onde deseja armazenar o banco de dados?");
		dialogo.setContentText("Especifique o local:");

		janela.setScene(scene);

		File file = new File("Config.cfg");

		if (file.createNewFile() || file.length() == 0) {
			Optional<String> result = dialogo.showAndWait();
			System.out.println("Ok");
			try {
				if (result.isPresent() && !result.get().isEmpty()) {
					
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					writer.write(result.get());

					writer.close();
					
					try (Scanner scanner = new Scanner(file)) {
				        Banco.url = "jdbc:sqlite:" + scanner.next() + "\\banco.db";
				        scanner.close();
				        
				        Banco.conectar();
						TabelaCertificados.criarTabela();
						Banco.buscar();
				        
				        janela.show();
				    } catch (FileNotFoundException e) {
				        e.printStackTrace();
				    }
				} else {
					System.exit(0);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
			try (Scanner scanner = new Scanner(file)) {
		        Banco.url = "jdbc:sqlite:" + scanner.next() + "\\banco.db";
		        scanner.close();
		        
		        Banco.conectar();
				TabelaCertificados.criarTabela();
				Banco.buscar();
		        
		        janela.show();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
