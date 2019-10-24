package controle;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import dataBase.Banco;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.Printer.MarginType;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import main.Main;

public class Controle {

	public static Printer defaultPrinter = Printer.getDefaultPrinter();
	public static PageLayout defaultLayout = defaultPrinter.createPageLayout(defaultPrinter.getPrinterAttributes().getDefaultPaper(),
			PageOrientation.LANDSCAPE, 1,1,1,1);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Controle() {

		Main.dialogo.getEditor().setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				DirectoryChooser dChooser = new DirectoryChooser();
				File file = dChooser.showDialog(Main.dialogo.getOwner());
				Main.dialogo.getEditor().setText(file.toString());
			}

		});

		Main.tfNome.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.ENTER) {
					if (Main.tfNome.getText().isEmpty() || Main.tfCPF.getPlainText().isEmpty()
							|| Main.tfEmpresa.getText().isEmpty()) {
						Main.tfCPF.requestFocus();
					} else {
						Main.btImprimir.fire();
					}
				}
			}
		});

		Main.tfCPF.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.ENTER) {
					if (Main.tfNome.getText().isEmpty() || Main.tfCPF.getPlainText().isEmpty()
							|| Main.tfEmpresa.getText().isEmpty()) {
						Main.tfEmpresa.requestFocus();
					} else {
						Main.btImprimir.fire();
					}
				}
			}
		});

		Main.tfEmpresa.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.ENTER) {
					if (Main.tfNome.getText().isEmpty() || Main.tfCPF.getPlainText().isEmpty()
							|| Main.tfEmpresa.getText().isEmpty()) {
						Main.tfNome.requestFocus();
					} else {
						Main.btImprimir.fire();
					}
				}
			}
		});

		Main.btImprimir.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {
				PrinterJob job = PrinterJob.createPrinterJob(defaultPrinter);
				job.getJobSettings().setPageLayout(defaultLayout);
				
				job.showPrintDialog(Main.janela);
				
				job.showPageSetupDialog(Main.janela);

				PageLayout novoLayout = job.getPrinter().createPageLayout(job.getJobSettings().getPageLayout().getPaper(),
						job.getJobSettings().getPageLayout().getPageOrientation(),
						1,1,1,1);
				
				job.getJobSettings().setPageLayout(novoLayout);
				
				System.out.println(defaultLayout.getPrintableWidth() + " - " + defaultLayout.getPrintableHeight());
				System.out.println(novoLayout.getPrintableWidth() + " - " + novoLayout.getPrintableHeight());
				
				Main.wvCertificado.setMinWidth(novoLayout.getPrintableWidth() - novoLayout.getPrintableWidth() * 100 / 2800);
				Main.wvCertificado.setMinHeight(novoLayout.getPrintableHeight() - novoLayout.getPrintableWidth() * 100 / 2800);
				Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
					if (newValue == State.SUCCEEDED) {

						Main.webEngine.executeScript(
								"document.getElementById('cert').style.width = '" + 100 + "%'");
						Main.webEngine.executeScript(
								"document.getElementById('cert').style.height = '" + 92 + "%'");
					}
				});
				
				


				if (job != null) {

					job.printPage(Main.wvCertificado);

					if (job.getJobStatus() == PrinterJob.JobStatus.PRINTING) {

						if (Main.lbCadastroEditado.getText() == "\n [novo] \n ") {

							if (!Main.tfNome.getText().isEmpty() && !Main.tfCPF.getPlainText().isEmpty()
									&& !Main.tcEmpresa.getText().isEmpty()) {

								DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
								LocalDateTime dataHora = LocalDateTime.now();

								Banco.inserir(Main.tfNome.getText().toString(), Main.tfCPF.getText().toString(),
										Main.tfEmpresa.getText().toString(), dtf.format(dataHora).toString());
								Main.tfNome.clear();
								Main.tfCPF.clear();
								Main.tfEmpresa.clear();

								Banco.buscar();
							}

						} else {

							if (!Main.tfNome.getText().isEmpty() && !Main.tfCPF.getPlainText().isEmpty()
									&& !Main.tcEmpresa.getText().isEmpty()) {

								String[] dados = Main.lbCadastroEditado.getText().substring(2).split(" - ");
								Banco.atualizar(Integer.valueOf(dados[0].toString()), Main.tfNome.getText(),
										Main.tfCPF.getText(), Main.tfEmpresa.getText());
								Main.lbCadastroEditado.setText("\n [novo] \n ");
								
								Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
									if (newValue == State.SUCCEEDED) {
										Main.webEngine.executeScript(
												"document.getElementById('nome').innerHTML = '" + "____________________" + "'");
										Main.webEngine.executeScript(
												"document.getElementById('cpf').innerHTML = '" + Main.tfCPF.getText() + "'");
										Main.webEngine.executeScript("document.getElementById('data').innerHTML = '"
												+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
														.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
												+ " de " + LocalDateTime.now().getYear() + "'");
										Main.webEngine.executeScript("document.getElementById('data2').innerHTML = '"
												+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
														.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
												+ " de " + LocalDateTime.now().getYear() + "'");
									}
								});
								
								Main.tfNome.clear();
								Main.tfCPF.clear();
								Main.tfEmpresa.clear();

								Banco.buscar();
							}
						}

						job.endJob();

					} else if (job.getJobStatus() == PrinterJob.JobStatus.CANCELED
							|| job.getJobStatus() == PrinterJob.JobStatus.ERROR) {
						job.endJob();
						Alert alErroImpressao = new Alert(Alert.AlertType.WARNING);
						alErroImpressao.setContentText("Impressão abortada!");
						alErroImpressao.show();
					}
				}
			}
		});

		Main.btSalvar.setOnAction(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				if (Main.lbCadastroEditado.getText() == "\n [novo] \n ") {

					if (!Main.tfNome.getText().isEmpty() && !Main.tfCPF.getPlainText().isEmpty()
							&& !Main.tcEmpresa.getText().isEmpty()) {

						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
						LocalDateTime dataHora = LocalDateTime.now();

						Banco.inserir(Main.tfNome.getText().toString(), Main.tfCPF.getText().toString(),
								Main.tfEmpresa.getText().toString(), dtf.format(dataHora).toString());
						
						Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
							if (newValue == State.SUCCEEDED) {
								Main.webEngine.executeScript(
										"document.getElementById('nome').innerHTML = '" + "____________________" + "'");
								Main.webEngine.executeScript(
										"document.getElementById('cpf').innerHTML = '" + Main.tfCPF.getText() + "'");
								Main.webEngine.executeScript("document.getElementById('data').innerHTML = '"
										+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
												.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
										+ " de " + LocalDateTime.now().getYear() + "'");
								Main.webEngine.executeScript("document.getElementById('data2').innerHTML = '"
										+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
												.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
										+ " de " + LocalDateTime.now().getYear() + "'");
							}
						});
						
						Main.tfNome.clear();
						Main.tfCPF.clear();
						Main.tfEmpresa.clear();

						Banco.buscar();
					}

				} else {

					if (!Main.tfNome.getText().isEmpty() && !Main.tfCPF.getPlainText().isEmpty()
							&& !Main.tcEmpresa.getText().isEmpty()) {

						String[] dados = Main.lbCadastroEditado.getText().substring(2).split(" - ");
						Banco.atualizar(Integer.valueOf(dados[0].toString()), Main.tfNome.getText(),
								Main.tfCPF.getText(), Main.tfEmpresa.getText());
						Main.lbCadastroEditado.setText("\n [novo] \n ");
						
						Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
							if (newValue == State.SUCCEEDED) {
								Main.webEngine.executeScript(
										"document.getElementById('nome').innerHTML = '" + "____________________" + "'");
								Main.webEngine.executeScript(
										"document.getElementById('cpf').innerHTML = '" + Main.tfCPF.getText() + "'");
								Main.webEngine.executeScript("document.getElementById('data').innerHTML = '"
										+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
												.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
										+ " de " + LocalDateTime.now().getYear() + "'");
								Main.webEngine.executeScript("document.getElementById('data2').innerHTML = '"
										+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
												.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
										+ " de " + LocalDateTime.now().getYear() + "'");
							}
						});
						
						Main.tfNome.clear();
						Main.tfCPF.clear();
						Main.tfEmpresa.clear();

						Banco.buscar();
					}
				}
			}
		});

		Main.btNovo.setOnAction(new EventHandler() {

			@Override
			public void handle(Event arg0) {

				Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
					if (newValue == State.SUCCEEDED) {
						Main.webEngine.executeScript(
								"document.getElementById('nome').innerHTML = '" + "____________________" + "'");
						Main.webEngine.executeScript(
								"document.getElementById('cpf').innerHTML = '" + Main.tfCPF.getText() + "'");
						Main.webEngine.executeScript("document.getElementById('data').innerHTML = '"
								+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
										.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
								+ " de " + LocalDateTime.now().getYear() + "'");
						Main.webEngine.executeScript("document.getElementById('data2').innerHTML = '"
								+ LocalDateTime.now().getDayOfMonth() + " de " + LocalDateTime.now().getMonth()
										.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt"))
								+ " de " + LocalDateTime.now().getYear() + "'");
					}
				});

				Main.lbCadastroEditado.setText("\n [novo] \n ");
				Main.tfNome.clear();
				Main.tfCPF.clear();
				Main.tfEmpresa.clear();
			}
		});

		Main.tfNome.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String ov, String nv) {

				Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
					if (newValue == State.SUCCEEDED) {
						if (Main.tfNome.getText().isEmpty()) {
							Main.webEngine.executeScript(
									"document.getElementById('nome').innerHTML = '" + "____________________" + "'");
						} else {
							Main.webEngine.executeScript("document.getElementById('nome').innerHTML = '" + nv + "'");
						}
					}
				});
				Main.webEngine.reload();
			}
		});

		Main.tfCPF.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String ov, String nv) {

				Main.webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
					if (newValue == State.SUCCEEDED) {
						Main.webEngine.executeScript("document.getElementById('cpf').innerHTML = '" + nv + "'");
					}
				});
				Main.webEngine.reload();
			}
		});

		Main.tfPesquisa.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				Banco.pesquisar(newValue, Main.paginacao.getCurrentPageIndex());
			}
		});

		Main.paginacao.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageIndex) {

				Banco.buscar();
				Main.tfPesquisa.clear();

				return new Label("");
			}
		});
	}
}
