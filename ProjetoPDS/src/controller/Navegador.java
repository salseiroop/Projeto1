package controller;

import javax.swing.JPanel;
import view.JanelaPrincipal;

public class Navegador {
    private JanelaPrincipal janela;

    public Navegador(JanelaPrincipal janela) {
        this.janela = janela;
    }

    public void adicionarPainel(String nome, JPanel tela) {
        this.janela.adicionarTela(tela, nome);
    }

    public void navegarPara(String nome) {
        this.janela.mostrarTela(nome);
    }
}