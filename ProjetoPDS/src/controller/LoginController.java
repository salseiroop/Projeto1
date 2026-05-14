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

        // Navegação
        this.viewLogin.acaoIrParaCadastro(e -> this.navegador.navegarPara("CADASTRO"));
        this.viewCadastro.acaovoltar(e -> this.navegador.navegarPara("LOGIN"));

        // Lógica de Cadastro
        this.viewCadastro.acaocadastrar(e -> {
            String nome = viewCadastro.getNome();
            String cpf = viewCadastro.getCpf();
            
            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                return;
            }

            // O DAO deve retornar boolean para esta lógica funcionar
            boolean sucesso = dao.salvar(new Usuario(cpf, nome, viewCadastro.isAdministrador()));
            
            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
                this.navegador.navegarPara("LOGIN");
            }
        });

        // Lógica de Login
        this.viewLogin.acaoLogin(e -> {
            String nome = viewLogin.getNome();
            // AQUI ESTAVA O ERRO: Chamando getSenha() para bater com a sua TelaLogin
            String cpf = viewLogin.getSenha(); 
            
            Usuario u = dao.validarLogin(nome, cpf);
            
            if (u != null) {
                if (u.isIsAdmin()) {
                    navegador.navegarPara("ESTOQUE");
                } else {
                    this.compraCtrl.atualizarVitrine(); 
                    navegador.navegarPara("COMPRAS");
                }
                viewLogin.limparCampos();
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou CPF incorretos!");
            }
        });
    }
}