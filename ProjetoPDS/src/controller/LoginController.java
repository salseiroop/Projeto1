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

    public LoginController(TelaLogin viewLogin, TelaCadastroUsuario viewCadastro, UsuarioDAO dao,
                           Navegador navegador, CompraController compraCtrl, EstoqueController estoqueCtrl) {
        this.viewLogin    = viewLogin;
        this.viewCadastro = viewCadastro;
        this.dao          = dao;
        this.navegador    = navegador;
        this.compraCtrl   = compraCtrl;
        this.estoqueCtrl  = estoqueCtrl;

        this.viewLogin.acaoIrParaCadastro(e -> this.navegador.navegarPara("CADASTRO"));
        this.viewCadastro.acaovoltar(e -> this.navegador.navegarPara("LOGIN"));

        this.viewCadastro.acaocadastrar(e -> {
            String nome = viewCadastro.getNome().trim();
            String cpf  = viewCadastro.getCpf();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "O campo Nome não pode estar vazio!",
                    "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (nome.length() > 100) {
                JOptionPane.showMessageDialog(null, "O nome deve ter no máximo 100 caracteres!",
                    "Nome Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cpf.length() < 11) {
                JOptionPane.showMessageDialog(null, "Preencha o CPF completo (11 dígitos)!",
                    "CPF Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario novoUsuario = new Usuario(cpf, nome, viewCadastro.isAdministrador());
            if (dao.salvar(novoUsuario)) {
                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                viewCadastro.limparcampos();
                this.navegador.navegarPara("LOGIN");
            }
        });

        this.viewLogin.acaoLogin(e -> {
            String nome = viewLogin.getNome().trim();
            String cpf  = viewLogin.getSenha();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "O campo Nome não pode estar vazio!",
                    "Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (cpf.isEmpty() || cpf.length() < 11) {
                JOptionPane.showMessageDialog(null, "Preencha o CPF completo para entrar!",
                    "CPF Inválido", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Nome de usuário ou CPF incorretos!",
                    "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
