package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.DAO;

public class Cadastro extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtAluno;
	private JLabel lblFoto;

	// CRIAR UM OBJETO PUBLICO (global) para obter o fluxo de bytes(imagem)
	// FileInputStream -. classe modelo respovavel por entrada de dados binarios
	private FileInputStream fis;
	// Criar uma variavel global para armezenar o tamanho em bytes da imagem
	private int tamanho;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Cadastro dialog = new Cadastro();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Cadastro() {
		setTitle("Carômetro- Cadastrar aluno(a)");
		setResizable(false);
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Cadastro.class.getResource("/img/favicon.png")));
		setBounds(100, 100, 450, 451);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Aluno(a):");
		lblNewLabel.setBounds(10, 23, 76, 14);
		contentPanel.add(lblNewLabel);

		txtAluno = new JTextField();
		txtAluno.setBounds(76, 20, 220, 20);
		contentPanel.add(txtAluno);
		txtAluno.setColumns(10);

		JButton btnSelecionar = new JButton("Selecionar Foto");
		btnSelecionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selecionarFoto();
			}
		});
		btnSelecionar.setBounds(32, 61, 147, 23);
		contentPanel.add(btnSelecionar);

		lblFoto = new JLabel("");
		lblFoto.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFoto.setBounds(32, 110, 256, 256);
		contentPanel.add(lblFoto);

		JButton btnSalvar = new JButton("");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvar();
			}
		});
		btnSalvar.setToolTipText("Salvar");
		btnSalvar.setBorderPainted(false);
		btnSalvar.setBackground(SystemColor.menu);
		btnSalvar.setIcon(new ImageIcon(Cadastro.class.getResource("/img/save.png")));
		btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSalvar.setBounds(298, 302, 64, 64);
		contentPanel.add(btnSalvar);
	}// fim do construtor

	DAO dao = new DAO();

	/**
	 * Mteodo responsavel pela seleção da foto
	 */

	private void selecionarFoto() {
		// JFileChooser -> classe modelo que gera um explorador de arquivos
		JFileChooser jfc = new JFileChooser();
		// a linha abaixo muda o titulo do explorador de arquivos do java
		jfc.setDialogTitle("Selecionar arquivo");
		// a linha baixo cria um filtro para escolher determiando tipos de arquivo
		jfc.setFileFilter(
				new FileNameExtensionFilter("Arquivo de Imagens(*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg"));
		// showOpenDialog(this); -> abre o explorador do arquivo
		// int resultado -> saber se o usuario seleciou um arquivo
		int resultado = jfc.showOpenDialog(this);
		// se o usuario escolher uma opção, setar a JLabel
		if (resultado == JFileChooser.APPROVE_OPTION) {
			// tratamento de excecão
			try {
				//// a linha abixo "pega" o arquivo
				fis = new FileInputStream(jfc.getSelectedFile());
				// a linha abaixo obtém o tamanho do arquivo
				tamanho = (int) jfc.getSelectedFile().length();
				// convertendo o arquivo e setando a largura e altura
				Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(),
						lblFoto.getHeight(), Image.SCALE_SMOOTH);
				// setar a JbLabel
				lblFoto.setIcon(new ImageIcon(foto));
				lblFoto.updateUI();

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void salvar() {
		if (txtAluno.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preencha o nome do(a) aluno(a)");
			txtAluno.requestFocus();
		} else {
			String insert = "insert into alunos(nome,foto) values (?,?)";
			try {
				Connection con = dao.conectar();
				PreparedStatement pst = con.prepareStatement(insert);
				pst.setString(1, txtAluno.getText());
				// setar o banco de dados com a imagem
				// fis(arquivo de imagem no formato binairo)
				//tamanho (tamano da imagem em bytes)
				pst.setBlob(2, fis, tamanho);
				int confirma = pst.executeUpdate();
				if (confirma == 1) {
					JOptionPane.showMessageDialog(null, "Aluno(a) cadastrado(a) com sucesso");
					limpar();
				} else {
					JOptionPane.showMessageDialog(null, "Erro! - Aluno(o) não cadastrado(a)");
					limpar();
				}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}

		}
	}

	private void limpar() {
		txtAluno.setText(null);
		lblFoto.setIcon(null);
	}

}// fim do codigo
