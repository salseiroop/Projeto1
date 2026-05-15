package main;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import controller.CompraController;
import controller.EstoqueController;
import controller.LoginController;
import controller.Navegador;
import model.ProdutoDAO;
import model.UsuarioDAO;
import view.JanelaPrincipal;
import view.TelaCadastroUsuario;
import view.TelaCompras;
import view.TelaEstoque;
import view.TelaLogin;

public class Main {
    public static void main(String[] args) {
        
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Tahoma", Font.PLAIN, 14)));

        JanelaPrincipal janela = new JanelaPrincipal();
        Navegador navegador = new Navegador(janela);
        
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ProdutoDAO produtoDAO = new ProdutoDAO();

        TelaLogin telaLogin = new TelaLogin();
        TelaCadastroUsuario telaCadastro = new TelaCadastroUsuario();
        TelaEstoque telaEstoque = new TelaEstoque();
        TelaCompras telaCompras = new TelaCompras();

        EstoqueController estoqueCtrl = new EstoqueController(telaEstoque, produtoDAO, navegador);
        CompraController compraCtrl = new CompraController(telaCompras, produtoDAO, navegador);

        new LoginController(telaLogin, telaCadastro, usuarioDAO, navegador, compraCtrl, estoqueCtrl);

        navegador.adicionarPainel("LOGIN", telaLogin);
        navegador.adicionarPainel("CADASTRO", telaCadastro);
        navegador.adicionarPainel("ESTOQUE", telaEstoque);
        navegador.adicionarPainel("COMPRAS", telaCompras);

        janela.setTitle("Supermercado IFSC - Projeto I");
        janela.setLocationRelativeTo(null); 
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setVisible(true);

        navegador.navegarPara("LOGIN");
    }
}