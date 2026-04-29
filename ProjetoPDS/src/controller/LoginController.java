package controller;

import model.Usuario;
import model.UsuarioDAO;
import view.TelaLogin;
import view.TelaCadastroUsuario;
import javax.swing.JOptionPane;

public class LoginController {
    private TelaLogin viewLogin;
    private TelaCadastroUsuario viewCadastro;
    private UsuarioDAO dao;
    private Navegador navegador;
    private CompraController compraCtrl;

    public LoginController(TelaLogin viewLogin, TelaCadastroUsuario viewCadastro, UsuarioDAO dao, Navegador navegador, CompraController compraCtrl) {
        this.viewLogin = viewLogin;
        this.viewCadastro = viewCadastro;
        this.dao = dao;
        this.navegador = navegador;
        this.compraCtrl = compraCtrl;

        this.viewLogin.acaoIrParaCadastro(e -> this.navegador.navegarPara("CADASTRO"));
        this.viewCadastro.acaovoltar(e -> this.navegador.navegarPara("LOGIN"));

        this.viewCadastro.acaocadastrar(e -> {
            String nome = viewCadastro.getNome();
            String cpf = viewCadastro.getCpf();
            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                return;
            }
            dao.salvar(new Usuario(cpf, nome, viewCadastro.isAdministrador()));
            JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
            this.navegador.navegarPara("LOGIN");
        });

        this.viewLogin.acaoLogin(e -> {
            Usuario u = dao.validarLogin(viewLogin.getNome(), viewLogin.getSenha());
            
            if (u != null) {
                if (u.isIsAdmin()) {
                    navegador.navegarPara("ESTOQUE");
                } else {
                    this.compraCtrl.atualizarVitrine(); 
                    navegador.navegarPara("COMPRAS");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou CPF incorretos!");
            }
        });
    }
}