package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.mysql.cj.jdbc.Blob;

import model.DAO;

public class Carometro extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtId;
	private JTextField txtAluno;
	private JLabel lblFoto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Carometro dialog = new Carometro();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public Carometro() {
		setTitle("Carômetro - Alunos");
		setResizable(false);
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Carometro.class.getResource("/img/favicon.png")));
		setBounds(100, 100, 476, 521);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Id:");
		lblNewLabel.setBounds(10, 11, 46, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Aluno(a):");
		lblNewLabel_1.setBounds(10, 48, 73, 14);
		getContentPane().add(lblNewLabel_1);

		txtId = new JTextField();
		txtId.setEnabled(false);
		txtId.setBounds(37, 8, 86, 20);
		getContentPane().add(txtId);
		txtId.setColumns(10);

		txtAluno = new JTextField();
		txtAluno.setColumns(10);
		txtAluno.setBounds(65, 45, 208, 20);
		getContentPane().add(txtAluno);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
		btnBuscar.setBounds(283, 44, 89, 23);
		getContentPane().add(btnBuscar);

		lblFoto = new JLabel("");
		lblFoto.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFoto.setBounds(79, 93, 256, 256);
		getContentPane().add(lblFoto);

		btnNewButton = new JButton("Excluir");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Delete();
			}
		});
		btnNewButton.setBounds(341, 375, 89, 23);
		getContentPane().add(btnNewButton);

	}// fim do cosntrutor

	DAO dao = new DAO();
	private JButton btnBuscar;
	private JButton btnNewButton;

	private void buscar() {
		String read = "select * from alunos where nome = ?";
		try {
			Connection con = dao.conectar();
			PreparedStatement pst = con.prepareStatement(read);
			pst.setString(1, txtAluno.getText());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				txtId.setText(rs.getString(1));
				// Ler o binario e converter para imagem
				Blob blob = (Blob) rs.getBlob(3);
				byte[] img = blob.getBytes(1, (int) blob.length());
				BufferedImage imagem = null;
				try {
					// renderizar a imagem(desenhar a foto(pixelx) no "papel")
					imagem = ImageIO.read(new ByteArrayInputStream(img));
				} catch (Exception e) {
					System.out.println(e);
				}
				// setar a imagem no JLabel
				ImageIcon icone = new ImageIcon(imagem);
				Icon foto = new ImageIcon(icone.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(),
						Image.SCALE_SMOOTH));
				lblFoto.setIcon(icone);

			} else {
				JOptionPane.showMessageDialog(null, "Aluno(a) não cadstrado(a)");

			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void Delete() {
		// System.out.println("teste de botao excluir);
		// validação
		int confirma = JOptionPane.showConfirmDialog(null, "Confirma a exclusão desde Aluno ?", "Excluir Aluno",
				JOptionPane.YES_NO_OPTION);
		if (confirma == JOptionPane.YES_OPTION) {
			String delete = "delete from alunos where nome = ?";
			try {
				// abrir a conexão
				Connection con = dao.conectar();
				// preparar a query
				PreparedStatement pst = con.prepareStatement(delete);
				pst.setString(1, txtAluno.getText());
				// execuatr o comandosql e confirmar a exclusão
				int confirmaExcluir = pst.executeUpdate();
				if (confirmaExcluir == 1) {
					limpar();
					JOptionPane.showMessageDialog(null, "Aluno excluido com sucesso");
				} else {
					JOptionPane.showMessageDialog(null, "Erro na exclusão do Aluno");
				}
				// encerrar a conexão
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	private void limpar() {
		txtAluno.setText(null);
		lblFoto.setIcon(null);
		txtId.setText(null);
	}
}// fim do codigo
