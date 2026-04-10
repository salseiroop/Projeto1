package controller;

import model.Produto;
import model.ProdutoDAO;
import view.TelaCompras;
import java.util.List;

public class CompraController {
    private final TelaCompras view;
    private final ProdutoDAO model;
    private final Navegador navegador;
    private double totalGeral = 0.0;

    public CompraController(TelaCompras view, ProdutoDAO model, Navegador navegador) {
        this.view = view;
        this.model = model;
        this.navegador = navegador;

        atualizarVitrine();

        this.view.acaoAdicionarCarrinho(e -> {
            int linha = view.getTabelaProdutos().getSelectedRow();
            if (linha != -1) {
                String nome = view.getModVitrine().getValueAt(linha, 1).toString();
                double preco = (double) view.getModVitrine().getValueAt(linha, 2);
                
                view.getModCarrinho().addRow(new Object[]{nome, 1, preco});

                totalGeral += preco;
                view.getLblTotalValor().setText(String.format("Total: R$ %.2f", totalGeral));
            } else {
                view.exibirAlerta("Selecione um produto na vitrine!");
            }
        });

        this.view.acaoFinalizarCompra(e -> {
            if (view.getModCarrinho().getRowCount() == 0) {
                view.exibirAlerta("O carrinho está vazio!");
                return;
            }

            System.out.println("--- NOTA FISCAL ---");
            for (int i = 0; i < view.getModCarrinho().getRowCount(); i++) {
                String item = view.getModCarrinho().getValueAt(i, 0).toString();
                double valor = (double) view.getModCarrinho().getValueAt(i, 2);
                System.out.println(item + " - R$ " + valor);
            }
            System.out.println(String.format("TOTAL DA COMPRA: R$ %.2f", totalGeral));
            System.out.println("-------------------");

            view.exibirAlerta("Compra finalizada! Nota emitida no console.");
            limparSessao();
            navegador.navegarPara("LOGIN");
        });

        this.view.acaoLogout(e -> {
            limparSessao();
            this.navegador.navegarPara("LOGIN");
        });
    }

    private void limparSessao() {
        view.getModCarrinho().setRowCount(0);
        totalGeral = 0.0;
        view.getLblTotalValor().setText("Total: R$ 0.00");
    }

    // Mudei para public para o LoginController poder chamar
    public void atualizarVitrine() {
        view.getModVitrine().setRowCount(0);
        List<Produto> lista = model.listarTodos();
        for (Produto p : lista) {
            view.getModVitrine().addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getQuantidade()});
        }
    }
}