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
    private EstoqueController estoqueCtrl; 

    public LoginController(TelaLogin viewLogin, TelaCadastroUsuario viewCadastro, UsuarioDAO dao, Navegador navegador, CompraController compraCtrl, EstoqueController estoqueCtrl) {
        this.viewLogin = viewLogin;
        this.viewCadastro = viewCadastro;
        this.dao = dao;
        this.navegador = navegador;
        this.compraCtrl = compraCtrl;
        this.estoqueCtrl = estoqueCtrl;

        this.viewLogin.acaoIrParaCadastro(e -> this.navegador.navegarPara("CADASTRO"));
        this.viewCadastro.acaovoltar(e -> this.navegador.navegarPara("LOGIN"));

        this.viewCadastro.acaocadastrar(e -> {
            String nome = viewCadastro.getNome().trim();
            String cpf = viewCadastro.getCpf();

            if (nome.isEmpty() || cpf.length() < 11) {
                JOptionPane.showMessageDialog(null, "Preencha o nome e o CPF completo!");
                return;
            }

            Usuario novoUsuario = new Usuario(cpf, nome, viewCadastro.isAdministrador());
            
            if (dao.salvar(novoUsuario)) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                viewCadastro.limparcampos();
                this.navegador.navegarPara("LOGIN");
            }
        });

        this.viewLogin.acaoLogin(e -> {
            String nome = viewLogin.getNome().trim();
            String cpf = viewLogin.getSenha(); 

            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Informe o nome e o CPF para entrar!");
                return;
            }

            Usuario u = dao.validarLogin(nome, cpf);
            
            if (u != null) {
                viewLogin.limparCampos();

                if (u.isIsAdmin()) {
                    this.estoqueCtrl.atualizarTabela(); 
                    navegador.navegarPara("ESTOQUE");
                } else {
                    this.compraCtrl.atualizarVitrine(); 
                    navegador.navegarPara("COMPRAS");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nome de usuário ou CPF incorretos!");
            }
        });
    }
}